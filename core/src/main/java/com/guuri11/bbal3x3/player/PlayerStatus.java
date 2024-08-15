package com.guuri11.bbal3x3.player;

public enum PlayerStatus {
  IDLE("Idle"),
  JUMP_SHOT("Jump_Shoot"),
  PASS_RECEIVE("Pass_Receive"),
  RUN("Run"),
  STAND_SHOOT("Stand_Shoot");

  public final String value;

  PlayerStatus(String value) {
    this.value = value;
  }
}
