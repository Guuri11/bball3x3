package com.guuri11.bbal3x3.court.hoop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Net {
    public static final float NET_WIDTH = 16 * 2.5F;
    public static final float NET_HEIGHT = 16 * 2.5F;

    TextureRegion net;
    private boolean isLeftSide;

    public Net(final Texture spriteBballCourt, final boolean isLeftSide) {
        this.isLeftSide = isLeftSide;
        net = new TextureRegion(spriteBballCourt, 120,1195, 16, 14);
        if (!isLeftSide) {
            net.flip(true, false);
        }
    }

    public void render(SpriteBatch batch) {
        if (this.isLeftSide) {
            batch.draw(net, 400, 664 - (NET_HEIGHT/1.8F), NET_WIDTH, NET_HEIGHT);
        } else {
            batch.draw(net, 1485, 664 - (NET_HEIGHT/1.8F), NET_WIDTH, NET_HEIGHT);
        }
    }
}
