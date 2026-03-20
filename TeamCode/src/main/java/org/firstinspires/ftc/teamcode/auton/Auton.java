package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.pedros.Tuning.follower;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.pedros.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Bot_Hardware;

@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class Auton extends OpMode {

  private TelemetryManager panelsTelemetry; // Panels Telemetry instance
  public Follower follower; // Pedro Pathing follower instance
  private int pathState; // Current autonomous path state (state machine)
  private ElapsedTime pathTimer; // Timer for path state machine
  private Paths paths; // Paths defined in the Paths class

  private final Bot_Hardware botHardware = new Bot_Hardware();

  @Override
  public void init() {
    panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    botHardware.init(hardwareMap);
    // ...
    panelsTelemetry.debug("Status", "Initialized");
    panelsTelemetry.update(telemetry);

    follower = Constants.createFollower(hardwareMap);
    follower.setStartingPose(
      new Pose(21.289, 124.366, Math.toRadians(137.604))
    );

    pathTimer = new ElapsedTime();
    paths = new Paths(follower);
  }

  @Override
  public void loop() {
    follower.update();
    pathState = autonomousPathUpdate();

    panelsTelemetry.debug("Path State", pathState);
    panelsTelemetry.debug("X", follower.getPose().getX());
    panelsTelemetry.debug("Y", follower.getPose().getY());
    panelsTelemetry.debug("Heading", follower.getPose().getHeading());
    panelsTelemetry.update(telemetry);
  }

  public static class Paths {

        public PathChain line1;
        public PathChain line2;
        public PathChain line3;
        public PathChain line4;

        public Paths(Follower follower) {
            line1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(21.289, 124.366), new Pose(5.318, 91.472))
                    )
                    .setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            line2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(57.318, 91.472), new Pose(36.137, 84.414))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            line3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(36.137, 84.414), new Pose(14.000, 84.414))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            line4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(14.000, 84.414), new Pose(57.318, 91.472))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(145), Math.toRadians(145))
                    .build();
        }
    }

    public int autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(paths.line1, true);
                setPathState(1);
                flywheel(true);
                break;
            case 1:
                if (!follower.isBusy()) {
                  setPathState(2);
                }
                break;
            case 2:
                setPathState(3);
                transfer(true);
                break;
            case 3:
                if (pathTimer.milliseconds() > 1000) {
                    flywheel(false);
                    transfer(false);
                    follower.followPath(paths.line2, true);
                    setPathState(4);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    transfer(true);
                    intake(true);
                    follower.followPath(paths.line3, true);
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy()) {
                    transfer(false);
                    intake(false);
                    flywheel(true);
                    follower.followPath(paths.line4, true);
                    setPathState(6);
                }
                break;
            case 6:
                if (!follower.isBusy()) {
                    transfer(true);
                    setPathState(7);
                }
                break;
            case 7:
                if (pathTimer.milliseconds() > 1000) {
                    setPathState(100);
                }
            case 100:
                transfer(false);
                intake(false);
                flywheel(false);
                requestOpModeStop();
                pathState = -1;
                break;
        }
        return pathState;
    }

  public void setPathState(int pState) {
    pathState = pState;
    pathTimer.reset();
  }

  public void intake(boolean state) {
      if (botHardware != null) {
          botHardware.intake.setPower(state ? 1 : 0);
      }
  }

  public void flywheel(boolean state) {
      if (botHardware != null) {
          botHardware.spinner1.setPower(state ? 1 : 0);
      }
  }

  public void transfer(boolean state) {
      // botHardware.intake.setPower(state ? 1 : 0);
  }
}
