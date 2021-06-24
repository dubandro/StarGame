package ru.gb.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.base.ScaledButton;
import ru.gb.math.Rect;
import ru.gb.screen.GameScreen;

public class ButtonMenuPlay extends ScaledButton {

    private static final float HEIGHT = 0.2f;
    private static final float PADDING = 0;

    private final Game game;

    public ButtonMenuPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btnStart"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
        setLeft(worldBounds.getLeft() + PADDING);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen(game));
    }
}