package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.tu_darmstadt.gdi1.gorillas.entities.Skyline;
import de.tu_darmstadt.gdi1.gorillas.main.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import de.tu_darmstadt.gdi1.gorillas.entities.Gorilla;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class GamePlayState extends BasicTWLGameState {

    private Skyline skyline;
    private Gorilla gorilla;  // Best Phun 4eva
    private Gorilla gorillb;  // :D
    private Image background;

    @Override
    public int getID() {
        return Gorillas.GAMEPLAYSTATE;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        background = Assets.imgBackground;
        skyline = new Skyline(6);

        int x1 = (int)(Math.random() * 3 + 0);
        int x2 = (int)(Math.random() * 3 + 3);

        int xx = x1 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);
        int yy = x2 * (skyline.BUILD_WIDTH) + (skyline.BUILD_WIDTH / 2);

        gorilla = new Gorilla(xx, Gorillas.FRAME_HEIGHT - skyline.getHeight(x1));
        gorillb = new Gorilla(yy, Gorillas.FRAME_HEIGHT - skyline.getHeight(x2));
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(background, -20, -10);
        skyline.render(g);
        gorilla.render(g);
        gorillb.render(g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        Input in = gc.getInput();
        if(in.isMousePressed(Input.MOUSE_LEFT_BUTTON))
            skyline.destroy(in.getMouseX(), in.getMouseY(), 64);
    }

}
