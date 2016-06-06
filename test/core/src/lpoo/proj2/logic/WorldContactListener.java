package lpoo.proj2.logic;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Antonio on 05-Jun-16.
 */
public class WorldContactListener implements ContactListener {
    private Player p;

    public WorldContactListener(Player p) {
        this.p = p;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "player" || fixB.getUserData() == "player"){
            Fixture player = fixA.getUserData() == "player" ? fixA :fixB;
            Fixture obj = player == fixA ? fixB : fixA;

            if(obj.getUserData() == "lava"){
                p.isKilled();
            }
            if(obj.getUserData() == "door"){
                p.checkExit();
            }
            if(obj.getUserData() == "key"){
                p.pickUpKey();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
