package org.firstinspires.ftc.teamcode.utils;

public class Ball {
    private static Ball INSTANCE;

    public final double MASS = 0.0; // KG
    public final double DIAMETER = 0.0; // METERS
    public final double RADIUS = DIAMETER / 2; // METERS
    public final double AREA = (RADIUS * RADIUS) * Math.PI * 4; // METERS^3

    private Ball() {}
    public static Ball getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Ball();
        }

        return INSTANCE;
    }
}
