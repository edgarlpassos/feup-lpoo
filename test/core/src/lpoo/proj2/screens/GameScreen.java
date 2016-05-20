package lpoo.proj2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
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
    Player player;


    public GameScreen(lpooGame game){
        this.game = game;

        cam = new OrthographicCamera();
        vport = new StretchViewport(800,480,cam);
        textures = new TextureAtlas("sp.pack");

        world = new World(new Vector2(0,0),true);

        player = new Player(this);
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
        handleInput(delta);
        player.update(delta);
        world.step(1/60f,6,2);
    }

    public void handleInput(float delta){
        if(Gdx.input.isTouched())
            player.run();
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
