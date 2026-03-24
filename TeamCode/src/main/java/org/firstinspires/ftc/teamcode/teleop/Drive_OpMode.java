package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.subsystems.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.AutoAim;
import org.firstinspires.ftc.teamcode.subsystems.Bot_Hardware;
import org.firstinspires.ftc.teamcode.utils.April_Tag;

@TeleOp(name="Drive", group = "!run!")
public class Drive_OpMode extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private final Bot_Hardware botHardware = new Bot_Hardware();
    private final AutoAim autoAim = new AutoAim();

    void l_init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        botHardware.init(hardwareMap);
        autoAim.init(botHardware, new April_Tag());

        // Alliance selection
        if (Alliance.getInstance().selected != Alliance.Options.UNASSIGNED) {
            while (!isStarted() && !isStopRequested()) {
                if (gamepad1.a || gamepad2.a) {
                    Alliance.getInstance().selected = Alliance.Options.RED;
                } else if (gamepad1.b || gamepad2.b) {
                    Alliance.getInstance().selected = Alliance.Options.BLUE;
                }

                telemetry.addLine("=== Alliance Selection ===");
                telemetry.addData("Press A", "for RED");
                telemetry.addData("Press B", "for BLUE");
                telemetry.addData("Current Selection", Alliance.getInstance().selected);
                telemetry.update();

                sleep(100);
            }
        } else {
            waitForStart();
        }

        runtime.reset();
    }

    void drive() {
        float speed_mode = 1 - (gamepad1.right_trigger * 0.5f);

        double y = gamepad1.left_stick_y * speed_mode;
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

        botHardware.frontRight.setPower(-frontRightPower);
        botHardware.frontLeft.setPower(frontLeftPower);
        botHardware.backRight.setPower(-backRightPower);
        botHardware.backLeft.setPower(-backLeftPower);
    }

    double calculate_ball_exit_vel(double rpm){
        double omega = rpm * 2.0 * Math.PI / 60.0; // rad/s
        return omega * Bot_Hardware.WHEEL_RADIUS; // m/s
    }

    void spinner(){
        double spinner1_vel = botHardware.spinner1.getVelocity(AngleUnit.DEGREES);
        double spinner2_vel = botHardware.spinner1.getVelocity(AngleUnit.DEGREES);

        double spinner1_rpm = (spinner1_vel * 60 * Bot_Hardware.HDHEX_G_RATIO) / 360;
        double spinner2_rpm = (spinner2_vel * 60 * Bot_Hardware.HDHEX_G_RATIO) / 360;

        if (gamepad2.right_trigger > 0.5f){
            botHardware.spinner1.setPower(1);

            double average_rpm = (spinner1_rpm + spinner2_rpm) / 2;
            double ball_vel = calculate_ball_exit_vel(average_rpm);
            telemetry.addData("Ball Velocity", "Speed: " + ball_vel);
        } else {
            botHardware.spinner1.setPower(0);
        }

        telemetry.addData("Spinner 1", "Encoder: " +
                spinner1_vel +
                "TPS\n\r" +
                "Speed: " +
                spinner1_rpm+
                "RPM");
        telemetry.addData("Spinner 2", "Encoder: " +
                spinner2_vel +
                "TPS\n\r" +
                "Speed: " +
                spinner2_rpm +
                "RPM");
    }

    void active_intake(){
        if (gamepad2.left_trigger > 0.1f){
            botHardware.intake.setPower(-gamepad2.left_trigger);
        } else {
            botHardware.intake.setPower(0);
        }
        telemetry.addData("IntakeSpinner", "Value:"
        +gamepad2.left_trigger
        );
    }

    void auto_aim(){
        if (gamepad2.right_bumper) {
            // auto aim here
            autoAim.aim();
        }
    }

    void transfer(){
        botHardware.transfer.setPower(gamepad2.right_stick_y);
    }

    void l_update() {
        drive();
        spinner();
        active_intake();
        transfer();
        auto_aim();

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