package lpoo.proj2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import lpoo.proj2.*;
import lpoo.proj2.logic.Player;

/**
 * Created by epassos on 5/13/16.
 */
public class GameScreen implements Screen {

    private World world;
    private lpooGame game;
    private OrthographicCamera cam;
    private Viewport vport;
    private TextureAtlas textures;
    private Button buttonA;
    private Button buttonB;
    private Button walkButton;
    private Button leftButton;
    private Button rightButton;

    Player player;


    public GameScreen(lpooGame game){
        this.game = game;

        cam = new OrthographicCamera();
        vport = new StretchViewport(800,480,cam);
        textures = new TextureAtlas("sp.pack");

        world = new World(new Vector2(0,0),true);

        player = new Player(this);
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


        buttonA = new ImageButton(aBtn,aBtnPressed);
        buttonB = new ImageButton(bBtn,bBtnPressed);
        leftButton = new ImageButton(left,leftPressed);
        rightButton = new ImageButton(right,rightPressed);
        walkButton = new ImageButton(walk,walkPressed,walkPressed);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        buttonA.draw(game.batch,0.5f);
        buttonB.draw(game.batch,0.5f);
        leftButton.draw(game.batch,0.5f);
        rightButton.draw(game.batch,0.5f);
        walkButton.draw(game.batch,0.5f);

        player.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        vport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void update(float delta) {
        handleInput();
        player.update(delta);

        world.step(1/60f,6,2);
    }

    public void handleInput(){
        if(Gdx.input.isTouched())
            player.run();

        else if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
            player.jump();

        else if (player.getCurrentState() != Player.State.STOP)
            player.stop();
    }

    public TextureAtlas getTextures(){
        return textures;
    }
    public World getWorld(){
        return world;
    }
}
