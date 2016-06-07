package lpoo.proj2.logic.states;


import java.util.Stack;

/**
 * Created by epassos on 6/4/16.
 */
public class GameStateManager {

    private Stack<GameState> states;

    public GameStateManager(){
        states = new Stack<GameState>();
    }

    public void push(GameState state){
        states.push(state);
    }

    public void render(float delta){
        states.peek().render(delta);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(GameState state){
        states.pop();
        states.push(state);
    }
}
