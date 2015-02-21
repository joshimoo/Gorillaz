package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Assets;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Sun extends Entity{

    private Image img;

    public Sun(final float x, final float y){
        this.x = x;
        this.y = y;
        img = Assets.imgSunSmile;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, x - img.getWidth(), y - img.getHeight());
    }

    @Override
    public void update(int delta) {
        // TODO: Implement sun face-change on collision.
    }

}
