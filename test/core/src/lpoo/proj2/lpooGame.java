package lpoo.proj2;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lpoo.proj2.gui.screen.MainMenuScreen;
import lpoo.proj2.gui.screen.MyScreen;
import lpoo.proj2.logic.states.GameState;
import lpoo.proj2.logic.states.GameStateManager;

/**
 * main class of the project
 */
public class lpooGame extends Game {


    public static final int WIDTH = 1400;
    public static final int HEIGHT = 700;

    /**
     *  Used for the interaction with the tile, tiles are 70x70 px
     *  PPM = pixels per meter
     */
    public static final float PPM = 70;

    public SpriteBatch batch;
    public GameStateManager gsm;
    public static Music music = null;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();

        //Main Menu creation
        MyScreen menu_screen = new MainMenuScreen(this);
        GameState main_menu = new GameState(menu_screen,gsm);
        gsm.push(main_menu);    //Starts game with main menu
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.render(Gdx.graphics.getDeltaTime());
    }
}
