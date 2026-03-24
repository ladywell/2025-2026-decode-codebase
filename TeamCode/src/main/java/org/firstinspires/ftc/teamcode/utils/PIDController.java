package org.firstinspires.ftc.teamcode.utils;

public class PIDController {
    double kP, kI, kD;
    double integral = 0;
    double lastError = 0;
    long lastTime = System.nanoTime();

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public double update(double error) {
        long now = System.nanoTime();
        double dt = (now - lastTime) / 1e9;
        lastTime = now;

        if (dt <= 0) return 0;

        integral += error * dt;
        integral = Math.max(-1, Math.min(1, integral)); // anti-windup

        double derivative = (error - lastError) / dt;
        lastError = error;

        return kP * error + kI * integral + kD * derivative;
    }

    public void reset() {
        integral = 0;
        lastError = 0;
    }
}