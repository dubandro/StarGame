package ru.gb.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.Ship;
import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.pool.ExplosionPool;
import ru.gb.pool.ExtraPool;

public class EnemyShip extends Ship {

    private final float START_VY = -0.3f;

    public EnemyShip(Rect worldBounds, BulletPool bulletPool, ExplosionPool explosionPool) {
        this.worldBounds = worldBounds;
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        setV = new Vector2();
        shipV = new Vector2();
        this.bulletV = new Vector2();
        this.bulletPos = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y - getHalfHeight());
        if (getTop() < worldBounds.getTop()) {
            shipV.set(setV);
            toFire = true;
        } else {
            timer = rateOfFire - delta * 10;
            toFire = false;
        }
        if (worldBounds.isOutside(this)) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 enemyEmitterV,
            TextureRegion bulletRegion,
            Sound bulletSound,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            float height,
            int hp
    ) {
        this.regions = regions;
        this.setV.set(enemyEmitterV);
        this.bulletRegion = bulletRegion;
        this.bulletSound = bulletSound;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.rateOfFire = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        shipV.set(0, START_VY);
    }
}
