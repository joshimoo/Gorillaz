package de.tu_darmstadt.gdi1.gorillas.test.setup;

import eea.engine.test.TestInput;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.SlickException;

public class TWLTestAppGameContainer extends AppGameContainer {

    public TWLTestAppGameContainer(Game game) throws SlickException {
        super(game, 640, 480, false);
    }

    public TWLTestAppGameContainer(Game game, int width, int height, boolean fullscreen) throws SlickException {
        super(game, width, height, fullscreen);
    }

    /**
     * Analog zu AppGameContainer.start() wird das StateBasedGame im
     * TestAppGameContainer gestartet, d.h. das Spiel wird ohne UI gestartet.
     *
     * @param delta Verzoegerung zwischen Frames
     * @throws SlickException
     */
    public void start(int delta) throws SlickException {

        this.input = new TestInput(delta);
        ((TWLTestStateBasedGame) game).initStatesList(this);
        game.update(this, delta);
        game.init(this);

    }

    public TestInput getTestInput() {
        return (TestInput) input;
    }

    public void updateGame(int delta) throws SlickException {
        game.update(this, delta);
        game.update(this, delta);
    }

    @Override
    public void reinit() {
        try {
            game.init(this);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}
