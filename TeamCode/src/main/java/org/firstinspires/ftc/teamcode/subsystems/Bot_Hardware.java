package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class Bot_Hardware{
    public DcMotorEx spinner1 = null;
    public DcMotorEx spinner2 = null;
    public DcMotorEx intake = null;
    public WebcamName camera = null;

    public static final int HDHEX_G_RATIO = 20;
    public static final double WHEEL_RADIUS = 0.03175; // TODO: Fix this
    public static final double WEBCAM_CENTER_TO_SHOOTER = 0;

    public void init(HardwareMap hardwareMap){
        spinner1 = hardwareMap.get(DcMotorEx.class, "spinner1");
        spinner2 = hardwareMap.get(DcMotorEx.class, "spinner2");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        camera = hardwareMap.get(WebcamName.class, "camera");
    }
}


