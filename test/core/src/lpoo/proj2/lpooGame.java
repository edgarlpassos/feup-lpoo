package lpoo.proj2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class lpooGame extends Game {

    public SpriteBatch batch;
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 700;


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
