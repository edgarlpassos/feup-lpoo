package lpoo.proj2.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import lpoo.proj2.screens.GameScreen;

/**
 * Created by epassos on 5/13/16.
 */
public class Player extends Sprite {

    public enum State {IDLE, START_RUN, RUNNING, STOP, TURNING, TURNING_RUN, RUN_JUMP, LONG_JUMP, FALLING,
        WALKING, DEAD, ATTACKING, DEFENDING, GETTING_SWORD, STORING_SWORD, SWORD_IDLE, DRINKING, CLIMBING, CLIMB_JUMP, DROP, ESCAPING}

    public World world;
    public Body body;
    private TextureRegion idle;
    private Animation running;
    private Animation start_run;
    private float elapsedTime;
    private State currentState;
    private State previousState;

    public Player( GameScreen screen){

        super(screen.getTextures().findRegion("playersprites"));
        this.world = screen.getWorld();
        definePlayer();

        currentState = State.IDLE;
        previousState = State.IDLE;

        //idle sprite
        idle = new TextureRegion(getTexture(),0,0,30,40);
        setBounds(0,0,30,40);
        setRegion(idle);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //start_run animation
        for(int i = 0; i < 14; i++){
            frames.add(new TextureRegion(getTexture(),(i*30),0,30,40));
        }
        start_run = new Animation(0.1f,frames);

        frames.clear();

        //run cycle animation
        for(int i = 0; i < 8; i++){
            frames.add(new TextureRegion(getTexture(),(i*33),50,33,40));
        }
        running = new Animation(0.1f,frames, Animation.PlayMode.LOOP);

        frames.clear();

        elapsedTime = 0;

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
        setRegion(getFrame(dt));
        System.out.println(elapsedTime);

        if(currentState == State.STOP && !body.getLinearVelocity().isZero(0.05f))
            body.applyForce(-100f,0f,body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() / 2,true);

        if(body.getLinearVelocity().isZero(0.05f)){
            body.setLinearVelocity(0,0);
        }
    }

    public void run(){
        if(currentState == State.START_RUN){
                if(start_run.isAnimationFinished(elapsedTime)) {
                    previousState = currentState;
                    currentState = State.RUNNING;
                    elapsedTime = 0;
                }

            else
                 body.applyLinearImpulse(1.0f,0f,body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() / 2,true); //FIXME adjust speed
        }

        else if(currentState != State.RUNNING ){
            previousState = currentState;
            currentState = State.START_RUN;
            elapsedTime = 0;
        }
        body.applyLinearImpulse(4f,0f,body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() / 2,true); //FIXME adjust speed
    }

    public void stop(){
        previousState = currentState;
        currentState = State.STOP;
    }

    public TextureRegion getFrame(float dt){
        if(currentState == State.START_RUN){
            elapsedTime += dt;
            return start_run.getKeyFrame(elapsedTime);
        }


        else if (currentState == State.RUNNING){
            elapsedTime += dt;
            return running.getKeyFrame(elapsedTime);
        }

        else if(currentState == State.IDLE){
            return idle;
        }

        return idle;
    }

    public State getPreviousState(){
        return previousState;
    }

    public State getCurrentState(){
        return currentState;
    }

}
