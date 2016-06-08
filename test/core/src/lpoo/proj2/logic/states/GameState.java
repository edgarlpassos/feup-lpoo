package lpoo.proj2.logic.states;

import com.badlogic.gdx.Screen;

/**
 * Represents a Game state
 */
public class GameState {
    /**
     * Game state screen
     */
    private Screen screen;
    /**
     * Game State Manager
     */
    private GameStateManager gsm;

    /**
     * Game State constructor
     * @param screen Game state screen
     * @param manager Game state manager
     */
    public GameState(Screen screen, GameStateManager manager){
        this.screen = screen;
        gsm = manager;
    }

    /**
     * Render method
     * @param delta time since last update
     */
    public void render(float delta){
        screen.render(delta);
    }

    /**
     * Dispose of the screen
     */
    public void dispose(){
        screen.dispose();
    }

}
