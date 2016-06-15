package lpoo.proj2.logic;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;

import lpoo.proj2.gui.screen.GameScreen;


/**
 * Represents a key in the game
 */
public class Key {

    /**
     * Body Key
     */
    private Body body;
    /**
     *  GameScreen key is in
     */
    private GameScreen screen;

    /**
     * Key constructor
     * @param body of the key
     * @param screen key is in
     */
    public Key(Body body, GameScreen screen) {
        this.body = body;
        this.screen = screen;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) screen.getMap().getLayers().get(2);
        return layer.getCell((int)(body.getPosition().x),(int)(body.getPosition().y));
    }

    public void dispose(){
        screen.dispose();
    }
}
