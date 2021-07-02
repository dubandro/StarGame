package ru.gb.pool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.gb.base.SpritesPool;
import ru.gb.math.Rect;
import ru.gb.sprite.Bullet;
import ru.gb.sprite.ExtraPower;

public class ExtraPool extends SpritesPool<ExtraPower> {

    private final Rect worldBounds;

    public ExtraPool(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    protected ExtraPower newObject() {
        return new ExtraPower(worldBounds);
    }
}
