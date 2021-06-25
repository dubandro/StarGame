package ru.gb.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.gb.base.ScaledButton;
import ru.gb.math.Rect;
import ru.gb.screen.GameScreen;

public class ButtonNewGame extends ScaledButton {

    private static final float NORMAL_HEIGHT = 0.045f;
    private static final float PULSE_HEIGHT = 0.05f;
    private static final float PULSE_PERIOD = 1.0f;
    private float height;
    private float timer;
    private boolean pulse = true;

    private final Game game;

    public ButtonNewGame(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("button_new_game"));
        this.game = game;
        height = NORMAL_HEIGHT * 0.1f;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(height);
        setTop(worldBounds.pos.y);
    }

    @Override
    protected void action() {
        game.setScreen(new GameScreen(game));
    }

    public void animation(float delta) {
        if (height < NORMAL_HEIGHT) {
            height += delta/10;
            if (height > NORMAL_HEIGHT) height = NORMAL_HEIGHT;
        }
        else {
            if (pressed) return;
            timer += delta;
            if (timer > PULSE_PERIOD) {
                if (pulse) {
                    if (height < PULSE_HEIGHT) height += 0.0005f;
                    else {
                        height = PULSE_HEIGHT;
                        pulse = false;
                    }
                } else {
                    if (height > NORMAL_HEIGHT) height -= 0.0005f;
                    else {
                        height = NORMAL_HEIGHT;
                        pulse = true;
                        timer = 0;
                    }
                }
// как вариант можно менять масштаб => меньше анимации
                /*if (timer < PULSE_PERIOD + 0.2f) {
                    scale = 1.1f;
                } else {
                    if (timer < PULSE_PERIOD + 0.4f) {
                        scale = 1;
                    } else timer = 0;
                }*/
            }
        }
        setHeightProportion(height);
    }
}
