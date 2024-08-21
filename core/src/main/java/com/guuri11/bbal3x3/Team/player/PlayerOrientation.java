package com.guuri11.bbal3x3.Team.player;

public enum PlayerOrientation {
  NORTH("North"),
  WEST("West"),
  EAST("East"),
  SOUTH("South"),
  NORTH_WEST("NorthWest"),
  SOUTH_WEST("SouthWest"),
  NORTH_EAST("NorthEast"),
  SOUTH_EAST("SouthEast");

  public final String value;

  PlayerOrientation(String value) {
    this.value = value;
  }
}
