package ru.gb.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.gb.base.BaseScreen;
import ru.gb.math.Rect;
import ru.gb.sprite.Background;
import ru.gb.sprite.Shuttle;

public class MenuScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private Texture sh;
    private Shuttle shuttle;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/background.png");
        background = new Background(bg);
        sh = new Texture("textures/shuttle.png");
        shuttle = new Shuttle(sh);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        shuttle.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(0.33f, 0.45f, 0.68f, 1);
        batch.begin();
        background.draw(batch);
        shuttle.update(delta);
        shuttle.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        sh.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        shuttle.touchDown(touch, pointer, button);
        return false;
    }
}