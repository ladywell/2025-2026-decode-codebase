package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auton BLUE", group = "Auton")
public class AutonBlue extends AutonBase {
    @Override
    protected int allianceDirection() { return -1; }
}