package org.firstinspires.ftc.teamcode.subsystems;

public final class Alliance {
    private static Alliance INSTANCE;

    public enum Options {
        RED, BLUE, UNASSIGNED
    }
    public Options selected = Options.UNASSIGNED;

    private Alliance() {}
    public static Alliance getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Alliance();
        }

        return INSTANCE;
    }
}
