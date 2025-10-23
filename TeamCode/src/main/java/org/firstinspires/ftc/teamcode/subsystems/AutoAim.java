package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.April_Tag;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Vector;

public class AutoAim {
    double AIM_SPEED = 0.9;
    April_Tag april_tag = new April_Tag();
    Telemetry telemetry = null;
    Vector<Double> last_apriltag_position = null;

    public void init(Bot_Hardware botHardware, Telemetry telemetry_instance){
        april_tag.init(botHardware);
        telemetry = telemetry_instance;
    }

    double calculate_ball_exit_vel(double rpm){
        double omega = rpm * 2.0 * Math.PI / 60.0; // rad/s
        return omega * Bot_Hardware.WHEEL_RADIUS; // m/s
    }

    double lerp(double a, double b, double t){
        return a * (1.0 - t) + (b * t);
    }

    public double[] look_at_apriltag(){
        if (last_apriltag_position == null){
            return null;
        }

        double offset = last_apriltag_position.get(0);
        double speed = AIM_SPEED / 10;
        double rx = lerp(0, offset, speed) / offset; // returns AIM_SPEED, 0 or -AIM_SPEED depending on where the tag is

        return new double[]{rx, -rx, rx, rx};
    }

    private AprilTagDetection handle_tag(){
        AprilTagDetection april_tag_detection = april_tag.getAprilTag();
        if (april_tag_detection != null){
            last_apriltag_position.clear();
            switch (april_tag_detection.metadata.distanceUnit){
                case CM:
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().x / 100, 0);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().y / 100, 1);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().z / 100, 2);
                case MM:
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().x / 1000, 0);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().y / 1000, 1);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().z / 1000, 2);
                case METER:
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().x, 0);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().y, 1);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().z, 2);
                case INCH:
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().x / 39.37, 0);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().y / 39.37, 1);
                    last_apriltag_position.setElementAt(april_tag_detection.robotPose.getPosition().z / 39.37, 2);
            }
        } else {
            last_apriltag_position = null;
            return null;
        }
        return april_tag_detection;
    }

    private static double[] solveLaunchAngles(double v, double x, double y, double z) {
        double d = Math.hypot(x, y); // Distance along ground plain
        double g = 9.81; // Gravity
        if (d <= 1e-9) { // 1x10^-9
            return new double[]{};
        }

        double v2 = v * v;
        double delta = v2*v2 - g*(g*d*d + 2*z*v2);
        if (delta < 0){
            return new double[]{};
        }

        double s = Math.sqrt(delta);
        double tan1 = (v2 + s) / (g * d);
        double tan2 = (v2 - s) / (g * d);
        double th1 = Math.atan(tan1);
        double th2 = Math.atan(tan2);

        if (Math.abs(th1 - th2) < 1e-9) {
            return new double[]{th1};
        } else {
            double low = Math.min(th1, th2);
            double high = Math.max(th1, th2);
            return new double[]{low, high};
        }
    }

    public double get_launch_angle(double motor_velocity){
        double ball_velocity = calculate_ball_exit_vel(motor_velocity);
        AprilTagDetection detection = handle_tag();
        if (detection == null){
            return -1;
        }

        double[] launchAngles = solveLaunchAngles(ball_velocity,
                                        last_apriltag_position.get(0),
                                        last_apriltag_position.get(1),
                                        last_apriltag_position.get(2) - Bot_Hardware.WEBCAM_CENTER_TO_SHOOTER);
        if (launchAngles.length == 0) {
            return -1;
        }
        int middle_item = Math.max(launchAngles.length / 2, 1);

        return launchAngles[middle_item];
    }
}
