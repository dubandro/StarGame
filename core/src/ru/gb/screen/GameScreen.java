package ru.gb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

import ru.gb.base.BaseScreen;
import ru.gb.base.Font;
import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.pool.EnemyPool;
import ru.gb.pool.ExplosionPool;
import ru.gb.pool.ExtraPool;
import ru.gb.sprite.Background;
import ru.gb.sprite.Bullet;
import ru.gb.sprite.ButtonNewGame;
import ru.gb.sprite.EnemyShip;
import ru.gb.sprite.GameOver;
import ru.gb.sprite.ExtraPower;
import ru.gb.sprite.Shuttle;
import ru.gb.sprite.Star;
import ru.gb.sprite.TrackingStar;
import ru.gb.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 400;
    private static final float FONT_SIZE = 0.02f;
    private static final float PADDING = 0.01f;

    private static final String FRAGS = "Frags:";
    private static final String HP = "HP:";
    private static final String LEVEL = "Level:";

    private enum State {PLAYING, GAME_OVER}

    private Texture bg;
    private Texture hp;
    private Texture dm;
    private Background background;
    private TextureAtlas atlas;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private ExtraPool extraPool;
    private EnemyEmitter enemyEmitter;
    private Shuttle shuttle;
    private Star[] stars;
    private GameOver gameOver;
    private ButtonNewGame newGame;

    private Sound explosionSound;

    private State state;
    private boolean newGameDraw;
    private int frags;

    private Font font;
    private Font font_hp;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/background.png");
        background = new Background(bg);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        gameOver = new GameOver(atlas);
        newGame = new ButtonNewGame(atlas, this);
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(atlas, explosionSound);
        bulletPool = new BulletPool();
        shuttle = new Shuttle(atlas, bulletPool, explosionPool);
        stars = new TrackingStar[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new TrackingStar(atlas, shuttle.getShipV());
        }
        extraPool = new ExtraPool(worldBounds);
        enemyPool = new EnemyPool(worldBounds, bulletPool, explosionPool);
        enemyEmitter = new EnemyEmitter(worldBounds, enemyPool, atlas);
        font = new Font("font/font.fnt", "font/font.png");
        font_hp = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        font_hp.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        frags = 0;
        state = State.PLAYING;
        //TODO добавить в атлас
        hp = new Texture("textures/circle.png");
        dm = new Texture("textures/circle2.png");
    }

    public void startNewGame() {
        frags = 0;
        shuttle.startNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        extraPool.freeAllActiveObjects();
        state = State.PLAYING;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        shuttle.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        gameOver.resize(worldBounds);
        newGame.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        update(delta);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        shuttle.dispose();
        enemyEmitter.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
        font.dispose();
        font_hp.dispose();
        hp.dispose();
        dm.dispose();
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            extraPool.updateActiveSprites(delta);
            shuttle.update(delta);
            enemyEmitter.generate(delta, frags);
            checkCollisions();
        } else {
            if (!newGameDraw) newGameDraw = gameOver.animation(delta);
            else newGame.animation(delta);
        }
    }

    private void extraPower(Vector2 pos) {
        float typeExtra = (float) Math.random();
        int extraHealth = 0;
        int extraDamage = 0;
        TextureRegion textureRegion = new TextureRegion();
        if (typeExtra < 0.1f) {
            //extraHealth
            extraHealth = 5;
            textureRegion.setRegion(hp);
        }
        if (typeExtra > 0.9f) {
            //extraDamage
            extraDamage = 2;
            textureRegion.setRegion(dm);
        }
        if (extraHealth != 0 || extraDamage != 0) {
            ExtraPower extraPower = extraPool.obtain();
            extraPower.set(pos, textureRegion, extraHealth, extraDamage);
        }
    }

    private void checkCollisions() {
        List<EnemyShip> enemyShips = enemyPool.getActiveObjects();
        List<Bullet> bullets = bulletPool.getActiveObjects();
        List<ExtraPower> extraPowers = extraPool.getActiveObjects();

        if (extraPowers.size() != 0) {
            for (ExtraPower extraPower : extraPowers) {
                if (shuttle.isMe(extraPower.pos)) {
                    shuttle.setDamage(extraPower.getDamage());
                    shuttle.setHp(extraPower.getHp());
                    extraPower.destroy();
                }
            }
        }

        if (enemyShips.size() != 0) {
            shuttle.setToFire(true);
            for (EnemyShip enemyShip : enemyShips) {
                float minDist = enemyShip.getHalfWidth() + shuttle.getHalfWidth();
                if (enemyShip.pos.dst(shuttle.pos) < minDist) {
                    enemyShip.destroy();
                    shuttle.damage(enemyShip.getDamage() * 2);
                }
            }
        }
        else shuttle.setToFire(false);

        if (bullets.size() != 0) {
            for (Bullet bullet : bullets) {
                if (bullet.isDestroyed()) {
                    continue;
                }
                if (bullet.getOwner() == shuttle) {
                    for (EnemyShip enemyShip : enemyShips) {
                        if (enemyShip.isDestroyed()) {
                            continue;
                        }
                        if (enemyShip.isMe(bullet.pos)) {
//                        if (enemyShip.isBulletCollision(bullet)) {
                            enemyShip.damage(bullet.getDamage());
                            bullet.destroy();
                        }
                        if (enemyShip.isDestroyed()) {
                            frags++;
                            extraPower(enemyShip.pos);
                        }
                    }
                } else {
                    if (shuttle.isMe(bullet.pos)) {
//                    if (shuttle.isBulletCollision(bullet)) {
                        shuttle.damage(bullet.getDamage());
                        bullet.destroy();
                    }
                }
            }
        }
        if (shuttle.isDestroyed()) state = State.GAME_OVER;
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
        extraPool.freeAllDestroyed();
    }

    private void draw() {
        ScreenUtils.clear(0.33f, 0.45f, 0.68f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        if (state == State.PLAYING) {
            shuttle.draw(batch);
            enemyPool.drawActiveSprites(batch);
            bulletPool.drawActiveSprites(batch);
            extraPool.drawActiveSprites(batch);
        } else {
            gameOver.draw(batch);
            if (newGameDraw) newGame.draw(batch);
        }
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + PADDING, worldBounds.getTop() - PADDING);
        if (state == State.PLAYING) {
            if (shuttle.getHp() <= 4 + enemyEmitter.getLevel()) {
                font_hp.setColor(Color.RED);
            } else {
                font_hp.setColor(Color.WHITE);
            }
            font_hp.draw(batch, sbHP.append(HP).append(shuttle.getHp()), worldBounds.pos.x, worldBounds.getTop() - PADDING, Align.center);
        }
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - PADDING, worldBounds.getTop() - PADDING, Align.right);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            shuttle.touchDown(touch, pointer, button);
        } else {
            newGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            shuttle.touchUp(touch, pointer, button);
        } else {
            newGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            shuttle.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            shuttle.keyUp(keycode);
        }
        return false;
    }
}