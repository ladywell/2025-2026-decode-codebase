package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class Bot_Hardware{
    public DcMotorEx spinner1 = null;
    public DcMotorEx hoodServo = null;
    public DcMotorEx intake = null;
    public WebcamName camera = null;
    public DcMotorEx frontLeft = null;
    public DcMotorEx frontRight = null;
    public DcMotorEx backLeft = null;
    public DcMotorEx backRight = null;


    public static final int HDHEX_G_RATIO = 20;
    public static final double WHEEL_RADIUS = 0.045; // 45mm from rev site
    public static final double WEBCAM_CENTER_TO_SHOOTER = 0;

    public void init(HardwareMap hardwareMap){
        spinner1 = hardwareMap.get(DcMotorEx.class, "spinner1");
        hoodServo = hardwareMap.get(DcMotorEx.class, "hoodServo");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        camera = hardwareMap.get(WebcamName.class, "camera");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
    }
}


