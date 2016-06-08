package lpoo.proj2.logic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import lpoo.proj2.gui.PlayerSprite;
import lpoo.proj2.gui.screen.GameScreen;
import lpoo.proj2.lpooGame;

/**
 * Created by Antonio Melo and Edgar Passos
 */

/**
 * Class used to represent the player character
 */
public class Player {

    public enum State {
        IDLE, START_RUN, RUNNING, STOP, TURNING, TURNING_RUN, RUN_JUMP, LONG_JUMP, FALLING,
        WALKING, DEAD, HANGING, CLIMB_JUMP, CLIMBING_UP
    }


    /**
     * Box2d physics world
     */
    public World world;

    /**
     * Box2d physics body
     */
    public Body body;


    //States

    /**
     * Time spent in the current state
     */
    public float elapsedTime;

    /**
     * Current player state
     */
    private State currentState;

    /**
     *  Previous player state
     */
    private State previousState;


    /**
     * True if the player is facing right
     */
    private boolean facingRight;

    /**
     * Maximum velocity for the horizontal player movement
     */
    private static final float MAX_VELOCITY = 4;

    /**
     * Velocity for the player along the Y axis
     */
    private float yVel;

    /**
     * Maximum velocity recorded during a fall
     */
    private float maxyVel;

    /**
     * Velocity for the Y axis recorded before yVel
     */
    private float prevyVel;

    //Logic variables
    /**
     * True if the player is alive
     */
    private boolean alive;

    /**
     * True if the player has the Key
     */
    private boolean hasKey;
    private boolean escaped;

    /**
     * Sprite that is drawn
     */
    private PlayerSprite playerSprite;

    public static final float STOP_TIME = 0.6f;
    public static final float TURNING_TIME = 0.6f;
    public static final float WALK_TIME = 0.7f;
    public static final float CLIMB_UP_TIME = 1f/2;
    public static final float CLIMB_JUMP_TIME = 1.1f;

    public boolean moving;
    public boolean walking;
//    public static final float

    /**
     * Constructor for the Player class
     * Initializes all the variables and loads the animations, also sets the initial position
     */
    public Player() {
        alive = true;
        hasKey = false;
        escaped = false;
        world = new World(new Vector2(0,-10f),false);
        definePlayer();

        //Initial status
        facingRight = true;
        currentState = State.IDLE;
        previousState = null;
        yVel = 0;
        maxyVel = 0;
        prevyVel = 0;
        elapsedTime = 0;
        playerSprite = null;//run();
        moving = false;
        walking = false;
    }

    /**
     * Defines the player representation in the box2d physics world
     * and sets its initial position
     *
     */
    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((lpooGame.WIDTH*6f+8*75)/lpooGame.PPM, (700*2f + 8*75) / lpooGame.PPM);
        //bdef.position.set(400 / lpooGame.PPM, (lpooGame.HEIGHT + 450) / lpooGame.PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape cshape = new PolygonShape();
        cshape.setAsBox(18 / 2 * 2.5f / lpooGame.PPM, (40 / 2) * 2.5f / lpooGame.PPM);//(40/2) / lpooGame.PPM,(40/2)/lpooGame.PPM);

        fdef.shape = cshape;
        fdef.friction = 0.7f;
        body.createFixture(fdef).setUserData("player");
    }

    public void setPlayerSprite(PlayerSprite sprite){
        //System.out.println(currentState);
        this.playerSprite = sprite;
        playerSprite.setCurrentAnimation(currentState);
    }

    /**
     * Updates all the info of the player, the camera and its movement according to the state it is currently in.
     *
     * @param dt time since last update
     */
    public void update(float dt) {

        int direction = facingRight ? 1 : -1;
        elapsedTime+=dt;
        playerSprite.update(dt);

        if(moving){
         if(walking)
             walk();
         else run();
        }
        else stop();


        prevyVel = yVel;
        yVel = body.getLinearVelocity().y;
        maxyVel = yVel > maxyVel ? yVel : maxyVel;


        //Player is falling
        if (yVel < -3f && currentState != State.FALLING && currentState != State.LONG_JUMP) {
            changeState(State.FALLING);
        }

        else if (yVel < -4f && currentState != State.FALLING ) {
            changeState(State.FALLING);
        }

        //Player just ended a fall, checks if its velocity was fast enough to kill him
        if(yVel == 0f && currentState == State.FALLING){
            if(prevyVel > 10){
                changeState(State.DEAD);
                isKilled();
            }

            //if not, handle it like any other stop in motion
            else {
                stop();
                return;
            }
        }



        //Checks the state of the player and moves it accordingly
        switch (currentState) {
            case START_RUN:
                body.applyForceToCenter(direction * 400f / lpooGame.PPM, 0f, true);
                break;

            case RUNNING:
                if ((body.getLinearVelocity().x <= MAX_VELOCITY && facingRight) || (body.getLinearVelocity().x >= -MAX_VELOCITY && !facingRight))
                    body.applyForceToCenter(direction * 500f / lpooGame.PPM, 0f, true);
                break;

            case STOP:
                if (elapsedTime > STOP_TIME ) {
                }
                else body.applyForceToCenter(-body.getLinearVelocity().x * STOP_TIME * lpooGame.PPM, 0, true); // acceleration needed to stop the body
                break;

            case RUN_JUMP:
                if(elapsedTime < 0.3)
                    body.applyForceToCenter(direction * 500f / lpooGame.PPM, 750f / lpooGame.PPM, true);

                else if (elapsedTime >= 0.3 && elapsedTime < 0.9) {
                    body.applyForceToCenter(direction * 700f / lpooGame.PPM, 750f / lpooGame.PPM, true);
                }

                else if (elapsedTime < 0.9f)
                    body.applyForceToCenter(direction * -6 * 700f / lpooGame.PPM, -6 * 750f / lpooGame.PPM, true);
                else {
                    changeState(State.RUNNING);
                }
                break;

            case WALKING:
                if (elapsedTime > 0.2f || elapsedTime < 0.5)
                    body.applyForceToCenter(direction * 500f / lpooGame.PPM, 0f, true);

                break;

            case LONG_JUMP:
                if (elapsedTime <= 0.4f) {//Sprite has not yet jumped
                } else if (elapsedTime >= 0.8f) {
                    body.applyForceToCenter(direction * -1750f / lpooGame.PPM, -1000f / lpooGame.PPM, true);
                } else body.applyForceToCenter(direction * 2500f / lpooGame.PPM, 1000f / lpooGame.PPM, true);

                if(elapsedTime > 1.3f)
                    stop();
                break;

            case IDLE:
                break;

            case TURNING_RUN:
                while (elapsedTime <= 1f) {
                    body.applyForceToCenter(direction * -10f / lpooGame.PPM, 0, true);
                }
                break;

            case CLIMB_JUMP:
                if (elapsedTime >= 0.7 && elapsedTime <= 0.8f)
                    body.applyForceToCenter(0, 4500f / lpooGame.PPM, true);
                break;

            case HANGING:
                body.applyForceToCenter(0, -1 * world.getGravity().y, true);
                break;

            case CLIMBING_UP:
                body.applyForceToCenter(0, -2 * world.getGravity().y, true);
                break;

            case FALLING:
                break;

            case TURNING:
                if(elapsedTime > TURNING_TIME)
                    changeState(State.IDLE);
                break;
        }
    }

    /**
     * Changes the player state to RUN or START_RUN, according to its current state
     */
    public void run() {
        if (currentState == State.IDLE) {
            changeState(State.START_RUN);
        } else if (currentState == State.START_RUN) { //Player had started running, go into the run cycle
            if (elapsedTime >= 0.6f) {
                changeState(State.RUNNING);
            }
        } else if (currentState == State.RUN_JUMP) { //Can be called for the jumps
            if (elapsedTime > 0.9f) {
                changeState(State.RUNNING);
            }
        } else if (currentState == State.LONG_JUMP) {
            if (elapsedTime > 0.9f) {
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
                    changeState(State.STOP);
                }
                break;

            case WALKING:
                if(elapsedTime >= WALK_TIME) {
                    changeState(State.IDLE);
                    body.setLinearVelocity(0, 0);
                }
                break;

            case STOP:
                if (elapsedTime >= STOP_TIME)
                    changeState(State.IDLE);
                break;

            case RUNNING:
                if (elapsedTime >= STOP_TIME)
                changeState(State.STOP);
                break;

            case RUN_JUMP:
                if (elapsedTime >= 0.9f) {
                    changeState(State.STOP);
                }
                break;

            case LONG_JUMP:
                if (elapsedTime >= 1.3f) {
                    changeState(State.STOP);
                }
                break;

            case TURNING_RUN:
                if (elapsedTime >= TURNING_TIME) {
                    changeState(State.STOP);
                }
                break;

            case HANGING:
                body.applyForceToCenter(0,-1 * world.getGravity().y,true);
                break;

            case CLIMBING_UP:
                if(elapsedTime >= CLIMB_UP_TIME){
                    body.setTransform(body.getPosition().x+ (facingRight ? 1 : -1) * 0.5f, body.getPosition().y + 2,0);//playerSprite.getX()+playerSprite.getWidth()/2,playerSprite.getY()+playerSprite.getHeight(),0);
                    changeState(State.IDLE);
                }
                break;

            case CLIMB_JUMP:
                if(elapsedTime >= CLIMB_JUMP_TIME)
                    changeState(State.IDLE);
                break;

            case FALLING:
                if(yVel == 0)
                    changeState(State.IDLE);
                break;

            case IDLE:
                break;

            case TURNING:
                if(elapsedTime > TURNING_TIME)
                    changeState(State.IDLE);
                break;

            default:
                changeState(State.IDLE);
                break;
        }
    }



    /**
     * Function used to make the player jump
     * goes into a standing long jump if the player is in a walking state and into a running jump if the player is running
     */
    public void jump() {
        if (currentState == State.RUNNING) {
            changeState(State.RUN_JUMP);
            return;
        } else if (currentState == State.WALKING) {
            changeState(State.LONG_JUMP);
            return;
        }

        else if (currentState == State.IDLE ) {
            changeState(State.CLIMB_JUMP);
        }

       else if (currentState == State.HANGING) {
            changeState(State.CLIMBING_UP);
        }

        //System.out.println("Jump animation");
    }

    /**
     * Handles movement when the hud "WALK" switch is activated
     */
    public void walk() {

        if (currentState == State.TURNING) {
            if (elapsedTime >= 1f) {
                changeState(State.WALKING);
            }
        } else if (currentState == State.WALKING) {
            if (elapsedTime > 0.2f && elapsedTime < 0.5)
                body.applyForceToCenter(facingRight ? 1 : -1 * 200f / lpooGame.PPM, 0f, true);
            else if (elapsedTime > WALK_TIME)
                stop();
        } else if (currentState == State.IDLE) {
            changeState(State.WALKING);
        }
    }

    /**
     * Used to trigger the "turn around" animation and update the facingRight boolean
     */
    public void turn() {
        if (currentState == State.WALKING) {
            if (elapsedTime >= 0.6f) {
                changeState(State.TURNING);
                facingRight = !facingRight;
            }
        } else if (currentState == State.IDLE) {
            changeState(State.TURNING);
            facingRight = !facingRight;
        } else if (currentState == State.RUNNING) {
            changeState(State.TURNING_RUN);
            facingRight = !facingRight;
        } else if (currentState == State.RUN_JUMP) {
            if (elapsedTime >= 0.9f) {
                changeState(State.TURNING_RUN);
                facingRight = !facingRight;
            }
        } else if (currentState == State.TURNING){
            if(elapsedTime > TURNING_TIME){
                changeState(State.IDLE);
                body.setLinearVelocity(0,0);
            }

        }
    }


    /**
     * Used to snap the player to a ledge and trigger a hanging animation, is called by the contact with a Climbable Tilemap tile
     */
    public void hang() {
        //System.out.println("HANG");
        if (currentState != State.HANGING && previousState != State.HANGING) {
            changeState(State.HANGING);
            body.setLinearVelocity(0,0);
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
    public boolean asEscaped(){return escaped;}

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

    public void checkExit(){
        if(hasKey){
            escaped = true;
            //TODO WIN GAME ANIMATION / CALL WIN MENU
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
        //System.out.println(state);
        previousState = currentState;
        currentState = state;
        elapsedTime = 0;

        if(playerSprite!=null)
            playerSprite.setCurrentAnimation(state);
    }


    public void draw(SpriteBatch batch){
        if (playerSprite != null)
            playerSprite.draw(batch);
    }
}
