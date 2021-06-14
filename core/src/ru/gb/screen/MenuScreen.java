package ru.gb.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.gb.base.BaseScreen;
import ru.gb.math.Rect;
import ru.gb.sprite.Background;
import ru.gb.sprite.ExitButton;
import ru.gb.sprite.Logo;
import ru.gb.sprite.MenuShuttle;
import ru.gb.sprite.PlayButton;
import ru.gb.sprite.Shuttle;
import ru.gb.sprite.Star;

public class MenuScreen extends BaseScreen {

    private static final int STAR_COUNT = 0;

    private final Game game;

    private Texture bg;
    private Texture sh;
    private Background background;
    private MenuShuttle shuttle;
    private Logo logo;
    private ExitButton exitButton;
    private PlayButton playButton;
    private TextureAtlas atlas;
    private Star[] stars;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/background.png");
        background = new Background(bg);
        atlas = new TextureAtlas("textures/gameMenuAtlas.pack");
        logo = new Logo(atlas);
        exitButton = new ExitButton(atlas);
        playButton = new PlayButton(atlas, game);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        sh = new Texture("textures/cockpit.png");
        shuttle = new MenuShuttle(sh);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        shuttle.resize(worldBounds);
        logo.resize(worldBounds);
        exitButton.resize(worldBounds);
        playButton.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        sh.dispose();
        atlas.dispose();
    }

    private void update(float delta) {
        background.update(delta);
        for (Star star : stars) {
            star.update(delta);
        }
    }

    private void draw() {
        ScreenUtils.clear(0.33f, 0.45f, 0.68f, 1);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        shuttle.draw(batch);
        logo.draw(batch);
        exitButton.draw(batch);
        playButton.draw(batch);
        batch.end();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        exitButton.touchDown(touch, pointer, button);
        playButton.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        exitButton.touchUp(touch, pointer, button);
        playButton.touchUp(touch, pointer, button);
        return false;
    }
}