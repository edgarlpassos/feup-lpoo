package lpoo.proj2;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lpoo.proj2.gui.screen.MainMenuScreen;
import lpoo.proj2.gui.screen.MyScreen;
import lpoo.proj2.logic.states.GameState;
import lpoo.proj2.logic.states.GameStateManager;

/**
 * main class of the project
 */
public class lpooGame extends Game {


    public static final int WIDTH = 1400;
    public static final int HEIGHT = 700;

    /**
     *  Used for the interaction with the tile, tiles are 70x70 px
     *  PPM = pixels per meter
     */
    public static final float PPM = 70;

    public SpriteBatch batch;
    public GameStateManager gsm;
    public static Music music = null;
    public static float[] scores;
    public static Preferences pref;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        scores = new float[3];
        pref = Gdx.app.getPreferences("highscores");

        if(pref.getString("name","No scores").equals("No scores"))
            inicialScores();
        else
            loadScores();

        //Main Menu creation
        MyScreen menu_screen = new MainMenuScreen(this);
        GameState main_menu = new GameState(menu_screen,gsm);
        gsm.push(main_menu);    //Starts game with main menu
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.render(Gdx.graphics.getDeltaTime());
    }

    public static void inicialScores(){
        for(int i= 0;i < scores.length; i++){
            scores[i] = 0f;
            pref.putFloat("number"+i,scores[i]);
        }
        pref.putString("name","lpoogame");
        pref.flush();
    }

    public static void loadScores(){
        for(int i= 0;i < scores.length; i++){
            scores[i] = pref.getFloat("number"+i);
        }
    }

    public static void saveScores(){
        for(int i= 0;i < scores.length; i++) {
            pref.putFloat("number" + i, scores[i]);
        }
        pref.flush();
    }

    public static void insertScore(float score){
        if(score < scores[0] || scores[0] == 0f){
            if(score < scores[1] || scores[1] == 0f){
                scores[0] = scores[1];
                if(score < scores[2] || scores[2] == 0f){
                    scores[1] = scores[2];
                    scores[2] = score;
                }else {
                    scores[1] = score;
                }
            }else{
                scores[0] = score;
            }
        }
        saveScores();
    }
}
