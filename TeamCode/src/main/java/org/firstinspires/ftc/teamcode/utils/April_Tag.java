package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class April_Tag {
    AprilTagProcessor aprilTagProcessor;

    void init(){
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
    }
}
