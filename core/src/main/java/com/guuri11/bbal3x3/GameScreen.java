package com.guuri11.bbal3x3;

import static com.guuri11.bbal3x3.ConfigurationProperties.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.guuri11.bbal3x3.Team.Team;
import com.guuri11.bbal3x3.Team.TeamName;
import com.guuri11.bbal3x3.Team.player.Player;
import com.guuri11.bbal3x3.Team.player.PlayerStatus;
import com.guuri11.bbal3x3.court.Court;
import com.guuri11.bbal3x3.utils.MyInputProcessor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameScreen implements Screen {
  final Bball3x3 game;

  OrthographicCamera camera;

  Team teamA;
  Team teamB;
  Court court;
  MyInputProcessor inputProcessor;

  public GameScreen(final Bball3x3 game) {
    this.game = game;
    // ball = new Ball();
    teamA = new Team(TeamName.A);
    teamA.loadPlayers();
    teamA.getPlayers().get(0).setHasTheBall(true);
    teamA.getPlayers().get(0).setPlayerStatus(PlayerStatus.IDLE_WITH_BALL);
    teamB = new Team(TeamName.B);
    teamB.loadPlayers();
    court = new Court();

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

    List<Player> players = new ArrayList<>(teamA.getPlayers().values());
    players.addAll(teamB.getPlayers().values());
    players.stream()
        .sorted(Comparator.comparingDouble(player -> -player.getSkin().y))
        .forEach(player -> player.render(camera.combined));

    game.batch.setProjectionMatrix(camera.combined);

    // DETECT IF PLAYER IS SHOOTING
    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
      teamA.getPlayers().get(0).jump();
      return;
    }
    if (!Gdx.input.isKeyPressed(Keys.SPACE)
        && teamA.getPlayers().get(0).getShotMeter().isShooting()) {
      teamA.getPlayers().get(0).getShotMeter().setShooting(false);
    }

    // DETECT IF PLAYER IS MOVING UP, LEFT, RIGHT, DOWN OR DIAGONAL DIRECTIONS
    if (Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.UP)) {
      teamA.getPlayers().get(0).moveLeftUp();
    } else if (Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.DOWN)) {
      teamA.getPlayers().get(0).moveLeftDown();
    } else if (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.UP)) {
      teamA.getPlayers().get(0).moveRightUp();
    } else if (Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.DOWN)) {
      teamA.getPlayers().get(0).moveRightDown();
    } else {
      if (Gdx.input.isKeyPressed(Keys.LEFT)) {
        teamA.getPlayers().get(0).moveLeft();
      }
      if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
        teamA.getPlayers().get(0).moveRight();
      }
      if (Gdx.input.isKeyPressed(Keys.UP)) {
        teamA.getPlayers().get(0).moveUp();
      }
      if (Gdx.input.isKeyPressed(Keys.DOWN)) {
        teamA.getPlayers().get(0).moveDown();
      }
    }

    // DETECT GAME BOUNDS
    if (teamA.getPlayers().get(0).getSkin().x < 0) {
      teamA.getPlayers().get(0).detectBoundLeft();
    }
    if (teamA.getPlayers().get(0).getShotMeter().getShotMeterSkin().x
        > SCREEN_WIDTH - teamA.getPlayers().get(0).getShotMeter().getShotMeterSkin().width) {
      teamA.getPlayers().get(0).detectBoundRight();
    }
    if (teamA.getPlayers().get(0).getSkin().y < 0) {
      teamA.getPlayers().get(0).detectBoundBottom();
    }
    if (teamA.getPlayers().get(0).getSkin().y > SCREEN_HEIGHT - 64) {
      teamA.getPlayers().get(0).detectBoundTop();
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
    teamB.getPlayers().forEach((integer, player) -> player.dispose());
    teamA.getPlayers().forEach((integer, player) -> player.dispose());
  }
}
