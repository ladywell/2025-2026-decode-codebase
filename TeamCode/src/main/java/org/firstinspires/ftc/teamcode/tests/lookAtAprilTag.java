package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Bot_Hardware;
import org.firstinspires.ftc.teamcode.utils.April_Tag;
import org.firstinspires.ftc.teamcode.utils.PIDController;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Utility: Look at April Tag", group = "Tests")
public class lookAtAprilTag extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private final Bot_Hardware botHardware = new Bot_Hardware();
    private April_Tag aprilTag;
    private PIDController turnPID;
    void l_init() {
        botHardware.init(hardwareMap);
        aprilTag.init(botHardware);

        telemetry.addLine("This will scan the camera for an April tag");
        telemetry.addLine("If one if found it will center it");

        // Tune these
        turnPID = new PIDController(0.8, 0.0, 0.1);

        waitForStart();
    }

    void l_update() {
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

            boolean aligned = Math.abs(error) < 0.02;
            telemetry.addData("Aligned: ", aligned);
        } else {
            turnPID.reset();
        }
    }

    @Override
    public void runOpMode() {
        l_init();

        while (opModeIsActive()) {
            l_update();
        }
    }
}
