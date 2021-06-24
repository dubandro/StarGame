package ru.gb.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.gb.base.Sprite;
import ru.gb.math.Rect;

public class GameOver extends Sprite {

    private static final float HEIGHT = 0.06f;
    private float height = 0.2f;

    public GameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setTop(worldBounds.pos.y + 0.12f);
    }

    public boolean animation(float delta) {
        if (height > HEIGHT) {
            height -= delta/10;
            setHeightProportion(height);
            return false;
        }
        return true;
    }
}