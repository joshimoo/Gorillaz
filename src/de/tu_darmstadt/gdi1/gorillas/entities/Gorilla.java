package de.tu_darmstadt.gdi1.gorillas.entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Gorilla extends Entity{
    public static  int WIDTH;
    public static  Image img;

    public Gorilla(int x, int y){
        this.x = x;
        this.y = y;
        try {
            img = new Image("assets/gorillas/gorillas/gorilla.png");
        }
        catch (SlickException e) {
            img = null;
            e.printStackTrace();
        }
        WIDTH = img.getWidth();
    }
    @Override
    public void render(Graphics graph) {
        graph.drawImage(img, x - WIDTH / 2, y - img.getHeight());

    }

    @Override
    public void update(int delta) { // TODO:
    }

    public Image getImage() {
        return img;
    }
}
