package org.firstinspires.ftc.teamcode;

import com.google.blocks.ftcrobotcontroller.util.CurrentGame;

import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class April_Tag {
    AprilTagProcessor aprilTagProcessor;

    void init(){
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
    }
}
