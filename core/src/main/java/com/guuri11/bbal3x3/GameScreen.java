package com.guuri11.bbal3x3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.guuri11.bbal3x3.ball.Ball;
import com.guuri11.bbal3x3.court.Court;
import com.guuri11.bbal3x3.player.Player;
import com.guuri11.bbal3x3.utils.MyInputProcessor;

import static com.guuri11.bbal3x3.ConfigurationProperties.*;

public class GameScreen implements Screen {
    final Bball3x3 game;

    OrthographicCamera camera;

    Player player;
    Ball ball;
    Court court;
    MyInputProcessor inputProcessor;

    Array<Rectangle> raindrops;

    public GameScreen(final Bball3x3 game) {
        this.game = game;
        ball = new Ball();
        player = new Player();
        court = new Court();
        // inputProcessor = new MyInputProcessor(hoopNetRight, 50, 300);
        // Gdx.input.setInputProcessor(inputProcessor);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();


        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Rectangle>();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND_COLOR);

        // tell the camera to update its matrices.
        camera.update();
        court.render(game.batch);
        player.render(camera.combined);
        ball.render(camera.combined, player.getCurrentOrientation());

        game.batch.setProjectionMatrix(camera.combined);

        // process user input
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            player.moveLeft();
            ball.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            player.moveRight();
            ball.moveRight();
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            player.moveUp();
            ball.moveUp(player);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            player.moveDown();
            ball.moveDown(player);
        }

        player.getShotMeter().setShooting(Gdx.input.isKeyPressed(Keys.SPACE));

        if (player.getSkin().x < 0 + ball.getSkin().width + 20) {
            player.detectBoundLeft();
            ball.detectBoundLeft();
        }
        if (player.getShotMeter().getShotMeterSkin().x > SCREEN_WIDTH - player.getShotMeter().getShotMeterSkin().width) {
            player.detectBoundRight();
            ball.detectBoundRight(player);
        }
        if (player.getSkin().y < 0) {
            player.detectBoundBottom();
            ball.detectBoundBottom(player);
        }
        if (player.getSkin().y > SCREEN_HEIGHT - 64) {
            player.detectBoundTop();
            ball.detectBoundTop(player);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // when the screen is shown
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        court.dispose();
        player.dispose();
    }

}
