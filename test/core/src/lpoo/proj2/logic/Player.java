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
    private Animation stop_run;
    private Animation running_jump;
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
        idle = new TextureRegion(getTexture(),198,100,39,40);
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


        //run stop animation
        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(getTexture(),(i*39),100,39,40));
        }
        stop_run = new Animation(0.1f,frames);
        frames.clear();


        //running jump animation
        for(int i = 0; i < 10; i++){
            frames.add(new TextureRegion(getTexture(),(i*50),150,50,43));
        }
        running_jump = new Animation(0.1f,frames);
        frames.clear();


        body.setTransform(-400f,0f,0);
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
       // System.out.println(elapsedTime);

        if(currentState == State.STOP && !body.getLinearVelocity().isZero(0.05f))
            body.applyLinearImpulse(-5f,0f,body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() / 2,true);

        if(body.getLinearVelocity().isZero(0.05f)){
            body.setLinearVelocity(0,0);
            previousState = currentState;
            currentState = State.IDLE;
            setRegion(idle);
            elapsedTime = 0;
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

        else if(currentState == State.RUN_JUMP){
            if(running_jump.isAnimationFinished(elapsedTime)){
                previousState = currentState;
                currentState = State.RUNNING;
                elapsedTime = 0;
            }
        }

        else if(currentState != State.RUNNING ){
            previousState = currentState;
            currentState = State.START_RUN;
            elapsedTime = 0;
        }
        body.applyLinearImpulse(4f,0f,body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() / 2,true); //FIXME adjust speed
    }

    public void stop(){
        if(previousState != State.STOP )
        previousState = currentState;
        currentState = State.STOP;
        elapsedTime = 0;
    }

    public TextureRegion getFrame(float dt){

        elapsedTime += dt;

        if(currentState == State.START_RUN){
            return start_run.getKeyFrame(elapsedTime);
        }


        else if (currentState == State.RUNNING){
            return running.getKeyFrame(elapsedTime);
        }

        else if(currentState == State.IDLE){
            return idle;
        }

        else if(currentState == State.STOP){
            return stop_run.getKeyFrame(elapsedTime);
        }

        else if(currentState == State.RUN_JUMP) {
            return running_jump.getKeyFrame(elapsedTime);
        }
        return idle;
    }

    public void jump(){//TODO different jumps for running and standing
        /*if(previousState == State.RUNNING){
            if(running.isAnimationFinished(elapsedTime)){
                previousState = currentState;
                currentState = State.RUN_JUMP;
                elapsedTime = 0;

                body.applyForce(5f,5f,body.getPosition().x,body.getPosition().y,true);
            }

           else return;
        }

        if(previousState == State.START_RUN){
            if(start_run.isAnimationFinished(elapsedTime)){
                previousState = currentState;
                currentState = State.RUN_JUMP;
                elapsedTime = 0;

                body.applyForce(5f,5f,body.getPosition().x,body.getPosition().y,true);
            }

            else return;
        }*/

        previousState = currentState;
        currentState = State.RUN_JUMP;
        elapsedTime = 0;

        body.applyLinearImpulse(15f,0,body.getPosition().x - getWidth()/2 ,body.getPosition().y - getHeight()/2,true);
    }

    public State getPreviousState(){
        return previousState;
    }

    public State getCurrentState(){
        return currentState;
    }

}
