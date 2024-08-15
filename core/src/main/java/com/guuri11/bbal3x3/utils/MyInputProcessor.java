package com.guuri11.bbal3x3.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Util for drag and drop a item and see where to place it
 *
 * <pre>
 * public class MyGdxGame extends ApplicationAdapter {
 *     TextureRegion leftHoopRegion;
 *     MyInputProcessor inputProcessor;
 *
 *     public void create () {
 *         leftHoopRegion = new TextureRegion(texture, 810, 0, 100, 100);
 *
 *         inputProcessor = new MyInputProcessor(leftHoopRegion, 50, 300);
 *         Gdx.input.setInputProcessor(inputProcessor);
 *     }
 *
 *     public void render () {
 *         batch.begin();
 *         batch.draw(leftHoopRegion, inputProcessor.getRegionX(), inputProcessor.getRegionY());
 *         batch.end();
 *     }
 * }
 * </pre>
 */
public class MyInputProcessor implements InputProcessor {

  private TextureRegion region;
  private float regionX, regionY;
  private boolean dragging;

  public MyInputProcessor(TextureRegion region, float initialX, float initialY) {
    this.region = region;
    this.regionX = initialX;
    this.regionY = initialY;
    this.dragging = false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    // Convertir la coordenada de pantalla a coordenada del mundo
    float worldY = Gdx.graphics.getHeight() - screenY;

    // Verificar si el clic está dentro de la región
    if ((float) screenX >= regionX
        && (float) screenX <= regionX + region.getRegionWidth()
        && worldY >= regionY
        && worldY <= regionY + region.getRegionHeight()) {
      dragging = true;
    }
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    dragging = false;
    return true;
  }

  @Override
  public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (dragging) {
      // Actualizar posición
      regionX = screenX - region.getRegionWidth() / 2f;
      regionY = Gdx.graphics.getHeight() - screenY - region.getRegionHeight() / 2f;
      System.out.println(regionX);
      System.out.println(regionY);
    }
    return true;
  }

  // Métodos vacíos necesarios para implementar InputProcessor
  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }

  public float getRegionX() {
    return regionX;
  }

  public float getRegionY() {
    return regionY;
  }
}
