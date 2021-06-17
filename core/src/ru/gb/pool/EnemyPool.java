package ru.gb.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.base.SpritesPool;
import ru.gb.math.Rect;
import ru.gb.sprite.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {

    private Rect worldBounds;
    private final BulletPool bulletPool;

    public EnemyPool(Rect worldBounds, BulletPool bulletPool) {
        this.worldBounds = worldBounds;
        this.bulletPool = bulletPool;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(worldBounds, bulletPool);
    }
}
