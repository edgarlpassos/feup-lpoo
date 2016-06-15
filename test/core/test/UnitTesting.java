
import org.junit.Test;
import org.junit.runner.RunWith;

import lpoo.proj2.gui.screen.GameScreen;
import lpoo.proj2.logic.Player;
import lpoo.proj2.lpooGame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import lpoo.proj2.logic.Player;

/**
 * Created by epassos on 6/7/16.
 */

@RunWith(GdxTestRunner.class)
public class UnitTesting {


    @Test
    public void PlayerStateTest() {
        Player player = new Player();

        //Inicial state
        assertEquals(player.getCurrentState(),Player.State.IDLE);

        //Start Running state
        player.run();
        assertEquals(player.getCurrentState(),Player.State.START_RUN);

        //Running
        player.elapsedTime = 5f;
        player.run();
        assertEquals(player.getCurrentState(),Player.State.RUNNING);

        //Stop running
        player.elapsedTime = 7f;
        player.stop();
        assertEquals(player.getCurrentState(),Player.State.STOP);
    }
}
