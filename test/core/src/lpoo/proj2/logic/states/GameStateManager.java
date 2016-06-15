package lpoo.proj2.logic.states;


import java.util.Stack;

/**
 * Represents a Game State Manager
 */
public class GameStateManager {
    /**
     * Stack of the sates
     */
    private Stack<GameState> states;

    /**
     * GameStateManager constructor
     */
    public GameStateManager(){
        states = new Stack<GameState>();
    }

    /**
     * Push state to stack
     * @param state Game State
     */
    public void push(GameState state){
        states.push(state);
    }

    /**
     * Render State on the top of the stack
     * @param delta time since last update
     */
    public void render(float delta){
        states.peek().render(delta);
    }

    /**
     * Pop the top of the stack
     */
    public void pop(){
        states.pop().dispose();
    }

    /**
     * Pop top of the stack and push a new state
     * @param state
     */
    public void set(GameState state){
        states.pop();
        states.push(state);
    }

    public void dispose(){
    }
}
