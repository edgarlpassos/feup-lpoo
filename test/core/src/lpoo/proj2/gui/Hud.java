package lpoo.proj2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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


/**
 * Created by epassos on 5/31/16.
 */
public class Hud {

    public Stage stage;
    public Viewport viewport;

    private GameScreen screen;

    private Button buttonA;
    private Button buttonB;
    private Button walkButton;
    private Button leftButton;
    private Button rightButton;
    private Button sound;

    public Hud(SpriteBatch batch, final GameScreen screen) {

        this.screen = screen;
        viewport = new FitViewport(lpooGame.WIDTH, lpooGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

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

        TextureRegionDrawable walk = new TextureRegionDrawable(new TextureRegion(new Texture("gui/walk.png")));
        TextureRegionDrawable walkPressed = new TextureRegionDrawable(new TextureRegion(new Texture("gui/walk_pressed.png")));

        TextureRegionDrawable soundOn = new TextureRegionDrawable(new TextureRegion(new Texture("gui/volume_on.png")));
        TextureRegionDrawable soundOff = new TextureRegionDrawable(new TextureRegion(new Texture("gui/volume_off.png")));




        buttonA = new ImageButton(aBtn, aBtnPressed);
        buttonB = new ImageButton(bBtn, bBtnPressed);
        leftButton = new ImageButton(left, leftPressed);
        rightButton = new ImageButton(right, rightPressed);
        walkButton = new ImageButton(walk, walkPressed, walkPressed);
        sound = new ImageButton(soundOn,soundOff,soundOff);

        //placing the buttons
        table.add(sound).expandX().align(Align.right).size(40,40).colspan(6);
        table.row();
        table.add().expandY();
        table.row();
        table.add(leftButton).size(40,40);
        table.add(rightButton).size(40,40).align(Align.left);
        table.add().expandX();
        table.add(walkButton).size(40,40);//.align(Align.right);

        table.add(buttonA).size(40,40);
        table.add(buttonB).size(40,40);

        stage.addActor(table);
        table.setDebug(true);   //TODO remove later
    }

    public void update(float delta) {
        stage.act();
    }

    public boolean pressedA() {
        return buttonA.isPressed();
    }

    public boolean pressedB() {
        return buttonB.isPressed();
    }

    public boolean pressedLeft() {
        return leftButton.isPressed();
    }

    public boolean pressedRight() {
        return rightButton.isPressed();
    }

    public boolean walkEnabled() {
        return walkButton.isChecked();
    }

    public boolean soundEnabled(){return !sound.isChecked();}

    public boolean soundPressed(){
        return sound.isPressed();
    }

    public Stage getStage() {
        return stage;
    }
}
