package com.guuri11.bbal3x3.court;

import static com.guuri11.bbal3x3.ConfigurationProperties.SCREEN_HEIGHT;
import static com.guuri11.bbal3x3.ConfigurationProperties.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.guuri11.bbal3x3.court.hoop.Hoop;

public class Court {
    public static final int COURT_SPRITE_X = 8;
    public static final int COURT_SPRITE_Y = 8;
    public static final int COURT_SPRITE_WIDTH = 640;
    public static final int COURT_SPRITE_HEIGHT = 192;
    public static final float COURT_SCALE = 2.5F;
    public static final float COURT_WIDTH = COURT_SPRITE_WIDTH * COURT_SCALE;
    public static final float COURT_X = (SCREEN_WIDTH - COURT_WIDTH) / 2;
    public static final float COURT_HEIGHT = COURT_SPRITE_HEIGHT * COURT_SCALE;
    public static final float COURT_Y = (SCREEN_HEIGHT - COURT_HEIGHT) / 2;


    Texture spriteBballCourt;
    TextureRegion court;
    Hoop leftHoop;
    Hoop rightHoop;

    public Court() {
        spriteBballCourt = new Texture((Gdx.files.internal("sprite-bball-court.png")));
        court = new TextureRegion(spriteBballCourt, COURT_SPRITE_X, COURT_SPRITE_Y, COURT_SPRITE_WIDTH, COURT_SPRITE_HEIGHT);
        leftHoop = new Hoop(spriteBballCourt, true);
        rightHoop = new Hoop(spriteBballCourt, false);
    }

    public void render(SpriteBatch batch) {
        batch.begin();

        batch.draw(court, COURT_X, COURT_Y, COURT_WIDTH, COURT_HEIGHT);
        leftHoop.render(batch);
        rightHoop.render(batch);

        batch.end();
    }

    public void dispose() {
        spriteBballCourt.dispose();
    }
}
