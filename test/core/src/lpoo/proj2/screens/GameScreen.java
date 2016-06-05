package lpoo.proj2.screens;

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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.physics.box2d.BodyDef;

import lpoo.proj2.*;
import lpoo.proj2.gui.Hud;
import lpoo.proj2.logic.Player;



/**
 * Created by epassos on 5/13/16.
 */
public class GameScreen implements Screen {

    private static int groundid = 22;
    private static int lamaid = 23;
    private static int doorid = 24;
    private static int keyid = 25;

    private World world;
    private lpooGame game;
    private OrthographicCamera cam;
    private Viewport vport;
    private TextureAtlas textures;
    private Hud hud;

    //Map variables
    private OrthogonalTiledMapRenderer rend;
    private TiledMap map;
    private TmxMapLoader mapLoader;
    //Box2d
    private Box2DDebugRenderer b2dr;

    //Player
    public Player player;


    public GameScreen(lpooGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        //System.out.println("HI"); //FIXME REMOVE
        cam = new OrthographicCamera();
        vport = new StretchViewport(game.WIDTH, game.HEIGHT, cam);
        textures = new TextureAtlas("sp.pack");
        hud = new Hud(game.batch, this);
        world = new World(new Vector2(0, 0), true);
        player = new Player(this);
        loadmap();
    }

    public void loadmap(){
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map/Map.tmx");

        rend = new OrthogonalTiledMapRenderer(map);
        b2dr = new Box2DDebugRenderer();


        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Ground
        for(MapObject obj : map.getLayers().get(groundid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX()+rect.getWidth()/2,rect.getY()+rect.getHeight()/2);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fdef.shape = shape;

            body.createFixture(fdef);
        }

        // Lama
        for(MapObject obj : map.getLayers().get(lamaid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX()+rect.getWidth()/2,rect.getY()+rect.getHeight()/2);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fdef.shape = shape;

            body.createFixture(fdef);
        }

        //Door
        for(MapObject obj : map.getLayers().get(doorid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX()+rect.getWidth()/2,rect.getY()+rect.getHeight()/2);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fdef.shape = shape;

            body.createFixture(fdef);
        }

        //Key
        for(MapObject obj : map.getLayers().get(keyid).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX()+rect.getWidth()/2,rect.getY()+rect.getHeight()/2);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fdef.shape = shape;

            body.createFixture(fdef);
        }


        cam.position.set(vport.getWorldWidth()/2,vport.getWorldHeight()/2,0);
        //Starting point
        cam.position.add(game.WIDTH*6,game.HEIGHT*2,0);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        rend.render();
        b2dr.render(world,cam.combined);

        game.batch.setProjectionMatrix(vport.getCamera().combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        vport.update(width, height);
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
        System.out.print(player.getX());
        System.out.println(player.body.getPosition().x);

        System.out.print(player.getY());
        System.out.println(player.body.getPosition().y);
        player.update(delta);
        //System.out.println(player.isFacingRight());
        world.step(1 / 60f, 6, 2);
    }

    public void handleInput() {
        if (hud.walkEnabled()) {
            //TODO walking animations
            if(hud.pressedRight()){
                if(player.isFacingRight())
                    player.walk();
            }

            if(hud.pressedLeft()){
                if(player.isFacingRight()){
                    player.turn();
                }

                else player.walk();
            }

            if(hud.pressedA()){
                player.jump(); //TODO facing left
            }

            else player.stop();
        } else {
            if (hud.pressedRight() && player.isFacingRight()) {

                if (hud.pressedA()) {
                    //System.out.println(player.getCurrentState());
                    player.jump();
                } else player.run();
            } else if (hud.pressedLeft()) {
                //TODO flip normal animations
            } else player.stop();
        }
    }

    public TextureAtlas getTextures() {
        return textures;
    }

    public World getWorld() {
        return world;
    }
}
