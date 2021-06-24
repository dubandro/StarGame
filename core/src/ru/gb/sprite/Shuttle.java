package ru.gb.sprite;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.Ship;
import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.pool.ExplosionPool;

public class Shuttle extends Ship {

    private static final float HEIGHT = 0.1f;
    private static final float PADDING = 0.05f;
    private static final int INVALID_POINTER = -1;
    private static final float FIRE_RATE = 0.2f;
    private static final float VELOCITY = 0.15f;

    private boolean pressedLeft;
    private boolean pressedRight;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
    private boolean enemyDetected;

    public void setEnemyDetected(boolean enemyDetected) {
        this.enemyDetected = enemyDetected;
    }

    public Shuttle(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        rateOfFire = FIRE_RATE;
        bulletHeight = 0.01f;
        damage = 1;
        hp = 1;
        setV = new Vector2(0.5f, 0);
        shipV = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    public void dispose() {
        bulletSound.dispose();
    }

    @Override
    protected void shoot() {
        if (enemyDetected) {
            super.shoot();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        // если нужно реагировать на нажатие правой или левой половины экрана, то worldBounds.pos.x
        // но логичнее отталкиваться от стороны корабля
        if (touch.x < this.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                super.shoot(); // TODO решить что делать с одиночными выстрелами
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    private void moveRight() {
        shipV.set(setV).nor().scl(VELOCITY);
    }

    private void moveLeft() {
        shipV.set(setV).rotateDeg(180).nor().scl(VELOCITY);
    }

    private void stop() {
        shipV.setZero();
    }
}