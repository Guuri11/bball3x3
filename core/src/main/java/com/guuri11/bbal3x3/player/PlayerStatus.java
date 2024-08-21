package com.guuri11.bbal3x3.player;

public enum PlayerStatus {
  DRIBBLE("Dribble"),
  IDLE("Idle"),
  IDLE_WITH_BALL("Idle_With_Ball"),
  JUMP_SHOT("Jump_Shoot"),
  JUMP_SHOT_WITH_BALL("Jump_Shoot_With_Ball"),
  PASS_RECEIVE("Pass_Receive"),
  PASS_RECEIVE_WITH_BALL("Pass_Receive_With_Ball"),
  RUN("Run"),
  STAND_SHOOT("Stand_Shoot"),
  STAND_SHOOT_WITH_BALL("Stand_Shoot_With_Ball");

  public final String value;

  PlayerStatus(String value) {
    this.value = value;
  }
}
