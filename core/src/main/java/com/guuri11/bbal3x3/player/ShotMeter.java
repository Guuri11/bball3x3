package com.guuri11.bbal3x3.player;

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
    private Texture shotMeterTexture;
    private float indicatorOffset = 0;
    private float indicatorDirection = 50; // Dirección de movimiento del indicador
    private final float maxOffset;
    private boolean shooting = false;

    public ShotMeter(final float playerHeight) {
        shotMeterSpriteBatch = new SpriteBatch();

        shotMeterSkin = new Rectangle();
        shotMeterSkin.x = ((float) Gdx.graphics.getWidth() / 2 - (float) Player.PLAYER_WIDTH / 4) + Player.PLAYER_WIDTH + 20; // centrar el medidor horizontalmente
        shotMeterSkin.y = 14 + (playerHeight / 4); // la esquina inferior izquierda del medidor está 20 píxeles por encima del borde inferior de la pantalla
        shotMeterSkin.width = 12;
        shotMeterSkin.height = 48;
        generateShotMeterTexture();
        float indicatorHeight = 2;
        maxOffset = shotMeterSkin.height - indicatorHeight;
    }

    public void render(final Matrix4 combined) {
        if (shooting) {
            shotMeterSpriteBatch.setProjectionMatrix(combined);
            shotMeterSpriteBatch.begin();
            shotMeterSpriteBatch.draw(shotMeterTexture, shotMeterSkin.x, shotMeterSkin.y);

            // Renderizar el indicador de tiro
            float indicatorSpeed = 200.0f; // ajusta la velocidad según sea necesario
            indicatorOffset += indicatorSpeed * Gdx.graphics.getDeltaTime() * (indicatorDirection / 50.0f);

            // Cambiar dirección cuando el indicador alcanza los límites
            if (indicatorOffset >= maxOffset) {
                indicatorOffset = maxOffset;
                indicatorDirection = -50; // Cambiar dirección para bajar
            } else if (indicatorOffset <= 0) {
                indicatorOffset = 0;
                indicatorDirection = 50; // Cambiar dirección para subir
            }

            // Dibujar el indicador
            shotMeterSpriteBatch.end();

            // Usar ShapeRenderer para dibujar el indicador como una barra blanca
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            float indicatorY = shotMeterSkin.y + indicatorOffset;
            shapeRenderer.rect(shotMeterSkin.x - 2, indicatorY - 1, shotMeterSkin.width + 4, 2);
            shapeRenderer.end();
            shapeRenderer.dispose();
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

        // Degradado de negro a verde del medidor de tiro
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
        shotMeterSpriteBatch.dispose();
        if (shotMeterTexture != null) {
            shotMeterTexture.dispose();
        }
    }

    public Rectangle getShotMeterSkin() {
        return shotMeterSkin;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
}
