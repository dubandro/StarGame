package ru.gb.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class TrackingStar extends Star {

    private final Vector2 trackingV;
    private final Vector2 sumV = new Vector2();

    public TrackingStar(TextureAtlas atlas, Vector2 trackingV) {
        super(atlas);
        this.trackingV = trackingV;
    }

    @Override
    public void update(float delta) {
//        float starRelative = (float) (Math.pow(starHeight, 3) * 100000);
        float starRelative = (float) (Math.pow(starHeight * 100, 3) * 1);
        sumV.setZero().mulAdd(trackingV, starRelative).rotateDeg(180).add(v);
        pos.mulAdd(sumV, delta);
        checkBounds();
        animate();
    }
}
