package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Banana extends Entity {

    // [Rotations per s] / 1000
    public static final float ROATAION_SPEED = 2.0f / 1000;
    private Image img;

    public Banana(final float x, final float y){
        this.x = x;
        this.y = y;
        img = Assets.loadUniqueImage(Assets.Images.BANNA);
        img.setCenterOfRotation(img.getHeight()/ 2, img.getWidth() / 2);
    }

    @Override
    public void render(Graphics graph) {
        graph.drawImage(img, x, y);
    }

    @Override
    public void update(int delta) {
        /* Let the Banana rotate over time */
        img.rotate(ROATAION_SPEED * delta);

        // TODO: Move the Banana according to the maths :)
    }

}
