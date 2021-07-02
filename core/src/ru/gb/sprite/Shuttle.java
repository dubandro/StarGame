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

    private static final float HEIGHT = 0.12f;
    private static final float PADDING = 0.03f;
    private static final int INVALID_POINTER = -1;
    private static final float FIRE_RATE = 0.2f;
    private static final float BULLET_HEIGHT = 0.008f;
    private static final float X_VELOCITY = 0.15f;
    private static final int SHUTTLE_HP = 10;
    private static final int SHUTTLE_DAMAGE = 1;

    private boolean pressedLeft;
    private boolean pressedRight;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private int countShoot;

    public Shuttle(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.damage = SHUTTLE_DAMAGE;
        this.hp = SHUTTLE_HP;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        rateOfFire = FIRE_RATE;
        bulletHeight = BULLET_HEIGHT;

        setV = new Vector2(0.5f, 0);
        shipV = new Vector2();
    }

    public void startNewGame() {
        this.hp = SHUTTLE_HP;
        this.damage = SHUTTLE_DAMAGE;
        this.bulletHeight = BULLET_HEIGHT;
        this.pos.x = worldBounds.pos.x;
        damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;
        stop();
        pressedLeft = false;
        pressedRight = false;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        toFire = false;
        flushDestroy();
    }

    public void setDamage(int damage) {
        if (damage != 0) {
            this.damage = damage;
            this.bulletHeight = BULLET_HEIGHT * damage;
            countShoot = 0;
            frame = 1;
            damageAnimateTimer = 0f;
        }
    }

    @Override
    protected void shoot() {
        super.shoot();
        if (damage != SHUTTLE_DAMAGE) {
            countShoot ++;
            if (countShoot > 10) {
                setDamage(SHUTTLE_DAMAGE);
            }
        }
    }

    public void setHp(int hp) {
        this.hp += hp;
        frame = 1;
        damageAnimateTimer = 0f;
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
                if (!toFire) {
                    super.shoot();
                }
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
        shipV.set(setV).nor().scl(X_VELOCITY);
    }

    private void moveLeft() {
        shipV.set(setV).rotateDeg(180).nor().scl(X_VELOCITY);
    }

    private void stop() {
        shipV.setZero();
    }
}