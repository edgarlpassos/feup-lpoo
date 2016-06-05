package lpoo.proj2.logic.states;

import com.badlogic.gdx.Screen;

/**
 * Created by epassos on 6/4/16.
 */
public class GameState {

    private Screen screen;
    private GameStateManager gsm;

    public GameState(Screen screen, GameStateManager manager){
        this.screen = screen;
        gsm = manager;
    }

    public void render(float delta){
        screen.render(delta);
    }

    public void dispose(){
        screen.dispose();
    }

}
