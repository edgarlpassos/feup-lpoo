package lpoo.proj2.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * Created by amelo on 6/7/16.
 */
public class EndGameScreen extends  MyScreen{


    private Viewport viewport;
    private OrthographicCamera cam;
    private Stage stage;
    private Texture background;
    private SpriteBatch batch;

    private Button playAgainButton;
    private Button exitButton;
    private Label menssage;

    private boolean win;

    public EndGameScreen(lpooGame game,boolean win) {
        super(game);

        this.win = win;
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


        if(win){
            lpooGame.music.stop();
            lpooGame.music = Gdx.audio.newMusic(Gdx.files.internal("music/maytheforcebewithyou.mp3"));
            lpooGame.music.play();
            lpooGame.music.setLooping(true);

            background = new Texture("gui/win.jpg");
            menssage = new Label("win",titleStyle);
        }else{
            lpooGame.music.stop();
            lpooGame.music = Gdx.audio.newMusic(Gdx.files.internal("music/burn.mp3"));
            lpooGame.music.play();
            lpooGame.music.setLooping(true);

            background = new Texture("gui/lose.jpg");
            menssage = new Label("lose",titleStyle);
        }


        playAgainButton = new TextButton("PLAY AGAIN",style);
        exitButton = new TextButton("BACK",style);


        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        table.add(menssage).expandX().align(Align.center).colspan(3);
        table.row();
        table.add(exitButton).align(Align.center);
        table.add();
        table.add(playAgainButton).align(Align.center);

        stage.addActor(table);
    }

    @Override
    public void update(float delta) {
        handleInput();
        stage.act();
    }

    @Override
    public void handleInput() {
        if(playAgainButton.isPressed()){
            game.gsm.set(new GameState(new GameScreen(game,"Map/Map.tmx"),game.gsm));
        }

        if(exitButton.isPressed()){
            game.gsm.set(new GameState(new MainMenuScreen(game),game.gsm));
        }
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
        background.dispose();
        batch.dispose();
        stage.dispose();
    }
}
