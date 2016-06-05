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

    public enum State {
        IDLE, START_RUN, RUNNING, STOP, TURNING, TURNING_RUN, RUN_JUMP, LONG_JUMP, FALLING,
        WALKING, DEAD, ATTACKING, DEFENDING, GETTING_SWORD, STORING_SWORD, SWORD_IDLE, DRINKING, CLIMBING, CLIMB_JUMP, DROP, ESCAPING
    }

    public World world;
    public Body body;

    private boolean facingRight;

    private TextureRegion idle;
    private Animation running;
    private Animation start_run;
    private Animation stop_run;
    private Animation running_jump;
    private Animation walking;
    private Animation long_jump;
    private Animation turn;
    private float elapsedTime;
    private State currentState;
    private State previousState;

    public Player(GameScreen screen) {

        super(screen.getTextures().findRegion("playersprites"));
        this.world = screen.getWorld();
        definePlayer();

        facingRight = true;

        currentState = State.IDLE;
        previousState = State.IDLE;

        //idle sprite
        idle = new TextureRegion(getTexture(), 198, 100, 39, 40);
        setBounds(0, 0, 39, 40);
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
            frames.add(new TextureRegion(getTexture(), (i * 33), 50, 33, 40));
        }
        running = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();


        //run stop animation
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), (i * 39), 100, 39, 40));
        }
        stop_run = new Animation(0.1f, frames);
        frames.clear();


        //running jump animation
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), (i * 50), 150, 50, 43));
        }
        running_jump = new Animation(0.1f, frames);
        frames.clear();


        //walking animation
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(), i * 40, 200, 40, 40));
        }
        walking = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        //long jump
        for (int i = 0; i < 15; i++) {
            frames.add(new TextureRegion(getTexture(), i * 50, 250, 50, 40));
        }
        long_jump = new Animation(0.1f, frames);
        frames.clear();

        //turning
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 50, 300, 50, 40));
        }
        turn = new Animation(0.12f, frames);
        frames.clear();

        body.setTransform(-400f, 0f, 0);
        elapsedTime = 0;

    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(70*3, 4*70);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape cshape = new CircleShape();
        cshape.setRadius(5);
        fdef.shape = cshape;

        body.createFixture(fdef);
    }

    public void update(float dt) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        //System.out.println(elapsedTime); FIXME remove

        //STARTING RUN
        if (currentState == State.START_RUN)
            body.applyLinearImpulse(1f, 0f, body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, true); //FIXME adjust speed

        //RUNNING
        if (currentState == State.RUNNING)
            body.applyLinearImpulse(4f, 0f, body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, true); //FIXME adjust speed

        //STOPPING
        if (currentState == State.STOP && !body.getLinearVelocity().isZero(0.5f))
            body.applyLinearImpulse(-5f, 0f, body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, true);

        if (body.getLinearVelocity().isZero(0.5f)) {
            if ((currentState == State.STOP && stop_run.isAnimationFinished(elapsedTime)) || currentState == State.TURNING && turn.isAnimationFinished(elapsedTime)) {
                body.setLinearVelocity(0, 0);
                previousState = currentState;
                currentState = State.IDLE;
                elapsedTime = 0;
            }
        }

        //Running jump
        if (currentState == State.RUN_JUMP)
            body.applyLinearImpulse(15f, 0, body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, true);

        //Walking
        if (currentState == State.WALKING)
            body.applyLinearImpulse(1f, 0f, body.getWorldCenter().x, body.getWorldCenter().y, true);

        //Long jump
        if (currentState == State.LONG_JUMP) {
            if (elapsedTime > 0.5f && elapsedTime < 1f)
                body.applyForceToCenter(100000f, 0f, true);
        }

        //turning
        if (currentState == State.TURNING && !body.getLinearVelocity().isZero(0.5f)) {
            if (!facingRight)
                body.applyForceToCenter(-5f, 0, true);

            else body.applyForceToCenter(5f, 0, true);
        }

    }

    public void run() {
        if (currentState == State.START_RUN) {
            if (start_run.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                currentState = State.RUNNING;
                elapsedTime = 0;
            }
        } else if (currentState == State.RUN_JUMP) {
            if (running_jump.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                currentState = State.RUNNING;
                elapsedTime = 0;
            }
        } else if (currentState == State.LONG_JUMP) {
            if (long_jump.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                currentState = State.RUNNING;
                elapsedTime = 0;
            }

        } else if (currentState != State.RUNNING) {
            previousState = currentState;
            currentState = State.START_RUN;
            elapsedTime = 0;
        }
    }


    public void stop() {

        if (currentState == State.START_RUN) {
            if (start_run.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                currentState = State.STOP;
                elapsedTime = 0;
            }
        } else if (currentState == State.RUNNING) {
            previousState = currentState;
            currentState = State.STOP;
            elapsedTime = 0;
        } else if (currentState == State.WALKING) {
            if (walking.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                body.setLinearVelocity(0f, 0f);
                currentState = State.IDLE;
                elapsedTime = 0;
            }
        } else if (currentState == State.LONG_JUMP) {
            if (elapsedTime > 1f)
                body.setLinearVelocity(0, 0);
            if (long_jump.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                body.setLinearVelocity(0f, 0f);
                currentState = State.IDLE;
                elapsedTime = 0;
            }
        }

    }

    public TextureRegion getFrame(float dt) {

        TextureRegion frame = idle;

        if (currentState == State.START_RUN) {
            frame = start_run.getKeyFrame(elapsedTime);
        } else if (currentState == State.RUNNING) {
            frame = running.getKeyFrame(elapsedTime);
        } else if (currentState == State.IDLE) {
            frame = idle;
        } else if (currentState == State.STOP) {
            frame = stop_run.getKeyFrame(elapsedTime);
        } else if (currentState == State.RUN_JUMP) {
            frame = running_jump.getKeyFrame(elapsedTime);
        } else if (currentState == State.WALKING) {
            frame = walking.getKeyFrame(elapsedTime);
        } else if (currentState == State.LONG_JUMP) {
            frame = long_jump.getKeyFrame(elapsedTime);
        } else if (currentState == State.TURNING) {
            frame = turn.getKeyFrame(elapsedTime);
        }

        if(!facingRight){
            elapsedTime += dt;
            TextureRegion flipped = new TextureRegion(frame);
            flipped.flip(true,false);
            return flipped;
        }

        elapsedTime += dt;
        return frame;

    }

    public void jump() {//TODO different jumps for running and standing
        if (currentState == State.RUNNING) {
            previousState = currentState;
            currentState = State.RUN_JUMP;
            elapsedTime = 0;
        } else if (currentState == State.WALKING) {
            if (walking.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                currentState = State.LONG_JUMP;
                elapsedTime = 0;
                body.applyForceToCenter(100000f, 0f, true);
            }
        } else if (currentState == State.IDLE) {
            previousState = currentState;
            currentState = State.LONG_JUMP;
            elapsedTime = 0;
        }
    }

    public void walk() {

        if (currentState == State.TURNING){
            if(turn.isAnimationFinished(elapsedTime)){
                previousState = currentState;
                currentState = State.WALKING;
                elapsedTime = 0;
            }
        }

        if (currentState != State.WALKING) {
            previousState = currentState;
            currentState = State.WALKING;
            elapsedTime = 0;
        }
    }

    public void turn() {
        if (currentState == State.WALKING) {
            if (walking.isAnimationFinished(elapsedTime)) {
                previousState = currentState;
                currentState = State.TURNING;
                elapsedTime = 0;
                if(facingRight)
                    facingRight = false;
                else facingRight = true;
            }
        } else if (currentState == State.IDLE) {
            previousState = currentState;
            currentState = State.TURNING;
            elapsedTime = 0;
            if(facingRight)
                facingRight = false;
            else facingRight = true;
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

}
