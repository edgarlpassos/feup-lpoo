package lpoo.proj2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lpoo.proj2.lpooGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = lpooGame.WIDTH;
		config.height = lpooGame.HEIGHT;
		new LwjglApplication(new lpooGame(), config);
	}
}
