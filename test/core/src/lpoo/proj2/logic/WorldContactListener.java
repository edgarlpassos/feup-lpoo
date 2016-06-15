package lpoo.proj2.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;



/**
 * World contact listener
 */
public class WorldContactListener implements ContactListener {
    /**
     * Player
     */
    private Player p;

    /**
     * WorldContactListener constructor
     * @param p Player
     */
    public WorldContactListener(Player p) {
        this.p = p;
    }

    /**
     * Checks player contact with other objects
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "player" || fixB.getUserData() == "player") {
            Fixture player = fixA.getUserData() == "player" ? fixA : fixB;
            Fixture obj = player == fixA ? fixB : fixA;

            if (obj.getUserData() == "lava") {
                p.isKilled();
            }
            if (obj.getUserData() == "door") {
                p.checkExit();
            }
            if (obj.getUserData() == "key") {
                p.pickUpKey();
            }

            if (obj.getUserData() == "climbable"){
                if(p.getCurrentState() == Player.State.CLIMB_JUMP || p.getCurrentState() == Player.State.RUN_JUMP || p.getCurrentState() == Player.State.LONG_JUMP || p.getCurrentState() == Player.State.FALLING)
                    p.hang();
            }

        }
    }

    /**
     * EndContact
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {

    }

    /**
     * PreSolve
     * @param contact
     * @param oldManifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    /**
     * PostSolve
     * @param contact
     * @param impulse
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
