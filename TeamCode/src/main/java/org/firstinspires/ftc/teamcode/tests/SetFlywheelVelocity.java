package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Bot_Hardware;

@TeleOp(name = "Utility: Set Flywheel Velocity", group = "Tests")
public class SetFlywheelVelocity extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private final Bot_Hardware botHardware = new Bot_Hardware();

    private int targetSpeed = 0;

    void l_init() {
        botHardware.init(hardwareMap);

        telemetry.addLine("Use the 'Right Joystick Y' to select target velocity and test the flywheel.");
        telemetry.addLine("Right bumper spins the transfer to launch the balls.");

        waitForStart();
    }

    void l_update() {
        botHardware.spinner1.setVelocity(targetSpeed);
        double currentSpeed = botHardware.spinner1.getVelocity();
        boolean atSpeed = Math.abs(currentSpeed - targetSpeed) < 50;

        targetSpeed += (int) (gamepad2.right_stick_y * 10);

        if (gamepad1.right_bumper) {
            botHardware.transfer.setPower(1);
        } else {
            botHardware.transfer.setPower(0);
        }

        telemetry.addData("Target speed: ", targetSpeed);
        telemetry.addData("At speed: ", atSpeed);
    }

    @Override
    public void runOpMode() {
        l_init();

        while (opModeIsActive()) {
            l_update();
        }
    }
}
