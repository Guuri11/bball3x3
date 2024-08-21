package com.guuri11.bbal3x3.player;

public enum PlayerStatus {
  DRIBBLE("Dribble", 96, 24, 4),
  IDLE("Idle", 64, 24, 4),
  IDLE_WITH_BALL("Idle_With_Ball", 96, 24, 4),
  JUMP_SHOT("Jump_Shoot", 32, 24, 2),
  JUMP_SHOT_WITH_BALL("Jump_Shoot_With_Ball", 72, 32, 3),
  PASS_RECEIVE("Pass_Receive", 16, 24, 1),
  PASS_RECEIVE_WITH_BALL("Pass_Receive_With_Ball", 16, 24, 1),
  RUN("Run", 64, 24, 4),
  STAND_SHOOT("Stand_Shoot", 32, 24, 2),
  STAND_SHOOT_WITH_BALL("Stand_Shoot_With_Ball", 48, 32, 3);

  public final String value;
  public final int spriteWidth;
  public final int spriteHeight;
  public final int frameWidth;
  public final int sprites;

  PlayerStatus(String value, int spriteWidth, int spriteHeight, int sprites) {
    this.value = value;
    this.spriteWidth = spriteWidth;
    this.spriteHeight = spriteHeight;
    this.sprites = sprites;
    this.frameWidth = spriteWidth / sprites;
  }
}
