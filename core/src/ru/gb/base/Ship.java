package ru.gb.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.sprite.Bullet;

public class Ship extends Sprite {

    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected Sound bulletSound;
    protected float bulletHeight;
    protected float rateOfFire;
    protected int damage;
    protected int hp;

    protected Vector2 setV;
    protected Vector2 shipV;

    protected float timer;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(shipV, delta);
        timer += delta;
        if (timer >= rateOfFire) {
            timer = 0;
            shoot();
        }
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, worldBounds, damage, bulletHeight);
        bulletSound.play();
    }
}
