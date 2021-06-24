package ru.gb.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

import ru.gb.base.BaseScreen;
import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.pool.EnemyPool;
import ru.gb.pool.ExplosionPool;
import ru.gb.sprite.Background;
import ru.gb.sprite.Bullet;
import ru.gb.sprite.ButtonNewGame;
import ru.gb.sprite.EnemyShip;
import ru.gb.sprite.GameOver;
import ru.gb.sprite.Shuttle;
import ru.gb.sprite.Star;
import ru.gb.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 400;

    private enum State {PLAYING, GAME_OVER}

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private Sound explosionSound;
    private EnemyEmitter enemyEmitter;
    private Shuttle shuttle;
    private Star[] stars;
    private State state;
    private GameOver gameOver;
    private ButtonNewGame newGame;
    private boolean newGameDraw;

    private final Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/background.png");
        background = new Background(bg);
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        gameOver = new GameOver(atlas);
        newGame = new ButtonNewGame(atlas, game);
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(atlas, explosionSound);
        bulletPool = new BulletPool();
        shuttle = new Shuttle(atlas, bulletPool, explosionPool);
        enemyPool = new EnemyPool(getWorldBounds(), bulletPool, explosionPool);
        enemyEmitter = new EnemyEmitter(getWorldBounds(), enemyPool, atlas);
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
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            shuttle.update(delta);
            enemyEmitter.generate(delta);
            fight();
        } else {
            if (!newGameDraw) newGameDraw = gameOver.animation(delta);
            else newGame.animation(delta);
        }
    }

    /**
     * ДЗ №6
     * Стрельба ведётся автоматической очередью при обнаружении врага на экране
     * Враг уничтожается пулями шаттла и при столкновении с шаттлом
     * @param
     */
    private void fight() {
        List<EnemyShip> enemyShips = enemyPool.getActiveObjects();
        if (enemyShips.size() != 0) {
            shuttle.setEnemyDetected(true);
            for (EnemyShip enemyShip : enemyShips) {
                float minDist = enemyShip.getHalfWidth() + shuttle.getHalfWidth();
                if (enemyShip.pos.dst(shuttle.pos) < minDist) {
                    enemyShip.destroy();
                    shuttle.damage(enemyShip.getDamage() * 2);
                }
            }
            for (Bullet bullet : bulletPool.getActiveObjects()) {
                if (!bullet.isDestroyed()) { //TODO подумать нужна ли эта проверка
                    if (bullet.getOwner() == shuttle) {
                        for (EnemyShip enemyShip : enemyShips) {
//                            if (enemyShip.isMe(bullet.pos)) {
                            if (enemyShip.isBulletCollision(bullet)) {
                                enemyShip.damage(bullet.getDamage());
                                bullet.destroy();
                            }
                        }
                    } else {
//                        if (shuttle.isMe(bullet.pos)) {
                        if (shuttle.isBulletCollision(bullet)) {
                            shuttle.damage(bullet.getDamage());
                            bullet.destroy();
                        }
                    }
                }
            }
        } else shuttle.setEnemyDetected(false);
        if (shuttle.isDestroyed()) state = State.GAME_OVER;
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
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
        } else {
            gameOver.draw(batch);
            if (newGameDraw) newGame.draw(batch);
        }
        batch.end();
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