package lpoo.proj2.gui.screen;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import lpoo.proj2.logic.states.GameState;
import lpoo.proj2.lpooGame;

/**
 * Created by Antonio Melo and Edgar Passos on 6/4/16.
 */
public class MainMenuScreen extends  MyScreen{

    private Texture background;
    private SpriteBatch batch;
    private Label title;
    private Label subtitle;

    private Button playButton;
    private Button exitButton;
    private Button highScores;

    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera cam;

    public MainMenuScreen(lpooGame game){
        super(game);
        Table table = new Table();

        if(lpooGame.music != null) lpooGame.music.stop();
        lpooGame.music = Gdx.audio.newMusic(Gdx.files.internal("music/star_wars.mp3"));
        lpooGame.music.play();
        lpooGame.music.setLooping(true);

        background = new Texture("gui/mainmenu_background.jpg");
        batch = new SpriteBatch();

        cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new FitViewport(lpooGame.WIDTH,lpooGame.HEIGHT,cam);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"),false);
        font.setColor(Color.BLACK);

        TextureRegionDrawable buttonImg = new TextureRegionDrawable(new TextureRegion(new Texture("menu_button.png")));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(buttonImg,buttonImg,buttonImg,font);

        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("font_title.fnt"),false);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont,Color.GOLD);


        playButton = new TextButton("PLAY",style);
        exitButton = new TextButton("exit",style);
        highScores = new TextButton("highscores",style);
        title = new Label("STAR WARS",titleStyle);
        subtitle = new Label("inside THE DEATH STAR",titleStyle);




        final lpooGame g = game;
        InputListener playListener = new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                g.gsm.push(new GameState(new GameScreen(g),g.gsm));
            }
        };
        InputListener highscoreListener = new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               g.gsm.push(new GameState(new HighScoresScreen(g),g.gsm));
            }
        };

        playButton.addListener(playListener);
        highScores.addListener(highscoreListener);

        stage.addActor(table);

        table.setFillParent(true);
        table.setDebug(false);
        table.add(title).expandX().align(Align.center).colspan(3);
        table.row();
        table.add(subtitle).align(Align.center).expand().colspan(3);
        table.row();
        table.add(exitButton).align(Align.center);
        table.add(highScores).align(Align.center);
        table.add(playButton).align(Align.center);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.begin();
        batch.draw(background,0,0,Gdx.app.getGraphics().getWidth(),Gdx.app.getGraphics().getHeight());
        batch.end();

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

    @Override
    public void update(float delta) {
        Gdx.input.setInputProcessor(stage);
        stage.act();
        handleInput();
    }

    @Override
    public void handleInput() {
        if(playButton.isPressed()){
            game.gsm.set(new GameState(new GameScreen(game),game.gsm));
        }

        if(exitButton.isPressed()){
            Gdx.app.exit();
        }

    }
}
