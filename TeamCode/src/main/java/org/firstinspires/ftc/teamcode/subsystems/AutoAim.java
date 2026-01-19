package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.utils.April_Tag;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class AutoAim {

    /* ===================== Tunables ===================== */

    private static final double AIM_KP = 1.2;           // Turn strength
    private static final double MAX_TURN = 0.6;         // Motor cap
    private static final double GRAVITY = 9.81;         // m/s^2

    /* ===================== State ===================== */

    private final April_Tag aprilTag = new April_Tag();
    private Telemetry telemetry;

    // Last known tag position (meters, camera frame)
    private double tagX = 0.0;
    private double tagY = 0.0;
    private double tagZ = 0.0;
    private boolean hasTag = false;

    /* ===================== Init ===================== */

    public void init(Bot_Hardware botHardware, Telemetry telemetry) {
        aprilTag.init(botHardware);
        this.telemetry = telemetry;
    }

    /* ===================== Aiming ===================== */

    /**
     * Returns mecanum turn values to face the AprilTag
     */
    public double[] getAimPowers() {
        if (!hasTag) {
            return new double[]{0, 0, 0, 0};
        }

        // tagX is left/right offset
        double turn = AIM_KP * tagX;
        turn = clamp(turn, -MAX_TURN, MAX_TURN);

        // mecanum rotation
        return new double[]{ turn, -turn, turn, -turn };
    }

    /* ===================== AprilTag Handling ===================== */

    private void updateAprilTag() {
        AprilTagDetection det = aprilTag.getAprilTag();

        if (det == null || det.robotPose == null) {
            hasTag = false;
            return;
        }

        // Convert all units to meters
        double scale = 1;
        if (det.metadata.distanceUnit == DistanceUnit.MM){
            scale = 0.001;
        } else if (det.metadata.distanceUnit == DistanceUnit.CM) {
            scale = 0.01;
        } else if (det.metadata.distanceUnit == DistanceUnit.INCH) {
            scale = 0.0254;
        }

        tagX = det.robotPose.getPosition().x * scale;
        tagY = det.robotPose.getPosition().y * scale;
        tagZ = det.robotPose.getPosition().z * scale;

        hasTag = true;

        telemetry.addData("Tag X (m)", tagX);
        telemetry.addData("Tag Y (m)", tagY);
        telemetry.addData("Tag Z (m)", tagZ);
        telemetry.update();
    }

    /* ===================== Shooter Math ===================== */

    private double ballExitVelocity(double rpm) {
        double omega = rpm * 2.0 * Math.PI / 60.0;
        return omega * Bot_Hardware.WHEEL_RADIUS;
    }

    /**
     * Returns the optimal launch angle (radians) or -1 if impossible
     */
    public double getLaunchAngle(double motorRPM) {
        updateAprilTag();

        if (!hasTag) {
            return -1;
        }

        double v = ballExitVelocity(motorRPM);
        double d = Math.hypot(tagX, tagY);
        double h = tagZ - Bot_Hardware.WEBCAM_CENTER_TO_SHOOTER;

        double v2 = v * v;
        double disc = v2 * v2 - GRAVITY * (GRAVITY * d * d + 2 * h * v2);

        if (disc < 0 || d < 1e-6) {
            return -1;
        }

        double sqrt = Math.sqrt(disc);

        // Two possible angles
        double low = Math.atan((v2 - sqrt) / (GRAVITY * d));
        double high = Math.atan((v2 + sqrt) / (GRAVITY * d));

        // Prefer high arc (more forgiving)
        return Math.max(low, high);
    }

    /* ===================== Utilities ===================== */

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}
