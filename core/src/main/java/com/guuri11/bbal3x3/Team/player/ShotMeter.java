package com.guuri11.bbal3x3.Team.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

public class ShotMeter {
  private final Rectangle shotMeterSkin;
  private final SpriteBatch shotMeterSpriteBatch;
  private final float maxOffset;
  private final ShapeRenderer shapeRenderer;
  private Texture shotMeterTexture;
  private float indicatorOffset = 0;
  private float indicatorDirection = 50;
  private boolean shooting = false;

  public ShotMeter(final Player player) {
    shotMeterSpriteBatch = new SpriteBatch();
    shapeRenderer = new ShapeRenderer();

    shotMeterSkin = new Rectangle();
    shotMeterSkin.x = player.getSkin().x + player.getSkin().width + 20;
    shotMeterSkin.y = player.getSkin().y + (player.getSkin().height / 4);
    shotMeterSkin.width = 12;
    shotMeterSkin.height = 48;
    generateShotMeterTexture();
    float indicatorHeight = 2;
    maxOffset = shotMeterSkin.height - indicatorHeight;
  }

  public boolean isShooting() {
    return shooting;
  }

  public void setShooting(boolean shooting) {
    this.shooting = shooting;
  }

  public void render(final Matrix4 combined) {
    if (shooting) {
      shotMeterSpriteBatch.setProjectionMatrix(combined);
      shotMeterSpriteBatch.begin();
      shotMeterSpriteBatch.draw(shotMeterTexture, shotMeterSkin.x, shotMeterSkin.y);

      // Render shot indicator
      float indicatorSpeed = 200.0f; // ajusta la velocidad según sea necesario
      indicatorOffset +=
          indicatorSpeed * Gdx.graphics.getDeltaTime() * (indicatorDirection / 50.0f);

      // Change direction on arrive to limit
      if (indicatorOffset >= maxOffset) {
        indicatorOffset = maxOffset;
        indicatorDirection = -50; // Change direction to go down
      } else if (indicatorOffset <= 0) {
        indicatorOffset = 0;
        indicatorDirection = 50; // Change direction to go up
      }

      // Draw indicator
      shotMeterSpriteBatch.end();

      // Use ShapeRenderer to draw the indicator with a white stick
      shapeRenderer.setProjectionMatrix(combined);
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      shapeRenderer.setColor(Color.WHITE);
      float indicatorY = shotMeterSkin.y + indicatorOffset;
      shapeRenderer.rect(shotMeterSkin.x - 2, indicatorY - 1, shotMeterSkin.width + 4, 2);
      shapeRenderer.end();
    } else {
      indicatorOffset = 0;
    }
  }

  private void generateShotMeterTexture() {
    int width = (int) shotMeterSkin.width;
    int height = (int) shotMeterSkin.height;
    Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

    float greenHeightPercentage = 0.10f;
    int greenHeight = (int) (height * greenHeightPercentage);

    // Black Green fade
    for (int y = 0; y < height; y++) {
      Color color;
      if (y < greenHeight) {
        float progress = (float) y / greenHeight;
        color = new Color(Color.GREEN);
        color.lerp(Color.BLACK, progress);
      } else {
        color = Color.BLACK;
      }
      pixmap.setColor(color);
      pixmap.drawLine(0, y, width, y);
    }

    shotMeterTexture = new Texture(pixmap);
    pixmap.dispose();
  }

  public void moveUp() {
    shotMeterSkin.y += 200 * Gdx.graphics.getDeltaTime();
  }

  public void moveDown() {
    shotMeterSkin.y -= 200 * Gdx.graphics.getDeltaTime();
  }

  public void moveLeft() {
    shotMeterSkin.x -= 200 * Gdx.graphics.getDeltaTime();
  }

  public void moveRight() {
    shotMeterSkin.x += 200 * Gdx.graphics.getDeltaTime();
  }

  public void detectBoundLeft(final Rectangle playerSkin) {
    shotMeterSkin.x = playerSkin.x + playerSkin.width + 20;
  }

  public void detectBoundRight() {
    shotMeterSkin.x = Gdx.graphics.getWidth() - shotMeterSkin.width;
  }

  public void detectBoundTop(final Rectangle playerSkin) {
    shotMeterSkin.y = Gdx.graphics.getHeight() - Player.PLAYER_HEIGHT + (playerSkin.height / 4) - 7;
  }

  public void detectBoundBottom(final Rectangle playerSkin) {
    shotMeterSkin.y = 0 + (playerSkin.height / 4) - 7;
  }

  public void dispose() {
    shapeRenderer.dispose();
    shotMeterSpriteBatch.dispose();
    if (shotMeterTexture != null) {
      shotMeterTexture.dispose();
    }
  }

  public Rectangle getShotMeterSkin() {
    return shotMeterSkin;
  }
}
