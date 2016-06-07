package lpoo.proj2.gui.screen;

import com.badlogic.gdx.Screen;

import lpoo.proj2.lpooGame;

/**
 * Created by Antonio Melo and Edgar Passos
 */

/**
 * Class used to represent a game screen
 */
public abstract class MyScreen implements Screen{

    /**
     * Current game
     */
    final protected lpooGame game;

    /**
     * Constructor
     * @param game current game
     */
    public MyScreen(lpooGame game){
        this.game = game;
    }

    /**
     * Updates all the elements of the screen
     * @param delta time between updates
     */
    public abstract void update(float delta);

    /**
     * Handles all touches and acts accordingly
     */
    public abstract void handleInput();


    public lpooGame getGame(){
        return game;
    }
}
