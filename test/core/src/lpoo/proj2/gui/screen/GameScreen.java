package lpoo.proj2.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import lpoo.proj2.*;
import lpoo.proj2.gui.Hud;
import lpoo.proj2.logic.Player;

/**
 * Created by epassos on 5/13/16.
 */
public class GameScreen extends MyScreen {

    private World world;
    private OrthographicCamera cam;
    private Viewport vport;
    private TextureAtlas textures;
    private Hud hud;
    public Player player;


    public GameScreen(lpooGame game) {
        super(game);
        lpooGame.music.stop();
        lpooGame.music = Gdx.audio.newMusic(Gdx.files.internal("music/game_music.mp3"));
        lpooGame.music.play();

        cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        vport = new FitViewport(lpooGame.WIDTH, lpooGame.HEIGHT, cam);
        textures = new TextureAtlas("sp.pack");
        hud = new Hud(game.batch, this);
        world = new World(new Vector2(0, 0), true);
        player = new Player(this);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        update(delta);
        game.batch.setProjectionMatrix(vport.getCamera().combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        System.out.println(player.getBoundingRectangle().width);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {

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
        hud.update(delta);
        player.update(delta);
        System.out.println(player.isFacingRight());
        world.step(1 / 60f, 6, 2);
    }

    public void handleInput() {

        //Walking animations
        if (hud.walkEnabled()) {
            if (hud.pressedA()) {   //long jump
                player.jump();
            }

            else if (hud.pressedRight()) {
                if (player.isFacingRight())
                    player.walk();
                //facing left, turn around
                else player.turn();
            }
            else if (hud.pressedLeft()) {
                if (!player.isFacingRight()) {
                    player.walk();
                //facing right, turn around
                } else player.turn();

            } else player.stop();

        //Running animations
        } else {
            if (hud.pressedRight()) {
                if(player.isFacingRight()){
                    if(hud.pressedA())
                        player.jump();

                    else player.run();
                //facing left, turn  around
                } else {
                    player.turn();
                }
            }

            else if(hud.pressedLeft()){
                if(!player.isFacingRight()) {
                    if (hud.pressedA())
                        player.jump();
                    else player.run();
                }
                //facing right, turn around
                else {
                    player.turn();
                }

            }

            else if(hud.pressedA()){
                player.jump();
            }

            else player.stop();
        }

        if(hud.soundPressed()){
            toggleMusic();
        }


    }

    public TextureAtlas getTextures() {
        return textures;
    }

    public World getWorld() {
        return world;
    }

    public void toggleMusic(){
        lpooGame.music.setVolume(hud.soundEnabled() ? 1  : 0);
    }
}
