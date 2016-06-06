package lpoo.proj2.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.physics.box2d.BodyDef;
import java.lang.Math;

import lpoo.proj2.*;
import lpoo.proj2.gui.Hud;
import lpoo.proj2.logic.Player;
import lpoo.proj2.logic.WorldContactListener;


/**
 * Created by epassos on 5/13/16.
 */
public class GameScreen extends MyScreen {

    private static final int groundid = 22;
    private static final int lavaid = 23;
    private static final int doorid = 24;
    private static final int keyid = 25;
    private static final int climbableid = 26;

    private World world;
    private OrthographicCamera cam;
    private Viewport vport;
    private TextureAtlas textures;
    private Hud hud;

    public enum Switch {UP, DOWN, LEFT, RIGHT};

    //Map variables
    private OrthogonalTiledMapRenderer rend;
    private TiledMap map;
    private TmxMapLoader mapLoader;
    //Box2d
    private Box2DDebugRenderer b2dr;

    //Player
    public Player player;


    public GameScreen(lpooGame game) {
        super(game);
        lpooGame.music.stop();
        lpooGame.music = Gdx.audio.newMusic(Gdx.files.internal("music/game_music.mp3"));
        lpooGame.music.play();



        cam = new OrthographicCamera();
        vport = new FitViewport(lpooGame.WIDTH/lpooGame.PPM,lpooGame.HEIGHT/lpooGame.PPM, cam);
        cam.setToOrtho(false,vport.getWorldWidth(),vport.getWorldHeight());
        textures = new TextureAtlas("sp.pack");
        hud = new Hud(game.batch, this);
        world = new World(new Vector2(0, -10), true);
        player = new Player(this);
        world.setContactListener(new WorldContactListener(player));
        loadmap();
    }

    public void loadmap(){
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map/Map.tmx");

        rend = new OrthogonalTiledMapRenderer(map,1/lpooGame.PPM);
        b2dr = new Box2DDebugRenderer();


        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Ground
        for(MapObject obj : map.getLayers().get(groundid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2) / lpooGame.PPM,(rect.getY()+rect.getHeight()/2)/ lpooGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/ lpooGame.PPM,rect.getHeight()/2/ lpooGame.PPM);
            fdef.shape = shape;

            body.createFixture(fdef).setUserData("ground");
        }

        // Lava
        for(MapObject obj : map.getLayers().get(lavaid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ lpooGame.PPM,(rect.getY()+rect.getHeight()/2)/ lpooGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/ lpooGame.PPM,rect.getHeight()/2/ lpooGame.PPM);
            fdef.shape = shape;

            body.createFixture(fdef).setUserData("lava");
        }

        //Door
            for(MapObject obj : map.getLayers().get(doorid).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX()+rect.getWidth()/2)/lpooGame.PPM,(rect.getY()+rect.getHeight()/2)/ lpooGame.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth()/2/ lpooGame.PPM,rect.getHeight()/2/ lpooGame.PPM);
                fdef.shape = shape;

                body.createFixture(fdef).setUserData("door");
            }

        //Key
        for(MapObject obj : map.getLayers().get(keyid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ lpooGame.PPM,(rect.getY()+rect.getHeight()/2)/ lpooGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/ lpooGame.PPM,rect.getHeight()/2/ lpooGame.PPM);
            fdef.shape = shape;

            body.createFixture(fdef).setUserData("key");
        }

        //Climbable edges
        for(MapObject obj : map.getLayers().get(climbableid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ lpooGame.PPM,(rect.getY()+rect.getHeight()/2)/ lpooGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/ lpooGame.PPM,rect.getHeight()/2/ lpooGame.PPM);
            fdef.shape = shape;

            body.createFixture(fdef).setUserData("climbable");
        }

        //Starting point
        cam.position.add(game.WIDTH*6/lpooGame.PPM,1400/lpooGame.PPM,0);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta){
        rend.render();
        //b2dr.render(world,cam.combined);
        update(delta);
        game.batch.setProjectionMatrix(vport.getCamera().combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
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
        cam.update();
        rend.setView(cam);
        player.update(delta);
        world.step(1 / 60f, 6, 2);
    }

    public void handleInput() {

        //Walking animations
        if (hud.walkEnabled()) {

            if (hud.pressedRight()) {
                if (player.isFacingRight()) {
                    if (hud.pressedA()) {   //long jump
                        player.jump();
                    }
                    player.walk();
                }
                //facing left, turn around
                else player.turn();
            }
            else if (hud.pressedLeft()) {
                if (!player.isFacingRight()) {
                    if (hud.pressedA()) {   //long jump
                        player.jump();
                    }
                    player.walk();
                //facing right, turn around
                } else player.turn();

            } else if (hud.pressedA()) {   //long jump
                player.climb();
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
                player.climb();
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
    public TiledMap getMap() {return map;}

    public void toggleMusic(){
        lpooGame.music.setVolume(hud.soundEnabled() ? 1  : 0);
    }

    public void switchCamera(Switch direction){
        switch(direction){
            case DOWN:
                cam.position.add(0,-lpooGame.HEIGHT / lpooGame.PPM,0);
                break;
            case UP:
                cam.position.add(0,lpooGame.HEIGHT / lpooGame.PPM,0);
                break;
            case LEFT:
                cam.position.add(-lpooGame.WIDTH / lpooGame.PPM,0,0);
                break;
            case RIGHT:
                cam.position.add(lpooGame.WIDTH / lpooGame.PPM,0,0);
                break;
        }
    }

    public OrthographicCamera getCam(){
        return cam;
    }
}
