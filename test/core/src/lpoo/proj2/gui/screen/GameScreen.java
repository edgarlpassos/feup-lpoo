package lpoo.proj2.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.physics.box2d.BodyDef;
import lpoo.proj2.*;
import lpoo.proj2.gui.Hud;
import lpoo.proj2.logic.Player;
import lpoo.proj2.logic.WorldContactListener;
import lpoo.proj2.logic.Key;


/**
 * Created by Antonio Melo and Edgar Passos
 */

/**
 * Class used when the application is in the game mode
 */
public class GameScreen extends MyScreen {

    /**
     * id of the ground layer in the tmx map
     */
    private static final int groundid = 22;

    /**
     * id of the lava layer in the tmx map
     */
    private static final int lavaid = 23;

    /**
     * id of the door layer in the tmx map
     */
    private static final int doorid = 24;

    /**
     * id of the key layer in the tmx map
     */
    private static final int keyid = 25;

    /**
     * id of the climbable layer in the tmx map
     */
    private static final int climbableid = 26;

    /**
     * Box2d physics world
     */
    private World world;

    /**
     * Screen camera
     */
    private OrthographicCamera cam;

    /**
     * Screen viewport
     */
    private Viewport vport;

    /**
     * Texture pack
     */
    private TextureAtlas textures;

    /**
     * Screen HUD, displays the input buttons
     */
    private Hud hud;

    /**
     * Used to snap cameras to new position
     */
    public enum Switch {UP, DOWN, LEFT, RIGHT};

    //Map variables
    /**
     * Renders the tiled map
     */
    private OrthogonalTiledMapRenderer rend;

    /**
     * Game map
     */
    private TiledMap map;

    /**
     * map loader
     */
    private TmxMapLoader mapLoader;


    //Box2d
    private Box2DDebugRenderer b2dr; //TODO remove

    //Player
    /**
     * Player character
     */
    public Player player;

    /**
     * Key used to finish the level
     */
    private Key key;


    /**
     * Constructor of the Game Screen, changes the music, initializes the variables and the physics world
     * and loads the map
     *
     * @param game current game
     */
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
        key = null;
        world.setContactListener(new WorldContactListener(player));
        loadmap();
    }

    /**
     * loads the tiled map and defines all its components in the box2d world
     */
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
            key = new Key(body,this);
        }

        //Climbable edges
        for(MapObject obj : map.getLayers().get(climbableid).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / lpooGame.PPM, (rect.getY() + rect.getHeight() / 2) / lpooGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / lpooGame.PPM, rect.getHeight() / 2 / lpooGame.PPM);
            fdef.shape = shape;

            body.createFixture(fdef).setUserData("climbable");

            //Starting point
            //cam.position.add(game.WIDTH*6/lpooGame.PPM,1400/lpooGame.PPM,0);
        }
    }

    @Override
    public void show() {
        //Do nothing
    }

    @Override
    public void render(float delta){
        rend.render();
        //b2dr.render(world,cam.combined); TODO remove

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
        //Do nothing
    }

    @Override
    public void pause() {
        //Do nothing
    }

    @Override
    public void resume() {
        //Do nothing
    }

    @Override
    public void hide() {
        //Do nothing
    }

    @Override
    public void dispose() {
        //// TODO: 6/7/16  
    }

    public void update(float delta) {
        handleInput();
        hud.update(delta);

        if(player.hasKey() && key.getBody() != null){
            world.destroyBody(key.getBody());
            key.getCell().setTile(null);
            key.setBody(null);
        }

        if(!player.isAlive()){
            game.gsm.pop();
        }

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
