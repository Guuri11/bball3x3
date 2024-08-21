package com.guuri11.bbal3x3.Team.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.guuri11.bbal3x3.Team.TeamName;

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
  private boolean isJumping;
  private float velocityY;
  private float floorY;
  private boolean reachedJumpLimit = false;

  private TeamName team;
  private boolean hasTheBall;

  public Player(
      final TeamName team,
      final float x,
      final float y,
      final PlayerOrientation orientation,
      final PlayerStatus status,
      final boolean hasTheBall) {
    this.team = team;
    spriteBatch = new SpriteBatch();
    playerOrientation = orientation;
    this.hasTheBall = hasTheBall;
    playerStatus = status;
    currentTexture =
        new TextureRegion(
            new Texture(
                Gdx.files.internal(
                    String.format(
                        "Player %s/Player %s %s/Player_%s_%s_%s.png",
                        team,
                        team,
                        playerStatus.value,
                        team,
                        playerStatus.value,
                        playerOrientation.value))),
            0,
            0,
            playerStatus.frameWidth,
            playerStatus.spriteHeight);

    // Create a Rectangle to logically represent the player
    skin = new Rectangle();
    skin.x = x;
    skin.y = y;
    skin.width = PLAYER_WIDTH;
    skin.height = PLAYER_HEIGHT;

    velocityY = 0;
    isJumping = false;
    shotMeter = new ShotMeter(this);
  }

  private void setTexture() {
    stateTime += Gdx.graphics.getDeltaTime();

    if (stateTime >= frameDuration) {
      stateTime = 0f;
      currentFrameIndex = (currentFrameIndex + 1) % playerStatus.sprites;
    }

    // Dispose the previous texture if any
    if (currentTexture.getTexture() != null) {
      currentTexture.getTexture().dispose();
    }

    String texturePath =
        String.format(
            "Player %s/Player %s %s/Player_%s_%s_%s.png",
            team, team, playerStatus.value, team, playerStatus.value, playerOrientation.value);

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

  public void jump() {
    if (hasTheBall) {
      jumpShot();
    } else {
      standShot();
    }
  }

  private void jumpShot() {
    if (!isJumping) {
      isJumping = true;
      frameDuration = 0.15f;
      floorY = skin.y;
      velocityY = 300;
      setPlayerStatus(PlayerStatus.JUMP_SHOT_WITH_BALL);
      this.shotMeter.setShooting(true);
    }
  }

  private void standShot() {
    if (!isJumping) {
      isJumping = true;
      frameDuration = 0.15f;
      floorY = skin.y;
      velocityY = 300;
      setPlayerStatus(PlayerStatus.STAND_SHOOT);
    }
  }

  public void updateJump() {
    if (!isJumping) {
      return;
    }

    float jumpHeightLimit = 1500f;
    float gravity = -1500f;
    float deltaTime = Gdx.graphics.getDeltaTime();

    if (!reachedJumpLimit && skin.y < floorY + jumpHeightLimit) {
      // Continue jumping upwards
      velocityY += gravity * deltaTime;
    } else {
      // Start falling after reaching the jump limit
      reachedJumpLimit = true;
      velocityY += gravity * deltaTime;
    }

    skin.y += velocityY * deltaTime;

    // Check if the player touched the ground
    if (skin.y <= floorY) {
      skin.y = floorY; // Prevent player from going through the ground
      resetJump();
    }
  }

  private void resetJump() {
    isJumping = false;
    velocityY = 0;
    if (hasTheBall) {
      hasTheBall = false;
      shotMeter.setShooting(false);
    }
    setPlayerStatus(PlayerStatus.IDLE);
    frameDuration = 0.5f;
  }

  public void moveUp() {
    skin.y += 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveUp();
    playerOrientation = PlayerOrientation.NORTH;
    setPlayerStatus(hasTheBall ? PlayerStatus.DRIBBLE : PlayerStatus.RUN);
  }

  public void moveDown() {
    if (!isJumping) {
      skin.y -= 200 * Gdx.graphics.getDeltaTime();
      shotMeter.moveDown();
      playerOrientation = PlayerOrientation.SOUTH;
      setPlayerStatus(hasTheBall ? PlayerStatus.DRIBBLE : PlayerStatus.RUN);
    }
  }

  public void moveLeft() {
    skin.x -= 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveLeft();
    setPlayerStatus(hasTheBall ? PlayerStatus.DRIBBLE : PlayerStatus.RUN);
    playerOrientation = PlayerOrientation.WEST;
  }

  public void moveRight() {
    skin.x += 200 * Gdx.graphics.getDeltaTime();
    shotMeter.moveRight();
    setPlayerStatus(hasTheBall ? PlayerStatus.DRIBBLE : PlayerStatus.RUN);
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
    setPlayerStatus(hasTheBall ? PlayerStatus.IDLE_WITH_BALL : PlayerStatus.IDLE);
    playerOrientation = PlayerOrientation.SOUTH;
  }

  public void detectBoundRight() {
    shotMeter.detectBoundRight();
    skin.x = shotMeter.getShotMeterSkin().x - 20 - skin.width;
    setPlayerStatus(hasTheBall ? PlayerStatus.IDLE_WITH_BALL : PlayerStatus.IDLE);
    playerOrientation = PlayerOrientation.SOUTH;
  }

  public void detectBoundTop() {
    skin.y = Gdx.graphics.getWidth() - 64;
    shotMeter.detectBoundTop(skin);
    setPlayerStatus(hasTheBall ? PlayerStatus.IDLE_WITH_BALL : PlayerStatus.IDLE);
  }

  public void detectBoundBottom() {
    skin.y = 0;
    shotMeter.detectBoundBottom(skin);
    setPlayerStatus(hasTheBall ? PlayerStatus.IDLE_WITH_BALL : PlayerStatus.IDLE);
  }

  public void dispose() {
    shotMeter.dispose();
    spriteBatch.dispose();
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

  public void setHasTheBall(boolean hasTheBall) {
    this.hasTheBall = hasTheBall;
  }
}
