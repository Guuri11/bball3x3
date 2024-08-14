package com.guuri11.bbal3x3.player;

public enum PlayerOrientation {
    NORTH("North"), WEST("West"), EAST("East"), SOUTH("South");

    public final String value;

    PlayerOrientation(String value) {
        this.value = value;
    }
}
