package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import org.firstinspires.ftc.teamcode.utils.April_Tag;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.utils.PIDController;

public class AutoAim {

    private Bot_Hardware botHardware;
    private April_Tag aprilTag;
    private PIDController turnPID;
    
    private long lastShotTime = 0;
    private double lastSpeed = 0;

    public void init(Bot_Hardware p_bot_hardware, April_Tag p_aprilTag) {
        botHardware = p_bot_hardware;
        aprilTag = p_aprilTag;

        // Tune these
        turnPID = new PIDController(0.8, 0.0, 0.1);
    }

    public void aim() {
        AprilTagDetection detection = aprilTag.getAprilTag();

        if (detection != null) {

            double x = detection.ftcPose.x;
            double y = detection.ftcPose.y;

            double distance = y;

            // radians
            double error = Math.atan2(x, y);

            // --- PID ---
            double turn = turnPID.update(error);

            // clamp
            turn = Math.max(-0.6, Math.min(0.6, turn));

            // deadband (~1 deg)
            if (Math.abs(error) < 0.02) {
                turn = 0;
            }

            // --- Apply to mecanum ---
            double fl = turn;
            double fr = -turn;
            double bl = turn;
            double br = -turn;

            botHardware.frontLeft.setPower(fl);
            botHardware.frontRight.setPower(fr);
            botHardware.backLeft.setPower(bl);
            botHardware.backRight.setPower(br);

            double targetSpeed = getShooterSpeed(distance);

            // spin up shooter EVERY loop
            botHardware.spinner1.setVelocity(targetSpeed);

            // --- Shoot when aligned ---
            double currentSpeed = botHardware.spinner1.getVelocity();
            boolean atSpeed = Math.abs(currentSpeed - targetSpeed) < 50;

            boolean aligned = Math.abs(error) < 0.02;

            boolean stable = Math.abs(currentSpeed - lastSpeed) < 30;
            lastSpeed = currentSpeed;

            boolean notTurning = Math.abs(turn) < 0.05;

            long now = System.currentTimeMillis();

            telemetry.addData("Target", targetSpeed);
            telemetry.addData("Current", currentSpeed);
            telemetry.addData("AtSpeed", atSpeed);
            telemetry.addData("Stable", stable);
            telemetry.addData("Error", error);

            if (aligned && stable && notTurning && atSpeed && now - lastShotTime > 400) {
                botHardware.transfer.setPower(1);
                lastShotTime = now;
                telemetry.addData("Shooting", true);
            } else {
                botHardware.transfer.setPower(0);
                telemetry.addData("Shooting", false);
            }
            telemetry.update();
        } else {
            turnPID.reset();
            botHardware.transfer.setPower(0);
            botHardware.spinner1.setVelocity(0);
        }
    }

    // --- Lookup table ---
    private double getShooterSpeed(double dist) {
        double[][] table = {
                {50, 3000},
                {100, 3200},
                {150, 3400},
                {200, 3600}
        };

        for (int i = 0; i < table.length - 1; i++) {
            if (dist >= table[i][0] && dist <= table[i + 1][0]) {
                double t = (dist - table[i][0]) /
                           (table[i + 1][0] - table[i][0]);

                return table[i][1] +
                        t * (table[i + 1][1] - table[i][1]);
            }
        }

        return table[table.length - 1][1];
    }
}