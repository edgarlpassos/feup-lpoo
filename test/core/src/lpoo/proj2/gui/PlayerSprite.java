package lpoo.proj2.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import lpoo.proj2.gui.screen.GameScreen;
import lpoo.proj2.logic.Player;
import lpoo.proj2.lpooGame;

/**
 * Created by epassos on 6/7/16.
 */
public class PlayerSprite extends Sprite {

    /**
     * Screen to which the player belongs
     */
    private GameScreen screen;

    //Textures and Animations

    /**
     * Default animation used for when there are no actions
     */
    private Animation idle;

    /**
     * Animation used when the player is falling
     */
    private Animation falling;

    /**
     * Animation used when the player is dead after falling
     */
    private Animation fall_death;

    /**
     * Animation used when the player is running
     */
    private Animation running;

    /**
     * Animation used when the player starts running
     */
    private Animation start_run;

    /**
     * Animation used when the player stops
     */
    private Animation stop_run;

    /**
     * Animation used when the player performs a running jump
     */
    private Animation running_jump;

    /**
     * Animation used when the player is walking
     */
    private Animation walking;

    /**
     * Animation used when the player performs a standing long jump
     */
    private Animation long_jump;

    /**
     * Animation used when the player turns around
     */
    private Animation turn;

    /**
     * Animation used when the player turns around while running
     */
    private Animation run_turn;

    /**
     * Animation used when the player jumps to climb a ledge
     */
    private Animation climb_jump;

    /**
     * Animation used when the player is hanging from a ledge
     */
    private Animation hanging;

    /**
     * Animation used when the player climbs up from a ledge
     */
    private Animation climb_up;

    /**
     * Current player Animation
     */
    private Animation currentAnimation;


    /**
     * Time in the current animation, equivalent to the timer in the player state
     */
    private float currAnimationTime;

    /**
     * Player the sprite represents
     */
    private Player player;

    public PlayerSprite(GameScreen screen, Player player) {
        super(screen.getTextures().findRegion("playersprites"));
        defineAnimations();
        this.screen = screen;
        this.player = player;
        currAnimationTime = 0;
    }


    public void defineAnimations() {
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
        falling = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        //death by fall damage animation
        frames.add(new TextureRegion(getTexture(), 0, 650, 17, 36));
        fall_death = new Animation(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        setPosition(1400 * 6f + 200, 700 * 2f + 700);
    }

    /**
     * Change the animation, updates the corresponding variable and resizes the bounds of the sprite to maintain shape
     *
     * @param state state that dictates the animation to switch to
     */
    public void setCurrentAnimation(Player.State state) {

        switch (state) {
            case IDLE:
                currentAnimation = idle;
                break;

            case START_RUN:
                currentAnimation = start_run;
                break;

            case STOP:
                currentAnimation = stop_run;
                break;

            case WALKING:
                currentAnimation = walking;
                break;

            case RUN_JUMP:
                currentAnimation = running_jump;
                break;

            case LONG_JUMP:
                currentAnimation = long_jump;
                break;

            case HANGING:
                currentAnimation = hanging;
                break;

            case CLIMB_JUMP:
                currentAnimation = climb_jump;
                break;

            case CLIMBING_UP:
                currentAnimation = climb_up;
                break;

            case TURNING_RUN:
                currentAnimation = run_turn;
                break;

            case TURNING:
                currentAnimation = turn;
                break;

            case FALLING:
                currentAnimation = falling;
                break;

            case DEAD:
                if (currentAnimation == falling)
                    currentAnimation = fall_death;
                break;


        }

        if (currentAnimation == climb_up) {
            setBounds((player.body.getPosition().x + getWidth() / 2f) / lpooGame.PPM, (player.body.getPosition().y + getHeight() / 2f) / lpooGame.PPM, (currentAnimation.getKeyFrame(0).getRegionWidth()) * 2.5f / lpooGame.PPM, (currentAnimation.getKeyFrame(0).getRegionHeight()) * 2.5f / lpooGame.PPM);
        } else
            setBounds((player.body.getPosition().x + getWidth() / 2f) / lpooGame.PPM, (player.body.getPosition().y + getHeight() / 2f) / lpooGame.PPM, (currentAnimation.getKeyFrame(0).getRegionWidth()) * 2.5f / lpooGame.PPM, (currentAnimation.getKeyFrame(0).getRegionHeight()) * 2.5f / lpooGame.PPM);
    }


    /**
     * Function used to define the current sprite for the player
     *
     * @param dt time since last update
     * @return the image of the current sprite
     * <p/>
     * Flips the image horizontally if needed (player facing left)
     */
    public TextureRegion getFrame(float dt) {

        TextureRegion frame = idle.getKeyFrame(0);
        currAnimationTime += dt;

       // System.out.println("anim time" + currAnimationTime);

        if (currAnimationTime >= currentAnimation.getAnimationDuration())
            currAnimationTime = 0f;

        frame = currentAnimation.getKeyFrame(currAnimationTime);

        if (!player.isFacingRight()) {
            TextureRegion flipped = new TextureRegion(frame);
            flipped.flip(true, false);
            return flipped;
        }

        return frame;


    }

    public void update (float dt){

        //Sets the new position of the player sprite to follow the box2d body
        if (currentAnimation == climb_up) {
            setPosition(player.isFacingRight() ? (player.body.getPosition().x) : player.body.getPosition().x - getWidth(), (player.body.getPosition().y - getHeight() / 4));
        } else
            setPosition((player.body.getPosition().x - getWidth() / 2), (player.body.getPosition().y - getHeight() / 2));

        setRegion(getFrame(dt));

        //Checks if the player has left the view of the camera and, if so, snaps it to a new position
        if (player.body.getPosition().y < screen.getCam().position.y - screen.getCam().viewportHeight / 2) {
            screen.switchCamera(GameScreen.Switch.DOWN);
        }
        if (player.body.getPosition().y > screen.getCam().position.y + screen.getCam().viewportHeight / 2) {
            screen.switchCamera(GameScreen.Switch.UP);
        }
        if (player.body.getPosition().x < screen.getCam().position.x - screen.getCam().viewportWidth / 2) {
            screen.switchCamera(GameScreen.Switch.LEFT);
        }
        if (player.body.getPosition().x > screen.getCam().position.x + screen.getCam().viewportWidth / 2) {
            screen.switchCamera(GameScreen.Switch.RIGHT);
        }
    }


}