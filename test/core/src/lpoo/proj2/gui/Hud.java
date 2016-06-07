package lpoo.proj2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import lpoo.proj2.*;
import lpoo.proj2.gui.screen.GameScreen;
import lpoo.proj2.gui.screen.Pause;
import lpoo.proj2.logic.states.GameState;


/**
 * Created by Antonio Melo and Edgar Passos
 */

/**
 * Class used to display the input buttons on screen
 */
public class Hud {

    /**
     * libgdx stage to hold the buttons
     */
    public Stage stage;

    /**
     * stage viewport
     */
    public Viewport viewport;

    /**
     * current game screen
     */
    private GameScreen screen;

    /**
     * A button
     */
    private Button buttonA;

    /**
     * B button
     */
    private Button buttonB;

    /**
     * Walk switch
     */
    private Button walkButton;

    /**
     * Left button
     */
    private Button leftButton;

    /**
     * Right button
     */
    private Button rightButton;

    /**
     * Sound button
     */
    private Button sound;
    private Button pauseButton;


    /**
     * Hud constructor, initializes the variables, loads the button images and sets up their layout
     * @param batch game's sprite batch
     * @param screen current game screen
     */
    public Hud(SpriteBatch batch, final GameScreen screen) {

        this.screen = screen;
        viewport = new FitViewport(lpooGame.WIDTH/2, lpooGame.HEIGHT/2, new OrthographicCamera());
        stage = new Stage(viewport, batch);


        Table table = new Table();
        table.setFillParent(true);

        //gui buttons
        TextureRegionDrawable aBtn = new TextureRegionDrawable(new TextureRegion(new Texture("gui/a.png")));
        TextureRegionDrawable aBtnPressed = new TextureRegionDrawable(new TextureRegion(new Texture("gui/a_pressed.png")));

        TextureRegionDrawable bBtn = new TextureRegionDrawable(new TextureRegion(new Texture("gui/b.png")));
        TextureRegionDrawable bBtnPressed = new TextureRegionDrawable(new TextureRegion(new Texture("gui/b_pressed.png")));

        TextureRegionDrawable left = new TextureRegionDrawable(new TextureRegion(new Texture("gui/left.png")));
        TextureRegionDrawable leftPressed = new TextureRegionDrawable(new TextureRegion(new Texture("gui/left_pressed.png")));

        TextureRegionDrawable right = new TextureRegionDrawable(new TextureRegion(new Texture("gui/right.png")));
        TextureRegionDrawable rightPressed = new TextureRegionDrawable(new TextureRegion(new Texture("gui/right_pressed.png")));

        final TextureRegionDrawable walk = new TextureRegionDrawable(new TextureRegion(new Texture("gui/walk.png")));
        TextureRegionDrawable walkPressed = new TextureRegionDrawable(new TextureRegion(new Texture("gui/walk_pressed.png")));

        TextureRegionDrawable soundOn = new TextureRegionDrawable(new TextureRegion(new Texture("gui/volume_on.png")));
        TextureRegionDrawable soundOff = new TextureRegionDrawable(new TextureRegion(new Texture("gui/volume_off.png")));

        //pause
        TextureRegionDrawable pause = new TextureRegionDrawable(new TextureRegion(new Texture("gui/pause.png")));



        buttonA = new ImageButton(aBtn, aBtnPressed);
        buttonB = new ImageButton(bBtn, bBtnPressed);
        leftButton = new ImageButton(left, leftPressed);
        rightButton = new ImageButton(right, rightPressed);
        walkButton = new ImageButton(walk, walkPressed, walkPressed);
        sound = new ImageButton(soundOn,soundOff,soundOff);
        pauseButton = new ImageButton(pause,pause,pause);

        InputListener leftListener = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("DSfadf");
                if(!screen.player.isFacingRight())
                    screen.player.moving = true;
                else screen.player.turn();

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.player.moving = false;
            }
        };

        InputListener rightListener = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(screen.player.isFacingRight())
                    screen.player.moving = true;
                else screen.player.turn();

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.player.moving = false;
            }
        };

        InputListener btnAListener = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.player.jump();
            }
        };

        InputListener musicBtnListener = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.toggleMusic();
            }
        };

        InputListener pauseListener = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.getGame().gsm.push(new GameState(new Pause(screen.getGame()),screen.getGame().gsm));
            }
        };

        leftButton.addListener(leftListener);
        rightButton.addListener(rightListener);
        buttonA.addListener(btnAListener);
        sound.addListener(musicBtnListener);
        pauseButton.addListener(pauseListener);

        //placing the buttons
        table.setDebug(false);
        table.add(pauseButton).align(Align.left).size(40,40);
        table.add(sound).expandX().align(Align.right).size(40,40).colspan(6);
        table.row();
        table.add().expandY();
        table.row();
        table.add(leftButton).size(60,60);
        table.add(rightButton).size(60,60).align(Align.left);
        table.add().expandX();
        table.add(walkButton).size(60,60);//.align(Align.right);

        table.add(buttonA).size(60,60);
        table.add(buttonB).size(60,60);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * updates all the buttons according to input
     */
    public void update() {
        stage.act();
        if(walkEnabled())
            screen.player.walking = true;
        else screen.player.walking = false;
    }
    public boolean pressedPause(){return pauseButton.isPressed();}

    /**
     * Poll the A button
     * @return true if the A button is pressed
     */
    public boolean pressedA() {
        return buttonA.isPressed();
    }

    /**
     * Poll the B button
     * @return true if the B button is pressed
     */
    public boolean pressedB() {
        return buttonB.isPressed();
    }

    /**
     * Poll the left button
     * @return true if the left button is pressed
     */
    public boolean pressedLeft() {
        return leftButton.isPressed();
    }

    /**
     * Poll the Right button
     * @return true if the Right button is pressed
     */
    public boolean pressedRight() {
        return rightButton.isPressed();
    }

    /**
     * Poll the walk switch
     * @return true if the walk switch is enabled
     */
    public boolean walkEnabled() {
        return walkButton.isChecked();
    }

    /**
     * Poll the sound button
     * @return true if the sound is enabled (switch not activated)
     */
    public boolean soundEnabled(){return !sound.isChecked();}

    /**
     * poll the sound button for presses
     * @return true if the button is pressed
     */
    public boolean soundPressed(){
        return sound.isPressed();
    }

    /**
     * @return Hud stage
     */
    public Stage getStage() {
        return stage;
    }
}
