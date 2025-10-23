package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.teamcode.subsystems.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.Bot_Hardware;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import android.util.Size;

import java.util.List;

// High level april tag class built using: https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html
// (C) Ladywell robotics
// TODO: Get lens intrinsics from https://www.youtube.com/watch?v=bTcCY3DZM0k
public class April_Tag {
    private static final int RED_TAG_ID = 24;
    private static final int BLUE_TAG_ID = 20;
    private int tag_id = 0;

    AprilTagProcessor aprilTagProcessor;
    AprilTagProcessor.Builder aprilTagProcessorBuilder;
    VisionPortal visionPortal;

    public void init(Bot_Hardware botHardware){
        aprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        aprilTagProcessorBuilder.setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
                .setDrawAxes(true)
                .setDrawTagOutline(true)
                .setDrawTagID(true);
        aprilTagProcessor = aprilTagProcessorBuilder.build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(botHardware.camera)
                .addProcessor(aprilTagProcessor)
                .setCameraResolution(new Size(640, 480))  // Best for logitech C720
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)  // MJPEG format uses less bandwidth than the default YUY2.
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();

        switch (Alliance.getInstance().selected){
            case RED:
                tag_id = RED_TAG_ID;
            case BLUE:
                tag_id = BLUE_TAG_ID;
            case UNASSIGNED:
                tag_id = RED_TAG_ID;
            default:
                tag_id = RED_TAG_ID;
        }
    }

    // Looks for the current tag ID and if found returns a AprilTagDetection object, otherwise returns null
    public AprilTagDetection getAprilTag(){
        List<AprilTagDetection> detectionList = aprilTagProcessor.getDetections();
        for (AprilTagDetection detection : detectionList) {
            if (detection.metadata != null && detection.id == tag_id){
                return detection;
            }
        }
        return null;
    }

    public void setState(boolean state){
        visionPortal.setProcessorEnabled(aprilTagProcessor, state); // Use to manage CPU usage during game
    }
}
