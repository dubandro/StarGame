package ru.gb.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.Sprite;
import ru.gb.math.Rect;
import ru.gb.math.Rnd;

public class Enemy extends Sprite {

    private static final float HEIGHT = 0.15f;

    private Rect worldBounds;
    private Vector2 v;

    public Enemy(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion("enemy0"), 1, 2, 2);
        this.worldBounds = worldBounds;
        resize();
    }

    private void resize() {
        setHeightProportion(HEIGHT);
        setAngle(180);
        set();
    }

    public void set() {
        v = new Vector2(0, -0.1f);
        setTop(worldBounds.getTop() + HEIGHT);
        setLeft(Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight() - this.getWidth()));
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }
}
