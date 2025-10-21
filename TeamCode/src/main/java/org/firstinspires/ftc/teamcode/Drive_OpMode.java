package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Drive")
public class Drive_OpMode extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private final Bot_Hardware botHardware = new Bot_Hardware();

    void l_init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        botHardware.init(hardwareMap);

        waitForStart();
        runtime.reset();
    }

    void drive() {
        float speed_mode = 1 - (gamepad1.right_trigger * 0.5f);

        double y = -gamepad1.left_stick_y * speed_mode;
        double x = gamepad1.left_stick_x * 1.1 * speed_mode;  // Multiplied by 1.1 because i saw that ftc teams said they find strafing weaker than forwards and backwards
        double rx = gamepad1.right_stick_x * speed_mode;

        double frontLeftPower = y + x + rx;
        double backLeftPower = -(y - x + rx);
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(backLeftPower), Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));

        if (max > 1.0) {
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }
        // TODO: update motor power
    }

    void spinner(){
        if (gamepad2.right_trigger > 0.5f){
            botHardware.spinner1.setPower(1);
            botHardware.spinner2.setPower(1);
        }
    }

    void active_intake(){
        if (gamepad2.left_trigger > 0.5f){
            botHardware.intake.setPower(gamepad2.left_trigger);
        }
    }

    void l_update() {
        drive();
        spinner();
        active_intake();

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        l_init();

        while (opModeIsActive()) {
            l_update();
        }
    }
}