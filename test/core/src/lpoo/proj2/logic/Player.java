package lpoo.proj2.logic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
        WALKING, DEAD, ATTACKING, DEFENDING, DRAW_SWORD, SHEATHE_SWORD, SWORD_IDLE, DRINKING, HANGING, CLIMB_JUMP, CLIMBING_UP, DROP, ESCAPING
    }


    public World world; //Box2d physics world
    public Body body;   //Box2d physics body
    private GameScreen screen;  //Screen to which the player belongs

    //Textures and Animations
    private Animation idle;         //Default animation used for when there are no actions
    private Animation falling;      //Animation used when the player is falling
    private Animation fall_death;   //Animation used when the player is dead after falling
    private Animation running;      //Animation used when the player is running
    private Animation start_run;    //Animation used when the player starts running
    private Animation stop_run;     //Animation used when the player stops
    private Animation running_jump; //Animation used when the player performs a running jump
    private Animation walking;      //Animation used when the player is walking
    private Animation long_jump;    //Animation used when the player performs a standing long jump
    private Animation turn;         //Animation used when the player turns around
    private Animation run_turn;     //Animation used when the player turns around while running
    private Animation climb_jump;   //Animation used when the player jumps to climb a ledge
    private Animation hanging;      //Animation used when the player is hanging from a ledge
    private Animation climb_up;     //Animation used when the player climbs up from a ledge

    //States
    private float elapsedTime;                  //Time spent in the current state
    private State currentState;                 //Current player state
    private State previousState;                //Previous player state
    private Animation currentAnimation;         //Current player Animation
    private boolean facingRight;                //True if the player sprite is facing right
    private static final float maxVelocity = 4; //Maximum velocity for the player movement
    private float yVel;                         //Velocity for the player along the Y axis
    private float maxyVel;                      //Maximum velocity recorded between non-zero Y velocities
    private float prevyVel;                     //Velocity for the Y axis recorded before yVel

    //Logic variables
    private boolean alive;                      //True if the player is alive
    private boolean hasKey;                     //True if the player is in posession of the key

    /**
     * Constructor for the Player class
     * @param screen Screen in which the player is drawn
     *
     *               Initializes all the variables and loads the animations, also sets the initial position
     */
    public Player(GameScreen screen) {
        super(screen.getTextures().findRegion("playersprites"));
        this.world = screen.getWorld();
        this.screen = screen;
        alive = true;
        hasKey = false;
        definePlayer();

        //Initial status
        facingRight = true;
        currentState = State.IDLE;
        previousState = null;
        yVel = 0;
        maxyVel = 0;
        prevyVel = 0;


        Array<TextureRegion> frames = new Array<TextureRegion>();

        //idle sprite
        frames.add(new TextureRegion(getTexture(), 0, 350, 18, 40));
        idle = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        currentAnimation = idle;

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

        //running turn animations
        for (int i = 0; i < 11; i++) {
            frames.add(new TextureRegion(getTexture(), i * 33, 350, 33, 40));
        }
        run_turn = new Animation(0.1f, frames);
        frames.clear();

        //jump to climb animation
        for (int i = 0; i < 12; i++) {
            frames.add(new TextureRegion(getTexture(), i * 20, 450, 20, 48));
        }
        climb_jump = new Animation(0.1f, frames);
        frames.clear();

        //hanging animation
        for (int i = 0; i < 13; i++) {
            frames.add(new TextureRegion(getTexture(), i * 26, 500, 26, 45));
        }
        hanging = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        //climb up animation
        for (int i = 0; i < 12; i++) {
            frames.add(new TextureRegion(getTexture(), i * 30, 550, 30, 83));
        }
        climb_up = new Animation(0.1f, frames);
        frames.clear();


        //falling animation
        frames.add(new TextureRegion(getTexture(), 0, 650, 17, 36));
        falling = new Animation(0.1f,frames, Animation.PlayMode.LOOP);
        frames.clear();

        //death by fall damage animation
        frames.add(new TextureRegion(getTexture(), 0, 650, 17, 36));
        fall_death = new Animation(0.1f,frames, Animation.PlayMode.LOOP);
        frames.clear();

        setPosition(1400 * 6f + 200, 700 * 2f + 700);
        elapsedTime = 0;
    }

    /**
     * Defines the player representation in the box2d physics world
     * and sets its initial position
     *
     */
    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((lpooGame.WIDTH*6f+20)/lpooGame.PPM, (700*2f + 450) / lpooGame.PPM);
        bdef.position.set(400 / lpooGame.PPM, (lpooGame.HEIGHT + 450) / lpooGame.PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape cshape = new PolygonShape();
        cshape.setAsBox(18 / 2 * 2.5f / lpooGame.PPM, 40 / 2 * 2.5f / lpooGame.PPM);//(40/2) / lpooGame.PPM,(40/2)/lpooGame.PPM);

        fdef.shape = cshape;
        fdef.friction = 0.7f;
        body.createFixture(fdef).setUserData("player");
    }

    /**
     * Updates all the info of the player, the camera and its movement according to the state it is currently in.
     *
     * @param dt time since last update
     */
    public void update(float dt) {

        int direction = facingRight ? 1 : -1;


        prevyVel = yVel;
        yVel = body.getLinearVelocity().y;
        maxyVel = yVel > maxyVel ? yVel : maxyVel;


        //Player is falling
        if (yVel < -3f && currentState != State.FALLING && currentState != State.LONG_JUMP) {
            changeState(State.FALLING);
            setCurrentAnimation(falling);
        }

        else if (yVel < -4f && currentState != State.FALLING ) {
            changeState(State.FALLING);
            setCurrentAnimation(falling);
        }

        //Player just ended a fall, checks if its velocity was fast enough to kill him
        if(yVel == 0f && currentState == State.FALLING){
            if(prevyVel > 10){
                setCurrentAnimation(fall_death);
                isKilled();
            }

            //if not, handle it like any other stop in motion
            else stop();
        }

        //Sets the new position of the box2d body to follow the player sprite
        if (currentAnimation == climb_up) {
            setPosition(facingRight ? (body.getPosition().x) : body.getPosition().x - getWidth(), (body.getPosition().y - getHeight() / 4));
        } else
            setPosition((body.getPosition().x - getWidth() / 2), (body.getPosition().y - getHeight() / 2));
        setRegion(getFrame(dt));


        //Checks if the player has left the view of the camera and, if so, snaps it to a new position
        if (body.getPosition().y < screen.getCam().position.y - screen.getCam().viewportHeight / 2) {
            screen.switchCamera(GameScreen.Switch.DOWN);
        }
        if (body.getPosition().y > screen.getCam().position.y + screen.getCam().viewportHeight / 2) {
            screen.switchCamera(GameScreen.Switch.UP);
        }
        if (body.getPosition().x < screen.getCam().position.x - screen.getCam().viewportWidth / 2) {
            screen.switchCamera(GameScreen.Switch.LEFT);
        }
        if (body.getPosition().x > screen.getCam().position.x + screen.getCam().viewportWidth / 2) {
            screen.switchCamera(GameScreen.Switch.RIGHT);
        }

        //Checks the state of the player and moves it accordingly
        switch (currentState) {
            case START_RUN:
                body.applyForceToCenter(direction * 400f / lpooGame.PPM, 0f, true);
                break;

            case RUNNING:
                if ((body.getLinearVelocity().x <= maxVelocity && facingRight) || (body.getLinearVelocity().x >= -maxVelocity && !facingRight))
                    body.applyForceToCenter(direction * 500f / lpooGame.PPM, 0f, true);
                break;

            case STOP:
                if (stop_run.isAnimationFinished(elapsedTime) ) {
                }
                else body.applyForceToCenter(-body.getLinearVelocity().x * stop_run.getAnimationDuration() * lpooGame.PPM, 0, true); // acceleration needed to stop the body
                break;

            case RUN_JUMP:
                if(elapsedTime < 0.3)
                    body.applyForceToCenter(direction * 500f / lpooGame.PPM, 750f / lpooGame.PPM, true);

                else if (elapsedTime >= 0.3 && elapsedTime < 0.9) {
                    body.applyForceToCenter(direction * 700f / lpooGame.PPM, 750f / lpooGame.PPM, true);
                }

                else
                    body.applyForceToCenter(direction * -6 * 700f / lpooGame.PPM, -6 * 750f / lpooGame.PPM, true);
                break;

            case WALKING:
                if (elapsedTime <= 0.2f || elapsedTime >= 0.5) {
                } else body.applyForceToCenter(direction * 500f / lpooGame.PPM, 0f, true);

                break;

            case LONG_JUMP:
                if (elapsedTime <= 0.4f) {//Sprite has not yet jumped
                } else if (elapsedTime >= 0.8f) {
                    body.applyForceToCenter(direction * -1750f / lpooGame.PPM, -1000f / lpooGame.PPM, true);
                } else body.applyForceToCenter(direction * 2500f / lpooGame.PPM, 1000f / lpooGame.PPM, true);
                if(long_jump.isAnimationFinished(elapsedTime))
                    stop();
                break;

            case IDLE:
                break;

            case TURNING_RUN:
                while (!run_turn.isAnimationFinished(elapsedTime)) {
                    body.applyForceToCenter(direction * -10f / lpooGame.PPM, 0, true);
                }
                break;

            case CLIMB_JUMP:
                if (elapsedTime >= 0.7 && elapsedTime <= 0.8f)
                    body.applyForceToCenter(0, 4200f / lpooGame.PPM, true);
                break;

            case HANGING:
                body.applyForceToCenter(0, -1 * world.getGravity().y, true);
                break;

            case CLIMBING_UP:
                body.applyForceToCenter(0, -2 * world.getGravity().y, true);
                break;
        }

        System.out.println(currentState);
    }

    /**
     * Changes the player state to RUN or START_RUN, according to its current state
     */
    public void run() {
        if (currentState == State.IDLE) {
            changeState(State.START_RUN);
            setCurrentAnimation(start_run);
        } else if (currentState == State.START_RUN) { //Player had started running, go into the run cycle
            if (elapsedTime >= 0.6f) {
                changeState(State.RUNNING);
                setCurrentAnimation(running);
            }
        } else { //Can be called for the jumps
            if (currentAnimation.isAnimationFinished(elapsedTime)) {
                setCurrentAnimation(running);
                changeState(State.RUNNING);
            }
        }
    }


    /**
     * Handles a stop in motion, acts according to the player state, with a running stop if the player was in quick motion, and a simple change
     * to idle if not
     */
    public void stop() {

        switch (currentState) {
            case START_RUN:
                if (elapsedTime >= 0.6f) {
                    setCurrentAnimation(stop_run);
                    changeState(State.STOP);
                }
                break;

            case RUNNING:
                setCurrentAnimation(stop_run);
                changeState(State.STOP);
                break;

            case RUN_JUMP:
                if (elapsedTime >= 0.9) {
                    setCurrentAnimation(stop_run);
                    changeState(State.STOP);
                }
                break;

            case LONG_JUMP:
                if (long_jump.isAnimationFinished(elapsedTime)) {
                    setCurrentAnimation(stop_run);
                    changeState(State.STOP);
                }
                break;

            case TURNING_RUN:
                if (run_turn.isAnimationFinished(elapsedTime)) {
                    setCurrentAnimation(stop_run);
                    changeState(State.STOP);
                    //body.setLinearVelocity(0, 0);
                }
                break;

            case HANGING:
                body.applyForceToCenter(0,-1 * world.getGravity().y,true);
                break;

            case CLIMBING_UP:
                if(climb_up.isAnimationFinished(elapsedTime)){
                    body.setTransform(getX()+getWidth()/2,getY()+getHeight(),0);
                    changeState(State.IDLE);
                    setCurrentAnimation(idle);
                }
                break;

            default:
                if (currentAnimation == null || currentAnimation.isAnimationFinished(elapsedTime)) {
                    changeState(State.IDLE);
                    setCurrentAnimation(idle);
                }
                break;
        }
    }

    /**
     * Function used to define the current sprite for the player
     * @param dt time since last update
     * @return the image of the current sprite
     *
     * Flips the image horizontally if needed (player facing left)
     */
    public TextureRegion getFrame(float dt) {

        TextureRegion frame = idle.getKeyFrame(0);


        if (elapsedTime >= currentAnimation.getAnimationDuration())
            elapsedTime = 0;
        frame = currentAnimation.getKeyFrame(elapsedTime);

        if (!facingRight) {
            elapsedTime += dt;
            TextureRegion flipped = new TextureRegion(frame);
            flipped.flip(true, false);
            return flipped;
        }

        elapsedTime += dt;
        return frame;

    }

    /**
     * Function used to make the player jump
     * goes into a standing long jump if the player is in a walking state and into a running jump if the player is running
     */
    public void jump() {
        if (currentState == State.RUNNING) {
            changeState(State.RUN_JUMP);
            setCurrentAnimation(running_jump);
        } else if (currentState == State.WALKING) {
            // if (walking.isAnimationFinished(elapsedTime)) {
            changeState(State.LONG_JUMP);
            setCurrentAnimation(long_jump);
            //}
        } else if (currentState == State.IDLE) {
            changeState(State.LONG_JUMP);
            setCurrentAnimation(long_jump);
        }
    }

    /**
     * Handles movement when the hud "WALK" switch is activated
     */
    public void walk() {

        if (currentState == State.TURNING) {
            if (turn.isAnimationFinished(elapsedTime)) {
                changeState(State.WALKING);
                setCurrentAnimation(walking);
            }
        } else if (currentState == State.WALKING) {
            changeState(State.WALKING);
            setCurrentAnimation(walking);
        } else if (currentState == State.IDLE) {
            changeState(State.WALKING);
            setCurrentAnimation(walking);
        }
    }

    /**
     * Used to trigger the "turn around" animation and update the facingRight boolean
     */
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
        } else if (currentState == State.RUNNING) {
            changeState(State.TURNING_RUN);
            setCurrentAnimation(run_turn);
            facingRight = !facingRight;
        } else if (currentState == State.RUN_JUMP) {
            if (running_jump.isAnimationFinished(elapsedTime)) {
                changeState(State.TURNING_RUN);
                setCurrentAnimation(run_turn);
                facingRight = !facingRight;
            }
        }
    }

    /**
     * Used to make the player climb ledges, goes into a jumping animation if the player was on the ground, and into a climbing up animation if the player was hanging
     */
    public void climb() {
        if (currentState == State.IDLE || currentState == State.WALKING) {
            changeState(State.CLIMB_JUMP);
            setCurrentAnimation(climb_jump);
        }

        if (currentState == State.HANGING) {
            changeState(State.CLIMBING_UP);
            setCurrentAnimation(climb_up);
        }
    }

    /**
     * Used to snap the player to a ledge and trigger a hanging animation, is called by the contact with a Climbable Tilemap tile
     */
    public void hang() {
        if (currentState != State.HANGING && previousState != State.HANGING) {
            changeState(State.HANGING);
            setCurrentAnimation(hanging);
        }
    }

    //TODO remove this
    public State getPreviousState() {
        return previousState;
    }

    /**
     * Returns the current player state
     * @return the current player state
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Returns the side the player is facing
     * @return true if the player is facing right, false if facing left
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * Check if the player is alive
     * @return true if the player is alive, false if dead
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Trigger the death of the player
     */
    public void isKilled() {
        alive = false;
        //TODO die animation
    }

    /**
     * Check if the player has the key
     * @return true if the player has the key, false if not
     */
    public boolean hasKey() {
        return hasKey;
    }

    /**
     * Used when the player picks up the key, updates the corresponding variable
     */
    public void pickUpKey() {
        if (!hasKey) {
            hasKey = true;
            //TODO Pick up key animation
        }
    }

    public void checkExit() {
        if (hasKey) {
            //TODO WIN GAME
        }
    }

    /**
     * Return the box2d physics world
     * @return box2d physics world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Change the state, updates the corresponding variables and resets the state timer
     * @param state state to switch to
     */
    public void changeState(State state) {
        previousState = currentState;
        currentState = state;
        elapsedTime = 0;
    }


    /**
     * Change the animation, updates the corresponding variable and resizes the bounds of the sprite to maintain shape
     * @param animation animation to switch to
     */
    public void setCurrentAnimation(Animation animation) {
        currentAnimation = animation;
        if (animation == climb_up) {
            setBounds((body.getPosition().x + getWidth() / 2f) / lpooGame.PPM, (body.getPosition().y + getHeight() / 2f) / lpooGame.PPM, (animation.getKeyFrame(0).getRegionWidth()) * 2.5f / lpooGame.PPM, (animation.getKeyFrame(0).getRegionHeight()) * 2.5f / lpooGame.PPM);
        } else
            setBounds((body.getPosition().x + getWidth() / 2f) / lpooGame.PPM, (body.getPosition().y + getHeight() / 2f) / lpooGame.PPM, (animation.getKeyFrame(0).getRegionWidth()) * 2.5f / lpooGame.PPM, (animation.getKeyFrame(0).getRegionHeight()) * 2.5f / lpooGame.PPM);
    }
}
