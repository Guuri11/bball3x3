package com.guuri11.bbal3x3;

import static com.guuri11.bbal3x3.ConfigurationProperties.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.guuri11.bbal3x3.ball.Ball;
import com.guuri11.bbal3x3.court.Court;
import com.guuri11.bbal3x3.player.Player;
import com.guuri11.bbal3x3.utils.MyInputProcessor;

public class GameScreen implements Screen {
  final Bball3x3 game;

  OrthographicCamera camera;

  Player player;
  Ball ball;
  Court court;
  MyInputProcessor inputProcessor;

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

    // DETECT IF PLAYER IS MOVING UP, LEFT, RIGHT, DOWN OR DIAGONAL DIRECTIONS
    if (Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.UP)) {
      player.moveLeftUp();
      ball.moveLeftUp(player);
    } else if (Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.DOWN)) {
      player.moveLeftDown();
      ball.moveLeftDown(player);
    } else if (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.UP)) {
      player.moveRightUp();
      ball.moveRightUp(player);
    } else if (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.DOWN)) {
      player.moveRightDown();
      ball.moveRightDown(player);
    } else {
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
    }

    // DETECT IF PLAYER IS SHOOTING
    player.getShotMeter().setShooting(Gdx.input.isKeyPressed(Keys.SPACE));

    // DETECT GAME BOUNDS
    if (player.getSkin().x < 0 + ball.getSkin().width + 20) {
      player.detectBoundLeft();
      ball.detectBoundLeft();
    }
    if (player.getShotMeter().getShotMeterSkin().x
        > SCREEN_WIDTH - player.getShotMeter().getShotMeterSkin().width) {
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
  public void resize(int width, int height) {}

  @Override
  public void show() {
    // when the screen is shown
  }

  @Override
  public void hide() {}

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void dispose() {
    court.dispose();
    player.dispose();
  }
}
