package lpoo.proj2.logic;

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


import lpoo.proj2.gui.screen.GameScreen;
import lpoo.proj2.lpooGame;

/**
 * Created by epassos on 5/13/16.
 */
public class Player extends Sprite {

    public enum State {
        IDLE, START_RUN, RUNNING, STOP, TURNING, TURNING_RUN, RUN_JUMP, LONG_JUMP, FALLING,
        WALKING, DEAD, ATTACKING, DEFENDING, DRAW_SWORD, SHEATHE_SWORD, SWORD_IDLE, DRINKING, CLIMBING, CLIMB_JUMP, DROP, ESCAPING
    }

    public World world;
    public Body body;

    private TextureRegion idle;
    private Animation running;
    private Animation start_run;
    private Animation stop_run;
    private Animation running_jump;
    private Animation walking;
    private Animation long_jump;
    private Animation turn;
    private Animation run_turn;

    private float elapsedTime;
    private State currentState;
    private State previousState;
    private Animation currentAnimation;
    private boolean facingRight;

    private boolean alive;
    private boolean hasKey;

    public Player(GameScreen screen) {
        super(screen.getTextures().findRegion("playersprites"));
        this.world = screen.getWorld();
        alive = true;
        hasKey = false;
        definePlayer();

        //Inicial status
        facingRight = true;
        currentState = State.IDLE;
        previousState = State.IDLE;

        //idle sprite
        idle = new TextureRegion(getTexture(), 0, 350, 18, 40);
        setBounds(6 * lpooGame.WIDTH / lpooGame.PPM,2*lpooGame.HEIGHT / lpooGame.PPM, 18 /lpooGame.PPM, 40 /lpooGame.PPM);
        setRegion(idle);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //start_run animation
        for (int i = 0; i < 14; i++) {
            frames.add(new TextureRegion(getTexture(), (i * 30), 0, 30, 40));
        }
        start_run = new Animation(0.1f, frames);
        frames.clear();

        //run cycle animation
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(), (i * 31), 50, 31, 40));
        }
        running = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();


        //run stop animation
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), (i * 37), 100, 37, 40));
        }
        stop_run = new Animation(0.1f, frames);
        frames.clear();


        //running jump animation
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), (i * 47), 150, 47, 40));
        }
        running_jump = new Animation(0.1f, frames);
        frames.clear();


        //walking animation
        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(getTexture(), i * 26, 200, 26, 40));
        }
        walking = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        //long jump
        for (int i = 0; i < 14; i++) {
            frames.add(new TextureRegion(getTexture(), i * 48, 250, 48, 40));
        }
        long_jump = new Animation(0.1f, frames);
        frames.clear();

        //turning left
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 18, 300, 18, 40));
        }
        turn = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0 ; i < 11; i++){
            frames.add(new TextureRegion(getTexture(),i*33,350,33,40));
        }
        run_turn = new Animation(0.1f,frames);
        frames.clear();

        setPosition(1400*6+70*4,700*2+70*5);
        body.setTransform(1400*6f+200, 700*2f + 700, 0);
        elapsedTime = 0;

    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((lpooGame.HEIGHT*6+70*4)/lpooGame.PPM,(lpooGame.WIDTH*2+70*5)/ lpooGame.PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape cshape = new PolygonShape();
        cshape.setAsBox((40/2)/lpooGame.PPM,(40/2)/lpooGame.PPM);
        fdef.shape = cshape;

        body.createFixture(fdef).setUserData("player");
    }

    public void update(float dt) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        body.applyForceToCenter(world.getGravity(),true);
        setRegion(getFrame(dt));
        int direction = facingRight ? 1 : -1;

        switch (currentState) {
            case START_RUN:
                body.applyForceToCenter(direction * 300f * lpooGame.PPM, 0f, true);
                break;

            case RUNNING:
                body.applyForceToCenter(direction * 1500f * lpooGame.PPM, 0f, true);
                break;

            case STOP:
                if (stop_run.isAnimationFinished(elapsedTime))
                    body.setLinearVelocity(0, 0);
                else body.applyForceToCenter(direction * -50f * lpooGame.PPM, 0, true);
                break;

            case RUN_JUMP:
                if(elapsedTime < 0.4f || elapsedTime >= 0.9)
                    body.applyForceToCenter(direction * 1500f * lpooGame.PPM, 0, true);

                else body.applyForceToCenter(direction * 1500f * lpooGame.PPM, 0, true);

                break;

            case WALKING:
                if(elapsedTime <= 0.2f || elapsedTime >= 0.5){
                    body.setLinearVelocity(0,0);
                }
                else body.applyForceToCenter(direction * 200f * lpooGame.PPM, 0f,true);

                break;

            case LONG_JUMP:
                if (elapsedTime <= 0.5f || elapsedTime >= 1f)
                    body.setLinearVelocity(0,0);

                else body.applyForceToCenter(direction * 10000f * lpooGame.PPM, 0f, true);
                break;

            case IDLE:
                body.setLinearVelocity(0,0);
                setBounds((body.getPosition().x - getWidth() / 2)/lpooGame.PPM, (body.getPosition().y - getHeight() / 2)/lpooGame.PPM, (idle.getRegionWidth())/lpooGame.PPM, idle.getRegionHeight()/lpooGame.PPM );
                break;

            case TURNING_RUN:
                while(!run_turn.isAnimationFinished(elapsedTime)){
                    body.applyForceToCenter(direction * -10f * lpooGame.PPM,0,true);
                }
                break;
        }

        if (body.getLinearVelocity().isZero(0.5f)) {
            if ((currentState == State.STOP && stop_run.isAnimationFinished(elapsedTime)) || currentState == State.TURNING && turn.isAnimationFinished(elapsedTime)) {
                body.setLinearVelocity(0, 0);
                changeState(State.IDLE);
            }
        }
    }

    public void run() {


        if (currentState == State.IDLE) {
            changeState(State.START_RUN);
            setCurrentAnimation(start_run);
        } else if (currentState == State.START_RUN) {
            if (elapsedTime >= 0.6f) {
                changeState(State.RUNNING);
                setCurrentAnimation(running);
            }
        } else {
            if (currentAnimation.isAnimationFinished(elapsedTime)) {
                setCurrentAnimation(running);
                changeState(State.RUNNING);
            }
        }
    }


    public void stop() {

        switch (currentState){
            case START_RUN:
                if(elapsedTime >= 0.6f){
                    setCurrentAnimation(stop_run);
                    changeState(State.STOP);
                    body.setLinearVelocity(0,0);
                }
                break;

            case RUNNING:
                setCurrentAnimation(stop_run);
                changeState(State.STOP);
                body.setLinearVelocity(0,0);
                break;

            case RUN_JUMP:
                if(running_jump.isAnimationFinished(elapsedTime)) {
                    setCurrentAnimation(stop_run);
                    changeState(State.STOP);
                    body.setLinearVelocity(0, 0);
                }
                break;

            case LONG_JUMP:
                if(long_jump.isAnimationFinished(elapsedTime)) {
                    body.setLinearVelocity(0, 0);
                    changeState(State.IDLE);
                }

                else if( elapsedTime > 1f){
                    body.setLinearVelocity(0, 0);
                }
                break;

            case TURNING_RUN:
                if(run_turn.isAnimationFinished(elapsedTime)){
                    setCurrentAnimation(stop_run);
                    changeState(State.STOP);
                    body.setLinearVelocity(0, 0);
                }
                break;

            default:
                if(currentAnimation == null || currentAnimation.isAnimationFinished(elapsedTime)) {
                    changeState(State.IDLE);
                    body.setLinearVelocity(0, 0);
                }
                break;
        }
    }

    public TextureRegion getFrame(float dt) {

        TextureRegion frame = idle;


        if (currentState != State.IDLE) {
            if (elapsedTime >= currentAnimation.getAnimationDuration())
                elapsedTime = 0;
            frame = currentAnimation.getKeyFrame(elapsedTime);
        }

        if (!facingRight) {
            elapsedTime += dt;
            TextureRegion flipped = new TextureRegion(frame);
            flipped.flip(true, false);
            return flipped;
        }

        elapsedTime += dt;
        return frame;

    }

    public void jump() {
        if (currentState == State.RUNNING) {
            changeState(State.RUN_JUMP);
            setCurrentAnimation(running_jump);
        } else if (currentState == State.WALKING) {
            if (walking.isAnimationFinished(elapsedTime)) {
                changeState(State.LONG_JUMP);
                setCurrentAnimation(long_jump);
            }
        } else if (currentState == State.IDLE) {
            changeState(State.LONG_JUMP);
            setCurrentAnimation(long_jump);
        }
    }

    public void walk() {

        if (currentState == State.TURNING) {
            if (turn.isAnimationFinished(elapsedTime)) {
                changeState(State.WALKING);
                setCurrentAnimation(walking);
            }
        } else if (currentState == State.WALKING) {
            changeState(State.WALKING);
            setCurrentAnimation(walking);
        } else if (currentState == State.IDLE){
            changeState(State.WALKING);
            setCurrentAnimation(walking);
        }
    }

    public void turn() {
        if (currentState == State.WALKING) {
            if (walking.isAnimationFinished(elapsedTime)) {
                changeState(State.TURNING);
                setCurrentAnimation(turn);
                facingRight = !facingRight;
            }
        } else if (currentState == State.IDLE) {
            changeState(State.TURNING);
            setCurrentAnimation(turn);
            facingRight = !facingRight;
        } else if (currentState == State.RUNNING){
            changeState(State.TURNING_RUN);
            setCurrentAnimation(run_turn);
            facingRight = !facingRight;
        } else if (currentState == State.RUN_JUMP){
            if(running_jump.isAnimationFinished(elapsedTime)){
                changeState(State.TURNING_RUN);
                setCurrentAnimation(run_turn);
                facingRight = !facingRight;
            }
        }
    }

    public State getPreviousState() {
        return previousState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean isFacingRight() {
        return facingRight;
    }
    public boolean isAlive(){
        return alive;
    }

    public void isKilled(){
        alive = false;
        //TODO die animation
    }
    public boolean hasKey(){
        return hasKey;
    }

    public void pickUpKey(){
        if(!hasKey) {
            hasKey = true;
            //TODO Pick up key animation
        }
    }

    public void checkExit(){
        if(hasKey){
            //TODO WIN GAME
        }
    }

    public void changeState(State state) {
        previousState = currentState;
        currentState = state;
        elapsedTime = 0;
    }

    public void setCurrentAnimation(Animation animation) {
        currentAnimation = animation;
        setBounds((body.getPosition().x - getWidth() / 2f) / lpooGame.PPM, (body.getPosition().y - getHeight() / 2f) /lpooGame.PPM, (animation.getKeyFrame(0).getRegionWidth())/lpooGame.PPM, (animation.getKeyFrame(0).getRegionHeight()) /lpooGame.PPM);
    }

}
