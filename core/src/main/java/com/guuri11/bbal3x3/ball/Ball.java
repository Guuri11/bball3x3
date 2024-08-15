package com.guuri11.bbal3x3.ball;

import static com.guuri11.bbal3x3.player.Player.PLAYER_HEIGHT;
import static com.guuri11.bbal3x3.player.Player.PLAYER_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.guuri11.bbal3x3.player.Player;
import com.guuri11.bbal3x3.player.PlayerOrientation;

public class Ball {
  public static final int BALL_WIDTH = 24;
  public static final int BALL_HEIGHT = 24;

  private final Rectangle skin;
  private final ShapeRenderer shape;
  private float velocityY;
  private float floorY = ((float) PLAYER_HEIGHT / 4); // ground height

  public Ball() {
    shape = new ShapeRenderer();

    skin = new Rectangle();
    skin.x = ((float) Gdx.graphics.getWidth() / 2 - (float) BALL_WIDTH / 2) - PLAYER_WIDTH + 54;
    skin.y = floorY;
    skin.width = BALL_WIDTH;
    skin.height = BALL_HEIGHT;

    velocityY = 300;
  }

  private static boolean isOrientedToEastSide(PlayerOrientation playerOrientation) {
    return playerOrientation == PlayerOrientation.EAST
        || playerOrientation == PlayerOrientation.SOUTH_EAST
        || playerOrientation == PlayerOrientation.NORTH_EAST;
  }

  public void render(final Matrix4 combined, final PlayerOrientation playerOrientation) {
    // Update velocity with gravity
    float gravity = -1500f;
    float bounceFactor = 0.9f;
    velocityY += gravity * Gdx.graphics.getDeltaTime();
    // Update ball position
    skin.y += velocityY * Gdx.graphics.getDeltaTime();

    // Check if bouncing
    if (skin.y <= floorY) {
      skin.y = floorY;
      velocityY = velocityY * bounceFactor; // Invert to apply bounce effect

      // Keep bouncing
      if (Math.abs(velocityY) < 150) {
        velocityY = 300 * bounceFactor; // Reset to keep bouncing
      }
    }

    // Draw
    shape.setProjectionMatrix(combined);
    shape.begin(ShapeRenderer.ShapeType.Filled);
    shape.setColor(Color.ORANGE);
    shape.ellipse(
        skin.x + (isOrientedToEastSide(playerOrientation) ? PLAYER_WIDTH : 0),
        skin.y,
        skin.width,
        skin.height);
    shape.end();
  }

  public void moveUp(final Player player) {
    skin.y += 200 * Gdx.graphics.getDeltaTime();
    floorY = player.getSkin().y;
  }

  public void moveDown(final Player player) {
    skin.y -= 200 * Gdx.graphics.getDeltaTime();
    floorY = player.getSkin().y;
  }

  public void moveLeft() {
    skin.x -= 200 * Gdx.graphics.getDeltaTime();
  }

  public void moveRight() {
    skin.x += 200 * Gdx.graphics.getDeltaTime();
  }

  public void moveLeftUp(final Player player) {
    moveLeft();
    moveUp(player);
  }

  public void moveLeftDown(final Player player) {
    moveLeft();
    moveDown(player);
  }

  public void moveRightUp(final Player player) {
    moveRight();
    moveUp(player);
  }

  public void moveRightDown(final Player player) {
    moveRight();
    moveDown(player);
  }

  public void detectBoundLeft() {
    skin.x = 0;
  }

  public void detectBoundRight(final Player player) {
    skin.x = player.getSkin().x - 54;
  }

  public void detectBoundTop(final Player player) {
    skin.y = Gdx.graphics.getHeight() - 64 + (player.getSkin().height / 4);
  }

  public void detectBoundBottom(final Player player) {
    skin.y = 0 + (player.getSkin().height / 4);
  }

  public Rectangle getSkin() {
    return skin;
  }
}
