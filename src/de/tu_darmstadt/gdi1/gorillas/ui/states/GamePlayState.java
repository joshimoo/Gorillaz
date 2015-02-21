package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.*;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.entities.Skyline;
import de.tu_darmstadt.gdi1.gorillas.entities.Sun;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.entities.Gorilla;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.state.StateBasedGame;


public class GamePlayState extends BasicTWLGameState {

    private Skyline skyline;
    private Gorilla gorilla;  // Best Phun 4eva
    private Gorilla gorillb;  // :D
    private Sun sun;
    private Image background;

	// Input-Elements Speed and Angle
	private RootPane rp;
    private Button btnThrow;
    private Label l_speed;
    private ValueAdjusterInt if_speed;
    private Label l_angle;
    private ValueAdjusterInt if_angle;
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

		// Needed for adding the new Input-Elements
		rp = super.createRootPane();

        // Create Input-Elements Speed and Angle
        l_speed = new Label("Speed");
        l_speed.setLabelFor(if_speed);
        // TODO: Set text color WHITE
        //l_speed.setForeground(Color.WHITE)

        if_speed= new ValueAdjusterInt();
        if_speed.setMinMaxValue(0,200);
        if_speed.setValue(100);

		
        l_angle = new Label("Angle ");
        l_angle.setLabelFor(if_angle);
        // TODO: Set text color WHITE

        if_angle = new ValueAdjusterInt();
        if_angle.setMinMaxValue(0,180);
        if_angle.setValue(120);

        btnThrow = new Button("Throw");

        // Wirkungslos
        btnThrow.setAlignment(Alignment.CENTER);
        btnThrow.addCallback(new Runnable() {
            public void run() {
                // TODO: Umsetzung des Bananenwurfes
                // Während des Fluges Eingaben deaktivieren
                btnThrow.setVisible(false);
                if_speed.setEnabled(false);
                if_angle.setEnabled(false);

                System.out.println("Throw Banana s=" + if_speed.getValue()+
                                    " a="+ if_angle.getValue()  );
                // Wurfsimulation
                try {
                    Thread.sleep(3000);
                }
                catch(InterruptedException e) {
                    System.out.println("Bla");
                }

                // Nach dem Flug Eingaben reaktivieren
                btnThrow.setVisible(true);
                if_speed.setEnabled(true);
                if_angle.setEnabled(true);

            }
        });
		
		// Set Size and Possition of the Input-Elements
        int basic_x=20;
        int basic_y=10;
        int basic_x_c=35;

        /*
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

        int pos=0;
        l_speed.setSize(60, 20);
        l_speed.setPosition(basic_x, basic_y+basic_x_c*pos);

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
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -20, -10);
        sun.render(g);
        skyline.render(g);
        gorilla.render(g);
        gorillb.render(g);

        g.setColor(Color.black);    /* Dropshadow */
        g.drawString(Gorillas.player2, gorillb.x - g.getFont().getWidth(Gorillas.player2) / 2 + 1, gorillb.y - 63);
        g.drawString(Gorillas.player1, gorilla.x - g.getFont().getWidth(Gorillas.player1) / 2 + 1, gorilla.y - 63);

        /* We could possibly change the name-color of the active player as an indication */
        g.setColor(Color.white);
        g.drawString(Gorillas.player2, gorillb.x - g.getFont().getWidth(Gorillas.player2) / 2, gorillb.y - 64);
        g.drawString(Gorillas.player1, gorilla.x - g.getFont().getWidth(Gorillas.player1) / 2, gorilla.y - 64);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        gorilla.update(delta);
        gorillb.update(delta);

        Input in = gc.getInput();
        if(in.isMousePressed(Input.MOUSE_LEFT_BUTTON))
            skyline.destroy(in.getMouseX(), in.getMouseY(), 64);
    }

    @Override
    protected RootPane createRootPane() {
        return rp;
    }
}
