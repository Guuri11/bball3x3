package com.guuri11.bbal3x3.player;

import static com.guuri11.bbal3x3.ball.Ball.BALL_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

public class Player {
  public static final int PLAYER_WIDTH = 64;
  public static final int PLAYER_HEIGHT = 64;

  private final Rectangle skin;
  private final ShotMeter shotMeter;
  private final SpriteBatch spriteBatch;

  // Textures for each direction
  private final TextureRegion currentTexture;
  float stateTime = 0f;
  int currentFrameIndex = 0;
  float frameDuration = 0.25f;
  private PlayerOrientation playerOrientation;
  private PlayerStatus playerStatus;

  public Player() {
    spriteBatch = new SpriteBatch();
    playerOrientation = PlayerOrientation.WEST;
    playerStatus = PlayerStatus.IDLE;
    currentTexture =
        new TextureRegion(
            new Texture(Gdx.files.internal("Player A/Player A Idle/Player_A_Idle_West_NOBALL.png")),
            0,
            0,
            16,
            24);

    // Create a Rectangle to logically represent the player
    skin = new Rectangle();
    skin.x =
        (float) Gdx.graphics.getWidth() / 2
            - (float) PLAYER_WIDTH / 4; // Center the player horizontally
    skin.y = 20; // Bottom left corner of the player is 20 pixels above the bottom screen edge
    skin.width = PLAYER_WIDTH;
    skin.height = PLAYER_HEIGHT;

    shotMeter = new ShotMeter(skin.height);
  }

  private void setTexture() {
    stateTime += Gdx.graphics.getDeltaTime();

    if (stateTime >= frameDuration) {
      stateTime = 0f;
      currentFrameIndex = (currentFrameIndex + 1) % 4;
    }

    Texture texture =
        new Texture(
            Gdx.files.internal(
                String.format(
                    "Player A/Player A %s/Player_A_%s_%s_NOBALL.png",
                    playerStatus.value, playerStatus.value, playerOrientation.value)));
    currentTexture.setRegion(new TextureRegion(texture), currentFrameIndex * 16, 0, 16, 24);
  }

  public void render(final Matrix4 combined) {
    spriteBatch.setProjectionMatrix(combined);
    spriteBatch.begin();
    setTexture();
    spriteBatch.draw(currentTexture, skin.x, skin.y, skin.width, skin.height);
    spriteBatch.end();

    shotMeter.render(combined);
  }

  public void moveUp() {
    skin.y += 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveUp();
    playerOrientation = PlayerOrientation.NORTH;
    playerStatus = PlayerStatus.RUN;
  }

  public void moveDown() {
    skin.y -= 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveDown();
    playerOrientation = PlayerOrientation.SOUTH;
    playerStatus = PlayerStatus.RUN;
  }

  public void moveLeft() {
    skin.x -= 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveLeft();
    playerStatus = PlayerStatus.RUN;
    playerOrientation = PlayerOrientation.WEST;
  }

  public void moveRight() {
    skin.x += 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveRight();
    playerStatus = PlayerStatus.RUN;
    playerOrientation = PlayerOrientation.EAST;
  }

  public void moveLeftUp() {
    moveLeft();
    moveUp();
    playerOrientation = PlayerOrientation.NORTH_WEST;
  }

  public void moveLeftDown() {
    moveLeft();
    moveDown();
    playerOrientation = PlayerOrientation.SOUTH_WEST;
  }

  public void moveRightUp() {
    moveRight();
    moveUp();
    playerOrientation = PlayerOrientation.NORTH_EAST;
  }

  public void moveRightDown() {
    moveRight();
    moveDown();
    playerOrientation = PlayerOrientation.SOUTH_EAST;
  }

  public void detectBoundLeft() {
    skin.x = BALL_WIDTH + 20;
    shotMeter.detectBoundLeft(skin);
    playerStatus = PlayerStatus.IDLE;
    playerOrientation = PlayerOrientation.SOUTH;
  }

  public void detectBoundRight() {
    shotMeter.detectBoundRight();
    skin.x = shotMeter.getShotMeterSkin().x - 20 - skin.width;
    playerStatus = PlayerStatus.IDLE;
    playerOrientation = PlayerOrientation.SOUTH;
  }

  public void detectBoundTop() {
    skin.y = Gdx.graphics.getWidth() - 64;
    shotMeter.detectBoundTop(skin);
    playerStatus = PlayerStatus.IDLE;
  }

  public void detectBoundBottom() {
    skin.y = 0;
    shotMeter.detectBoundBottom(skin);
    playerStatus = PlayerStatus.IDLE;
  }

  public void dispose() {
    shotMeter.dispose();
  }

  public Rectangle getSkin() {
    return skin;
  }

  public ShotMeter getShotMeter() {
    return shotMeter;
  }

  public PlayerOrientation getCurrentOrientation() {
    return playerOrientation;
  }
}
