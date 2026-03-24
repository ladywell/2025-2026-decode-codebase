package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.AutoAim;
import org.firstinspires.ftc.teamcode.subsystems.Bot_Hardware;
import org.firstinspires.ftc.teamcode.utils.April_Tag;

public abstract class AutonBase extends LinearOpMode {

    protected Bot_Hardware botHardware = new Bot_Hardware();
    protected April_Tag aprilTag = new April_Tag();
    protected AutoAim autoAim = new AutoAim();

    private static final int SHOTS_REQUIRED     = 3;
    private static final long AIM_TIMEOUT_MS    = 5000;
    private static final long SHOOT_DURATION_MS = 300;
    private static final long SPIN_UP_MS        = 1000;

    // ~1 meter back into shooting position — tune if needed
    private static final int DRIVE_TO_SHOOT_TICKS = 2800;
    private static final double DRIVE_TO_SHOOT_POWER = 0.5;

    // Leave zone after shooting
    private static final int LEAVE_ZONE_TICKS   = 1500;
    private static final double LEAVE_POWER     = 0.5;

    protected abstract int allianceDirection();

    @Override
    public void runOpMode() {
        botHardware.init(hardwareMap);
        aprilTag.init(botHardware);
        autoAim.init(botHardware, aprilTag);

        telemetry.addLine("Initialized — waiting for start");
        telemetry.update();
        waitForStart();

        switch (allianceDirection()){
            case 1:
                Alliance.getInstance().selected = Alliance.Options.RED;
                break;
            case -1:
                Alliance.getInstance().selected = Alliance.Options.BLUE;
                break;
            default:
                return;
        }

        if (!opModeIsActive()) return;

        // ── Phase 1: Reverse ~1m into shooting position ───────────────────
        telemetry.addLine("Driving to shooting position...");
        telemetry.update();

        driveByTicks(-DRIVE_TO_SHOOT_TICKS, DRIVE_TO_SHOOT_POWER);
        stopDrive();

        // ── Phase 2: Use AutoAim to shoot 3 preloads ──────────────────────
        int shotsLanded = 0;

        while (opModeIsActive() && shotsLanded < SHOTS_REQUIRED) {

            telemetry.addData("Phase", "Aiming");
            telemetry.addData("Shots", shotsLanded + "/" + SHOTS_REQUIRED);

            long aimStart = System.currentTimeMillis();
            boolean shotFired = false;

            botHardware.spinner1.setVelocity(3200);
            sleep(SPIN_UP_MS);

            while (opModeIsActive() && !shotFired) {

                if (System.currentTimeMillis() - aimStart > AIM_TIMEOUT_MS) {
                    telemetry.addLine("Aim timeout — skipping shot");
                    telemetry.update();
                    break;
                }

                // Hand off entirely to your AutoAim subsystem
                autoAim.aim();

                if (botHardware.transfer.getPower() > 0.5) {
                    sleep(SHOOT_DURATION_MS);
                    botHardware.transfer.setPower(0);
                    shotsLanded++;
                    shotFired = true;
                }

                telemetry.addData("Shots fired", shotsLanded);
                telemetry.update();
            }
        }

        botHardware.spinner1.setVelocity(0);
        stopDrive();

        // ── Phase 3: Leave the shooting zone ──────────────────────────────
        telemetry.addLine("Leaving zone...");
        telemetry.update();

        driveByTicks(LEAVE_ZONE_TICKS, LEAVE_POWER);
        stopDrive();

        telemetry.addLine("Auton complete");
        telemetry.update();
    }

    private void driveByTicks(int ticks, double power) {
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Negative ticks = reverse. allianceDirection flips left/right for Blue.
        int target = ticks * allianceDirection();

        botHardware.frontLeft.setTargetPosition(target);
        botHardware.frontRight.setTargetPosition(-target);
        botHardware.backLeft.setTargetPosition(target);
        botHardware.backRight.setTargetPosition(-target);

        botHardware.frontLeft.setPower(power);
        botHardware.frontRight.setPower(power);
        botHardware.backLeft.setPower(power);
        botHardware.backRight.setPower(power);

        while (opModeIsActive() &&
                (botHardware.frontLeft.isBusy()  ||
                        botHardware.frontRight.isBusy() ||
                        botHardware.backLeft.isBusy()   ||
                        botHardware.backRight.isBusy())) {
            telemetry.addData("FL ticks", botHardware.frontLeft.getCurrentPosition());
            telemetry.addData("FR ticks", botHardware.frontRight.getCurrentPosition());
            telemetry.update();
        }

        stopDrive();
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void stopDrive() {
        botHardware.frontLeft.setPower(0);
        botHardware.frontRight.setPower(0);
        botHardware.backLeft.setPower(0);
        botHardware.backRight.setPower(0);
    }

    private void setDriveMode(DcMotor.RunMode mode) {
        botHardware.frontLeft.setMode(mode);
        botHardware.frontRight.setMode(mode);
        botHardware.backLeft.setMode(mode);
        botHardware.backRight.setMode(mode);
    }
}