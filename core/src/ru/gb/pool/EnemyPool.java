package ru.gb.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.base.SpritesPool;
import ru.gb.math.Rect;
import ru.gb.sprite.Enemy;

public class EnemyPool extends SpritesPool<Enemy> {

    private TextureAtlas atlas;
    private Rect worldBounds;

    public EnemyPool(TextureAtlas atlas, Rect worldBounds) {
        this.atlas = atlas;
        this.worldBounds = worldBounds;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(atlas, worldBounds);
    }
}
