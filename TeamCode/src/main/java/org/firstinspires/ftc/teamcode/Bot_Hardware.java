package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

public class Bot_Hardware{
    public DcMotorEx spinner1 = null;
    public DcMotorEx spinner2 = null;
    public DcMotorEx intake = null;

    public void init(HardwareMap hardwareMap){
        spinner1 = hardwareMap.get(DcMotorEx.class, "spinner1");
        spinner2 = hardwareMap.get(DcMotorEx.class, "spinner2");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
    }
}
