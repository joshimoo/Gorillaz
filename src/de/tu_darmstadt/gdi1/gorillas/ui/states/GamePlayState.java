package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.*;

import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.entities.Skyline;
import de.tu_darmstadt.gdi1.gorillas.entities.Sun;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.Gorilla;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import static de.tu_darmstadt.gdi1.gorillas.main.Gorillas.player1;
import static de.tu_darmstadt.gdi1.gorillas.main.Gorillas.player2;


public class GamePlayState extends BasicTWLGameState {

    private Skyline skyline;
    private Gorilla gorilla;  // Best Phun 4eva
    private Gorilla gorillb;  // :D
    private Sun sun;
    private Image background;

    private ValueAdjusterInt if_speed;
    private ValueAdjusterInt if_angle;
    private Button btnThrow;

    @Override
    public int getID() {
        return Gorillas.GAMEPLAYSTATE;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.GAMEPLAY_BACKGROUND);
        skyline = new Skyline(6);

        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 3);

        int xx = x1 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);
        int yy = x2 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);

        gorilla = new Gorilla(xx, Gorillas.FRAME_HEIGHT - skyline.getHeight(x1));
        gorillb = new Gorilla(yy, Gorillas.FRAME_HEIGHT - skyline.getHeight(x2));

        sun = new Sun(400, 60);

        this.createRootPane();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -20, -10);
        sun.render(g);
        skyline.render(g);
        gorilla.render(g);
        gorillb.render(g);

        g.setColor(Color.black);    /* Dropshadow TODO: maybe translucent background */
        g.drawString(player2.getName(), gorillb.x - g.getFont().getWidth(player2.getName()) / 2 + 1, gorillb.y - 63);
        g.drawString(player1.getName(), gorilla.x - g.getFont().getWidth(player1.getName()) / 2 + 1, gorilla.y - 63);

        /* We could possibly change the name-color of the active player as an indication */
        g.setColor(Color.white);
        g.drawString(player2.getName(), gorillb.x - g.getFont().getWidth(player2.getName()) / 2, gorillb.y - 64);
        g.drawString(player1.getName(), gorilla.x - g.getFont().getWidth(player1.getName()) / 2, gorilla.y - 64);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        gorilla.update(delta);
        gorillb.update(delta);

        Input in = gc.getInput();
        if(in.isMousePressed(Input.MOUSE_LEFT_BUTTON))
            skyline.destroy(in.getMouseX(), in.getMouseY(), 64);

        // Keyboardcontroll
        Input in_key = gc.getInput();
        if(in_key.isKeyPressed(Input.KEY_ESCAPE) || in_key.isKeyPressed(Input.KEY_P))
            game.enterState(Gorillas.PAUSESTATE);
        else {
            if (in_key.isKeyPressed(Input.KEY_RETURN) || in_key.isKeyPressed(Input.KEY_SPACE)) throwBanana();
            if (in_key.isKeyPressed(Input.KEY_RIGHT) || in_key.isKeyPressed(Input.KEY_D)) if_speed.setValue(if_speed.getValue() + 5);
            if (in_key.isKeyPressed(Input.KEY_LEFT) || in_key.isKeyPressed(Input.KEY_A)) if_speed.setValue(if_speed.getValue() - 5);
            if (in_key.isKeyPressed(Input.KEY_UP) || in_key.isKeyPressed(Input.KEY_W)) if_angle.setValue(if_angle.getValue() + 5);
            //if (in_key.isKeyDown(Input.KEY_UP)) if_angle.setValue(if_angle.getValue() + 1);
            if (in_key.isKeyPressed(Input.KEY_DOWN) || in_key.isKeyPressed(Input.KEY_S)) if_angle.setValue(if_angle.getValue() - 5);

        }
    }

    @Override
    protected RootPane createRootPane() {
        // Needed for adding the new Input-Elements
        RootPane rp = super.createRootPane();

        if_speed= new ValueAdjusterInt();
        if_angle = new ValueAdjusterInt();
        btnThrow = new Button("Throw");

        // Create Input-Elements Speed and Angle
        Label l_speed = new Label("Speed");
        Label l_angle = new Label("Angle ");


        l_speed.setLabelFor(if_speed);
        // TODO: Set text color WHITE
        //l_speed.setForeground(Color.WHITE)
        l_angle.setLabelFor(if_angle);
        // TODO: Set text color WHITE

        if_speed.setMinMaxValue(0,200);
        if_speed.setValue(100);

        if_angle.setMinMaxValue(0,180);
        if_angle.setValue(120);

        // Wirkungslos
        btnThrow.setAlignment(Alignment.CENTER);

        btnThrow.addCallback(new Runnable() {
            public void run() {
               throwBanana();
            }
        });

        // Set Size and Position of the Input-Elements
        int basic_x=20;
        int basic_y=10;
        int basic_x_c=35;

        /*
        // Everything under each other
        int pos=0;
        l_speed.setSize(100, 20);
        l_speed.setPosition(basic_x, basic_y+basic_x_c*pos);

        if_speed.setSize(100, 25);
        if_speed.setPosition(basic_x, basic_y+basic_x_c*pos+25);

        pos=1;
        l_angle.setSize(100, 20);
        l_angle.setPosition(basic_x, basic_y+basic_x_c*pos);

        if_angle.setSize(100, 25);
        if_angle.setPosition(basic_x, basic_y+basic_x_c*pos+25);

        pos=2;
        // Button kleiner und verschoben
        btnThrow.setSize(50, 25);
        btnThrow.setPosition(basic_x+20, basic_y+basic_x_c*pos);
        */

        // Labels next to the inputs because of place-conflict the the skyscraper
        int pos=0;
        l_speed.setSize(60, 20);
        l_speed.setPosition(basic_x, basic_y + basic_x_c * pos);

        if_speed.setSize(100, 25);
        if_speed.setPosition(basic_x+60, basic_y+basic_x_c*pos);

        pos=1;
        l_angle.setSize(60, 20);
        l_angle.setPosition(basic_x, basic_y+basic_x_c*pos);

        if_angle.setSize(100, 25);
        if_angle.setPosition(basic_x+60, basic_y+basic_x_c*pos);

        pos=2;
        // Button kleiner und verschoben
        btnThrow.setSize(50, 25);
        btnThrow.setPosition(basic_x+60+20, basic_y+basic_x_c*pos);

        // Add the Input-Elements to the RootPane
        rp.add(l_speed);
        rp.add(if_speed);
        rp.add(l_angle);
        rp.add(if_angle);
        rp.add(btnThrow);
        return rp;
    }

    /**
     * Is called if a banana should thrown
     */
    private void throwBanana()
    {
        // TODO: Umsetzung des Bananenwurfes

        // During the flight disable inputs
        btnThrow.setVisible(false);
        if_speed.setEnabled(false);
        if_angle.setEnabled(false);

        System.out.println("Throw Banana s=" + if_speed.getValue()+
                " a="+ if_angle.getValue()  );

        // after the flight reactivate inputs
        btnThrow.setVisible(true);
        if_speed.setEnabled(true);
        if_angle.setEnabled(true);

    }
}
