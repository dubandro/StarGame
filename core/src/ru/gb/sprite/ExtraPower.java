package ru.gb.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.Sprite;
import ru.gb.math.Rect;

public class ExtraPower extends Sprite {

    private final float EXTRA_HEIGHT = 0.05f;
    private final Rect worldBounds;
    private Vector2 v;
    private int hp;
    private int damage;

    public ExtraPower(Rect worldBounds) {
        this.worldBounds = worldBounds;
        regions = new TextureRegion[1];
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        this.setAngle(getAngle() + 3);
        if (worldBounds.isOutside(this)) {
            destroy();
        }
    }

    public void set(Vector2 pos, TextureRegion region, int hp, int damage) {
        this.pos.set(pos);
        this.regions[0] = region;
        this.hp = hp;
        this.damage = damage;
        v = new Vector2(0, -0.05f);
        setHeightProportion(EXTRA_HEIGHT);
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }
}