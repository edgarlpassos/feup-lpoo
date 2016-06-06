package lpoo.proj2.logic;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;

import lpoo.proj2.gui.screen.GameScreen;


/**
 * Created by Antonio on 06-Jun-16.
 */
public class Key {

    private Body body;
    private GameScreen screen;


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
}
