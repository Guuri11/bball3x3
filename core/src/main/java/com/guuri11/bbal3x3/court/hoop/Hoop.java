package com.guuri11.bbal3x3.court.hoop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.guuri11.bbal3x3.ConfigurationProperties.SCREEN_WIDTH;

public class Hoop {
  public static final float HOOP_WIDTH = 105 * 2.5F;
  public static final float HOOP_HEIGHT = 105 * 2.5F;

  TextureRegion hoop;
  Net net;
  private boolean isLeftSide;

  public Hoop(final Texture spriteBballCourt, final boolean isLeftSide) {
    this.isLeftSide = isLeftSide;
    hoop = new TextureRegion(spriteBballCourt, 8, 1103, 105, 105);
    if (!isLeftSide) {
      hoop.flip(true, false);
      net = new Net(spriteBballCourt, false);
    } else {
      net = new Net(spriteBballCourt, true);
    }
  }

  public void render(SpriteBatch batch) {
    if (this.isLeftSide) {
      batch.draw(hoop, 180, 500, HOOP_WIDTH, HOOP_HEIGHT);
    } else {
      batch.draw(
          hoop, SCREEN_WIDTH - HOOP_WIDTH - (HOOP_WIDTH / 1.5F), 500, HOOP_WIDTH, HOOP_HEIGHT);
    }
    net.render(batch);
  }
}
