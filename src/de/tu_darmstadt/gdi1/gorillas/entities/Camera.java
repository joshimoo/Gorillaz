package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by Georg Schmidt on 18.03.2015.
 */
public class Camera {

    private Image background;
    private Image buffer;

    public Camera(Image background) {
        this.background = background;

        float scaleFactor = (float) Gorillas.CANVAS_WIDTH / background.getWidth();
        Game.CANVAS_SCALE = scaleFactor;

        if(Game.CANVAS_SCALE != 1) this.background = background.getScaledCopy(Game.CANVAS_SCALE);

        try {
            buffer = new Image(Gorillas.CANVAS_WIDTH, Gorillas.CANVAS_HEIGHT);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public Image getBuffer()
    {
        return buffer;
    }
}
