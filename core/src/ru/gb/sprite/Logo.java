package ru.gb.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.gb.base.Sprite;
import ru.gb.math.Rect;

public class Logo extends Sprite {

    private float padding = 0.2f;

    public Logo(Texture texture) {
        super(new TextureRegion(texture));
    }

    public Logo(TextureAtlas atlas) {
        super(atlas.findRegion("logo1"));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.05f);
//        this.pos.set(worldBounds.pos);
        setTop(worldBounds.getTop() - padding);
    }
}
