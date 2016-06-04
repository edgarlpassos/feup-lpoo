package lpoo.proj2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class lpooGame extends Game {

    public SpriteBatch batch;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new lpoo.proj2.gui.screen.GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
