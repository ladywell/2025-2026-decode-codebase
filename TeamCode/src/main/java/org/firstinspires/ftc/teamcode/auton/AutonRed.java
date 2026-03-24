package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auton RED", group = "Auton")
public class AutonRed extends AutonBase {
    @Override
    protected int allianceDirection() { return 1; }
}