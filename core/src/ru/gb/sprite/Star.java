package ru.gb.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.Sprite;
import ru.gb.math.Rect;
import ru.gb.math.Rnd;

public class Star extends Sprite {

    protected final Vector2 v;
    private Rect worldBounds;
    protected float starHeight;
    private float starBlink;
    private final float MINSIZE = 0.0007f;
    private final float MAXSIZE = 0.007f;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        starHeight = Rnd.nextFloat(MINSIZE, MAXSIZE);
        starBlink = Rnd.nextFloat(MINSIZE/100, MAXSIZE/100); //мерцание рандомное для всех размеров
        v = new Vector2();
        float starRatio = starHeight * starHeight * 50;
        float vx = Rnd.nextFloat(-starRatio, starRatio); //чем меньше звезда (чем она дальше), тем меньше отклонение по х
        float vy = starRatio * (-10); //маленькие звёзды движутся медленне => эффект перспективы
        v.set(vx, vy);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        checkBounds();
        animate();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(starHeight);
        float x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(x, y);
    }

    protected void checkBounds() {
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (getLeft() >  worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }
        if (getBottom() > worldBounds.getTop()) {
            setTop(worldBounds.getBottom());
        }
    }

    protected void animate() {
        float height = getHeight();
        if (height < starHeight) height += starBlink;
        else height = starHeight * 0.8f;
        setHeightProportion(height);
    }
}