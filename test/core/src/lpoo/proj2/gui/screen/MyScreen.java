package lpoo.proj2.gui.screen;

import com.badlogic.gdx.Screen;

import lpoo.proj2.lpooGame;

/**
 * Created by epassos on 6/4/16.
 */
public abstract class MyScreen implements Screen{

    protected lpooGame game;

    public MyScreen(lpooGame game){
        this.game = game;
    }

    public abstract void update(float delta);
    public abstract void handleInput();
}
