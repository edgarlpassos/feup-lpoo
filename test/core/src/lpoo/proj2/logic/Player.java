package lpoo.proj2.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.awt.geom.RectangularShape;

import lpoo.proj2.screens.GameScreen;

/**
 * Created by epassos on 5/13/16.
 */
public class Player extends Sprite {

    public World world;
    public Body body;
    private TextureRegion idle;
    private Animation running;
    private float elapsedTime;

    public Player( GameScreen screen){
        super(screen.getTextures().findRegion("run_cycle"));
        this.world = screen.getWorld();
        definePlayer();
        idle = new TextureRegion(getTexture(),0,0,30,40);
        setBounds(0,0,30,40);
        setRegion(idle);

        elapsedTime = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 8; i++){
            frames.add(new TextureRegion(getTexture(),(i*30),0,30,40));
        }

        running = new Animation(0.1f,frames);
        frames.clear();
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(0,0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape cshape = new CircleShape();
        cshape.setRadius(5);
        fdef.shape = cshape;

        body.createFixture(fdef);
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(elapsedTime));
        elapsedTime += dt;
        System.out.println(elapsedTime);
        body.applyLinearImpulse(5f,0f,body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() / 2,true);
    }

    public TextureRegion getFrame(float dt){
        return running.getKeyFrame(dt,true);
    }
}
