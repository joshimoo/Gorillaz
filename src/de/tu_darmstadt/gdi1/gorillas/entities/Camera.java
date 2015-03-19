package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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

    public Image getBackground()
    {
        return background;
    }

    /**
     * Return the OffsetToCenter of the Canvas to the Frame Value is always negative.
     * @param screen    ScreenSize (WIDTH and HEIGHT)
     * @param center    Position that should the center of the window
     * @return X and Y to move to left (negative)
     */
    public Vector2f getOffsetToCenter(Vector2f screen, Vector2f center){
        return getOffsetToCenter(screen, center, 1f);
    }

    public Vector2f getOffsetToCenter(Vector2f screen, Vector2f center, float scale){
        float a = screen.x / scale;
        float b = screen.y / scale;
        float x = clamp(0, center.x - (a / 2), Gorillas.CANVAS_WIDTH - a);
        float y = clamp(0, center.y - (b / 2),screen.y - b);
        return new Vector2f(x * -scale, y * -scale);
    }

    private float clamp (float a, float x, float b){
        return Math.max(a, Math.min(x, b));
    }

}
