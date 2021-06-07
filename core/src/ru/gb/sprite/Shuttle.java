package ru.gb.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.Sprite;
import ru.gb.math.Rect;

public class Shuttle extends Sprite {
    // lesson4 - заглушка
    private Vector2 vector;
    private Vector2 target;
    private final float velocity = 0.005f;

    public Shuttle(Texture texture) {
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.08f);
        this.pos.set(worldBounds.pos);
    }

    @Override
    public void update(float delta) {
        if (target != null && !this.pos.epsilonEquals(target, 0.01f)) this.pos.add(vector);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        target = new Vector2();
        target.set(touch);
        vector = target.cpy().sub(this.pos); // когда будет много вычислений - вынести
        vector.nor().scl(velocity);
        return false;
    }
}
