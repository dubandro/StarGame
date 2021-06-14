package ru.gb.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.gb.base.BaseScreen;
import ru.gb.math.Rect;
import ru.gb.pool.BulletPool;
import ru.gb.sprite.Background;
import ru.gb.sprite.ExitButton;
import ru.gb.sprite.PlayButton;
import ru.gb.sprite.Shuttle;
import ru.gb.sprite.Star;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 400;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private BulletPool bulletPool;
    private Shuttle shuttle;
    private Star[] stars;

    //    private ExitButton exitButton;

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
        bulletPool = new BulletPool();
        shuttle = new Shuttle(atlas, bulletPool);

//        Аналоговый способ разрезать пополам
//        TextureRegion mainShip = atlas.findRegion("main_ship");
//        mainShip.setRegionWidth(195);
//        shuttle = new Shuttle(mainShip);

//        exitButton = new ExitButton(atlas);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        shuttle.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
//        exitButton.resize(worldBounds);
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
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        shuttle.update(delta);
        bulletPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyed();
    }

    private void draw() {
        ScreenUtils.clear(0.33f, 0.45f, 0.68f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        shuttle.draw(batch);
        bulletPool.drawActiveSprites(batch);
//        exitButton.draw(batch);
        batch.end();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        shuttle.touchDown(touch, pointer, button);
//        exitButton.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        shuttle.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        shuttle.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        shuttle.keyUp(keycode);
        return false;
    }
}