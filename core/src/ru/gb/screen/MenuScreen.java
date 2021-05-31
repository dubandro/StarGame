package ru.gb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.gb.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture shuttle;
    private Texture background;
    private Vector2 position;
    private Vector2 vector;
    private Vector2 target;
    private float velocity = 2.5f;

    @Override
    public void show() {
        super.show();
        shuttle = new Texture("shuttle.png");
        position = new Vector2(250,220);
        vector = new Vector2();
        target = new Vector2(0,0);
        background = new Texture("background.png");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!position.epsilonEquals(target, 5.0f)) position.add(vector);
        ScreenUtils.clear(0.33f, 0.45f, 0.68f, 1);
        batch.begin();
        batch.draw(background, 0, 0, 1080, 2220);
        batch.draw(shuttle, position.x, position.y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        shuttle.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        target.set(screenX, Gdx.graphics.getHeight() - screenY);
        vector = target.cpy().sub(position); // когда будет много вычислений - вынести
        vector.nor().scl(velocity);
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
