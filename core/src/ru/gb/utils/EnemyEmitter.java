package ru.gb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.math.Rect;
import ru.gb.math.Rnd;
import ru.gb.pool.EnemyPool;
import ru.gb.sprite.EnemyShip;

public class EnemyEmitter {

    private static final float GENERATE_INTERVAL = 4f;

    private static final float ENEMY_SMALL_HEIGHT = 0.09f;
    private static final float ENEMY_SMALL_VY = -0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = ENEMY_SMALL_VY - 0.15f;
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final float ENEMY_SMALL_FIRE_RATE = 1f;
    private static final int ENEMY_SMALL_HP = 2;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.09f;
    private static final float ENEMY_MEDIUM_VY = -0.075f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.015f;
    private static final float ENEMY_MEDIUM_BULLET_VY = ENEMY_MEDIUM_VY - 0.1f;
    private static final int ENEMY_MEDIUM_DAMAGE = 2;
    private static final float ENEMY_MEDIUM_FIRE_RATE = 2f;
    private static final int ENEMY_MEDIUM_HP = 5;

    private static final float ENEMY_BIG_HEIGHT = 0.09f;
    private static final float ENEMY_BIG_VY = -0.05f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_BIG_BULLET_VY = ENEMY_BIG_VY - 0.1f;
    private static final int ENEMY_BIG_DAMAGE = 3;
    private static final float ENEMY_BIG_FIRE_RATE = 3f;
    private static final int ENEMY_BIG_HP = 10;
    private final Sound ENEMY_BIG_BULLET_SOUND = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));

    private int level = 1;

    private float generateTimer;

    private final TextureRegion[] enemySmallRegions;
    private final TextureRegion[] enemyMediumRegions;
    private final TextureRegion[] enemyBigRegions;

    private final Vector2 enemySmallV;
    private final Vector2 enemyMediumV;
    private final Vector2 enemyBigV;

    private final TextureRegion bulletRegion;

    private final Rect worldBounds;
    private final EnemyPool enemyPool;

    public EnemyEmitter(Rect worldBounds, EnemyPool enemyPool, TextureAtlas atlas) {
        this.worldBounds = worldBounds;
        this.enemyPool = enemyPool;
        enemySmallRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2, 2);
        enemyMediumRegions = Regions.split(atlas.findRegion("enemy1"), 1, 2, 2);
        enemyBigRegions = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);
        bulletRegion = atlas.findRegion("bulletEnemy");
        enemySmallV = new Vector2(0, ENEMY_SMALL_VY);
        enemyMediumV = new Vector2(0, ENEMY_MEDIUM_VY);
        enemyBigV = new Vector2(0, ENEMY_BIG_VY);
    }

    public int getLevel() {
        return level;
    }

    public void generate(float delta, int frags) {
        level = frags / 10 + 1;
        int deltaHeight = level / 1000;
        generateTimer += delta;
        if (generateTimer >= GENERATE_INTERVAL) {
            generateTimer = 0f;
            EnemyShip enemyShip = enemyPool.obtain();
            float type = (float) Math.random();
            if (type < 0.5f) {
                enemyShip.set(
                        enemySmallRegions,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_SOUND,
                        ENEMY_SMALL_BULLET_HEIGHT + deltaHeight,
                        ENEMY_SMALL_BULLET_VY,
                        ENEMY_SMALL_DAMAGE + level,
                        ENEMY_SMALL_FIRE_RATE,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP
                );
            } else if (type > 0.7f) {
                enemyShip.set(
                        enemyMediumRegions,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_SOUND,
                        ENEMY_MEDIUM_BULLET_HEIGHT + deltaHeight,
                        ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_DAMAGE + level,
                        ENEMY_MEDIUM_FIRE_RATE,
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP
                );
            } else {
                enemyShip.set(
                        enemyBigRegions,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_SOUND,
                        ENEMY_BIG_BULLET_HEIGHT + deltaHeight,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_DAMAGE + level,
                        ENEMY_BIG_FIRE_RATE,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP
                );
            }
            float enemyHalfWidth = enemyShip.getHalfWidth();
            enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyHalfWidth, worldBounds.getRight() - enemyHalfWidth);
            enemyShip.setBottom(worldBounds.getTop());
            enemyShip.setBulletPos(enemyShip.pos);
        }
    }

    public void dispose() {
        ENEMY_BIG_BULLET_SOUND.dispose();
    }
}