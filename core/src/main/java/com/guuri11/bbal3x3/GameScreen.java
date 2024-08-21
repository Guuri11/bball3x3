package com.guuri11.bbal3x3;

import static com.guuri11.bbal3x3.ConfigurationProperties.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.guuri11.bbal3x3.court.Court;
import com.guuri11.bbal3x3.player.Player;
import com.guuri11.bbal3x3.player.Team;
import com.guuri11.bbal3x3.utils.MyInputProcessor;

public class GameScreen implements Screen {
  final Bball3x3 game;

  OrthographicCamera camera;

  Player player;
  Court court;
  MyInputProcessor inputProcessor;

  public GameScreen(final Bball3x3 game) {
    this.game = game;
    // ball = new Ball();
    player = new Player(Team.B);
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

    game.batch.setProjectionMatrix(camera.combined);

    // DETECT IF PLAYER IS SHOOTING
    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
      player.jump();
      return;
    }
    if (!Gdx.input.isKeyPressed(Keys.SPACE) && player.getShotMeter().isShooting()) {
      player.getShotMeter().setShooting(false);
    }

    // DETECT IF PLAYER IS MOVING UP, LEFT, RIGHT, DOWN OR DIAGONAL DIRECTIONS
    if (Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.UP)) {
      player.moveLeftUp();
    } else if (Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.DOWN)) {
      player.moveLeftDown();
    } else if (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.UP)) {
      player.moveRightUp();
    } else if (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.DOWN)) {
      player.moveRightDown();
    } else {
      if (Gdx.input.isKeyPressed(Keys.LEFT)) {
        player.moveLeft();
      }
      if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
        player.moveRight();
      }
      if (Gdx.input.isKeyPressed(Keys.UP)) {
        player.moveUp();
      }
      if (Gdx.input.isKeyPressed(Keys.DOWN)) {
        player.moveDown();
      }
    }

    // DETECT GAME BOUNDS
    if (player.getSkin().x < 0) {
      player.detectBoundLeft();
    }
    if (player.getShotMeter().getShotMeterSkin().x
        > SCREEN_WIDTH - player.getShotMeter().getShotMeterSkin().width) {
      player.detectBoundRight();
    }
    if (player.getSkin().y < 0) {
      player.detectBoundBottom();
    }
    if (player.getSkin().y > SCREEN_HEIGHT - 64) {
      player.detectBoundTop();
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
