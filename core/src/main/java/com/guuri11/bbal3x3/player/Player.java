package com.guuri11.bbal3x3.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import static com.guuri11.bbal3x3.ball.Ball.BALL_WIDTH;

public class Player {
    public static final int PLAYER_WIDTH = 64;
    public static final int PLAYER_HEIGHT = 64;

    private final Rectangle skin;
    private final ShotMeter shotMeter;
    private final SpriteBatch spriteBatch;

    // Textures for each direction
    private final TextureRegion playerTextureLeft;
    private final TextureRegion playerTextureRight;
    private final TextureRegion playerTextureUp;
    private final TextureRegion playerTextureDown;
    private TextureRegion currentTexture;

    private PlayerOrientation playerOrientation;

    public Player() {
        spriteBatch = new SpriteBatch();
        playerOrientation = PlayerOrientation.WEST;

        // Load textures
        playerTextureLeft = new TextureRegion(new Texture(Gdx.files.internal("Player A/Player A Idle (no ball)/Player_A_Idle_West_NOBALL_strip4.png")), 0, 0, 16, 24);
        playerTextureRight = new TextureRegion(new Texture(Gdx.files.internal("Player A/Player A Idle (no ball)/Player_A_Idle_East_NOBALL_strip4.png")), 0, 0, 16, 24);
        playerTextureUp = new TextureRegion(new Texture(Gdx.files.internal("Player A/Player A Idle (no ball)/Player_A_Idle_North_NOBALL_strip4.png")), 0, 0, 16, 24);
        playerTextureDown = new TextureRegion(new Texture(Gdx.files.internal("Player A/Player A Idle (no ball)/Player_A_Idle_South_NOBALL_strip4.png")), 0, 0, 16, 24);

        // Set initial texture
        currentTexture = playerTextureDown;

        // Create a Rectangle to logically represent the player
        skin = new Rectangle();
        skin.x = (float) Gdx.graphics.getWidth() / 2 - (float) PLAYER_WIDTH / 4; // Center the player horizontally
        skin.y = 20; // Bottom left corner of the player is 20 pixels above the bottom screen edge
        skin.width = PLAYER_WIDTH;
        skin.height = PLAYER_HEIGHT;

        shotMeter = new ShotMeter(skin.height);
    }

    public void render(final Matrix4 combined) {
        spriteBatch.setProjectionMatrix(combined);
        spriteBatch.begin();
        spriteBatch.draw(currentTexture, skin.x, skin.y, skin.width, skin.height);
        spriteBatch.end();

        shotMeter.render(combined);
    }

    public void moveUp() {
        skin.y += 200 * Gdx.graphics.getDeltaTime();
        shotMeter.moveUp();
        playerOrientation = PlayerOrientation.NORTH;
        currentTexture = playerTextureUp; // Update texture to up direction
    }

    public void moveDown() {
        skin.y -= 200 * Gdx.graphics.getDeltaTime();
        shotMeter.moveDown();
        playerOrientation = PlayerOrientation.SOUTH;
        currentTexture = playerTextureDown; // Update texture to down direction
    }

    public void moveLeft() {
        skin.x -= 200 * Gdx.graphics.getDeltaTime();
        shotMeter.moveLeft();
        playerOrientation = PlayerOrientation.WEST;
        currentTexture = playerTextureLeft; // Update texture to left direction
    }

    public void moveRight() {
        skin.x += 200 * Gdx.graphics.getDeltaTime();
        shotMeter.moveRight();
        playerOrientation = PlayerOrientation.EAST;
        currentTexture = playerTextureRight; // Update texture to right direction
    }

    public void detectBoundLeft() {
        skin.x = BALL_WIDTH + 20;
        shotMeter.detectBoundLeft(skin);
    }

    public void detectBoundRight() {
        shotMeter.detectBoundRight();
        skin.x = shotMeter.getShotMeterSkin().x - 20 - skin.width;
    }

    public void detectBoundTop() {
        skin.y = Gdx.graphics.getWidth() - 64;
        shotMeter.detectBoundTop(skin);
    }

    public void detectBoundBottom() {
        skin.y = 0;
        shotMeter.detectBoundBottom(skin);
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
