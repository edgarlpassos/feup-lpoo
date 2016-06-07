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
public class HighScoresScreen extends MyScreen{

    private Viewport viewport;
    private OrthographicCamera cam;
    private Stage stage;
    private Texture background;
    private SpriteBatch batch;

    private Button exitButton;
    private Label menssage;
    private Label score_menssage1;
    private Label score_menssage2;
    private Label score_menssage3;


    public HighScoresScreen(lpooGame game) {
        super(game);

        batch = new SpriteBatch();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new FitViewport(lpooGame.WIDTH,lpooGame.HEIGHT,cam);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        background = new Texture("gui/highscores_background.jpg");

        BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"),false);
        font.setColor(Color.BLACK);

        TextureRegionDrawable buttonImg = new TextureRegionDrawable(new TextureRegion(new Texture("menu_button.png")));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(buttonImg,buttonImg,buttonImg,font);

        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("font_title.fnt"),false);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont,Color.GOLD);

        exitButton = new TextButton("BACK",style);
        menssage = new Label("high scores",titleStyle);
        score_menssage1 = new Label(Float.toString(lpooGame.scores[2]),titleStyle);
        score_menssage2 = new Label(Float.toString(lpooGame.scores[1]),titleStyle);
        score_menssage3 = new Label(Float.toString(lpooGame.scores[0]),titleStyle);


        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        table.add(menssage).expandX().align(Align.center).colspan(1);
        table.row();
        table.add(score_menssage1).expandX().align(Align.center).expandY();
        table.row();
        table.add(score_menssage2).expandX().align(Align.center).expandY();
        table.row();
        table.add(score_menssage3).expandX().align(Align.center).expandY();
        table.row().expandY();
        table.add(exitButton).align(Align.center).align(Align.bottom);

        stage.addActor(table);

    }

    @Override
    public void update(float delta) {
        stage.act();
        handleInput();
    }

    @Override
    public void handleInput() {

        if(exitButton.isPressed()){
            game.gsm.pop();
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
        stage.dispose();
        background.dispose();
    }
}
