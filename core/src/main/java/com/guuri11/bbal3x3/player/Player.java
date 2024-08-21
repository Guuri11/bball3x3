package com.guuri11.bbal3x3.player;

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
  float frameDuration = 0.5f;
  private PlayerOrientation playerOrientation;
  private PlayerStatus playerStatus;

  // Physics for jumps
  private boolean isShooting;
  private float velocityY;
  private float floorY;
  private boolean reachedJumpLimit = false;

  public Player() {
    spriteBatch = new SpriteBatch();
    playerOrientation = PlayerOrientation.WEST;
    playerStatus = PlayerStatus.IDLE_WITH_BALL;
    currentTexture =
        new TextureRegion(
            new Texture(
                Gdx.files.internal(
                    String.format(
                        "Player A/Player A %s/Player_A_%s_%s.png",
                        playerStatus.value, playerStatus.value, playerOrientation.value))),
            0,
            0,
            playerStatus.frameWidth,
            playerStatus.spriteHeight);

    // Create a Rectangle to logically represent the player
    skin = new Rectangle();
    skin.x =
        (float) Gdx.graphics.getWidth() / 2
            - (float) PLAYER_WIDTH / 4; // Center the player horizontally
    skin.y = 20; // Bottom left corner of the player is 20 pixels above the bottom screen edge
    skin.width = PLAYER_WIDTH;
    skin.height = PLAYER_HEIGHT;

    velocityY = 0;
    isShooting = false;
    shotMeter = new ShotMeter(skin.height);
  }

  private void setTexture() {
    stateTime += Gdx.graphics.getDeltaTime();

    if (stateTime >= frameDuration) {
      stateTime = 0f;
      currentFrameIndex = (currentFrameIndex + 1) % playerStatus.sprites;
    }

    String texturePath =
        String.format(
            "Player A/Player A %s/Player_A_%s_%s.png",
            playerStatus.value, playerStatus.value, playerOrientation.value);

    Texture texture = new Texture(Gdx.files.internal(texturePath));
    currentTexture.setRegion(
        new TextureRegion(texture),
        currentFrameIndex * playerStatus.frameWidth,
        0,
        playerStatus.frameWidth,
        playerStatus.spriteHeight);
  }

  public void render(final Matrix4 combined) {

    updateJump();

    spriteBatch.setProjectionMatrix(combined);
    spriteBatch.begin();
    setTexture();
    spriteBatch.draw(currentTexture, skin.x, skin.y, skin.width, skin.height);
    spriteBatch.end();

    shotMeter.render(combined);
  }

  public void jumpShot() {
    if (!isShooting) {
      isShooting = true;
      floorY = skin.y;
      velocityY = 300;
      setPlayerStatus(PlayerStatus.JUMP_SHOT_WITH_BALL);
      this.shotMeter.setShooting(true);
      reachedJumpLimit = false;
    }
  }

  public void updateJump() {
    if (isShooting) {
      // If limit was not reached, keep going up
      float jumpHeightLimit = 1500;
      float gravity = -1500f;
      if (!reachedJumpLimit && skin.y < floorY + jumpHeightLimit) {
        // Increment jump height
        velocityY += gravity * Gdx.graphics.getDeltaTime();
        skin.y += velocityY * Gdx.graphics.getDeltaTime();
      } else {
        // Limit reached, start falling
        reachedJumpLimit = true;
        velocityY += gravity * Gdx.graphics.getDeltaTime();
        skin.y += velocityY * Gdx.graphics.getDeltaTime();
      }

      // Verify that player touched the ground
      if (skin.y <= floorY) {
        skin.y = floorY; // make sure that player does not go through ground
        isShooting = false;
        velocityY = 0;
        setPlayerStatus(PlayerStatus.IDLE);
        this.shotMeter.setShooting(false);
      }
    }
  }

  public void moveUp() {
    skin.y += 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveUp();
    playerOrientation = PlayerOrientation.NORTH;
    setPlayerStatus(PlayerStatus.DRIBBLE);
  }

  public void moveDown() {
    if (!isShooting) {
      skin.y -= 200 * Gdx.graphics.getDeltaTime();
      shotMeter.moveDown();
      playerOrientation = PlayerOrientation.SOUTH;
      setPlayerStatus(PlayerStatus.DRIBBLE);
    }
  }

  public void moveLeft() {
    skin.x -= 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveLeft();
    setPlayerStatus(PlayerStatus.DRIBBLE);
    playerOrientation = PlayerOrientation.WEST;
  }

  public void moveRight() {
    skin.x += 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveRight();
    setPlayerStatus(PlayerStatus.DRIBBLE);
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
    skin.x = 0;
    shotMeter.detectBoundLeft(skin);
    setPlayerStatus(PlayerStatus.IDLE);
    playerOrientation = PlayerOrientation.SOUTH;
  }

  public void detectBoundRight() {
    shotMeter.detectBoundRight();
    skin.x = shotMeter.getShotMeterSkin().x - 20 - skin.width;
    setPlayerStatus(PlayerStatus.IDLE);
    playerOrientation = PlayerOrientation.SOUTH;
  }

  public void detectBoundTop() {
    skin.y = Gdx.graphics.getWidth() - 64;
    shotMeter.detectBoundTop(skin);
    setPlayerStatus(PlayerStatus.IDLE);
  }

  public void detectBoundBottom() {
    skin.y = 0;
    shotMeter.detectBoundBottom(skin);
    setPlayerStatus(PlayerStatus.IDLE);
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

  public void setPlayerStatus(PlayerStatus playerStatus) {
    if (playerStatus.sprites < this.playerStatus.sprites) {
      currentFrameIndex = 0;
    }
    this.playerStatus = playerStatus;
  }
}
