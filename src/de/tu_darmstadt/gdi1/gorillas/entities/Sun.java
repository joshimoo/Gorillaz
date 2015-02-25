package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

public class Sun extends Entity{

    private Image img;
    private Circle collCircle;

    public Sun(final float x, final float y){
        this.x = x;
        this.y = y;
        img = Assets.loadImage(Assets.Images.SUN_SMILING);
        collCircle = new Circle(x - img.getWidth()/2, y - img.getHeight()/2,img.getHeight()/2);
    }

    @Override
    public void render(Graphics g) {

        g.drawImage(img, x - img.getWidth(), y - img.getHeight());
        //g.draw(collCircle);
    }

    @Override
    public void update(int delta) {
        // TODO: Implement sun face-change on collision.
    }

    @Override
    public boolean isCollidding(Banana b) {
        boolean bool = collCircle.intersects(b.getColMask());
        if(bool)
            img = Assets.loadImage(Assets.Images.SUN_ASTONISHED);
        else
            img = Assets.loadImage(Assets.Images.SUN_SMILING);
        return bool;
    }
}
