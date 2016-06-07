package lpoo.proj2.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.org.apache.xpath.internal.operations.String;

/**
 * Created by epassos on 6/7/16.
 */
public class MyGame {

    private int groundID;
    private int lavaID;
    private int keyID;
    private int doorID;
    private int climbableID;
    private String mapName;

    private TmxMapLoader mapLoader;

    private Player player;
    private Key key;
    private World world;
    private TiledMap map;


    public MyGame(String mapName, int groundID, int lavaID, int keyID,int  doorID,int climbableID){

        this.lavaID = lavaID;
        this.groundID = groundID;
        this.keyID = keyID;
        this.doorID = doorID;
        this.climbableID = climbableID;
        this.mapName = mapName;

        this.player = new Player();

    }
}
