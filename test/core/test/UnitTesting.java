import com.badlogic.gdx.Game;

import org.junit.Test;
import org.junit.runner.RunWith;

import lpoo.proj2.gui.screen.GameScreen;
import lpoo.proj2.lpooGame;

import static org.junit.Assert.assertTrue;

/**
 * Created by epassos on 6/7/16.
 */

@RunWith(GdxTestRunner.class)
public class UnitTesting {

    lpooGame game;
    GameScreen screen;

    public UnitTesting(lpooGame game){
        this.game = game;
        screen = new GameScreen(game,"Map/Test_map.tmx");
    }

    @Test
    public void testTest() {
        assertTrue(true);
    }
}
