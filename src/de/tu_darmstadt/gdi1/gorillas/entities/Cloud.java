package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Cloud extends Entity {
    private Image img;
    private int w;
    private float t;

    public Cloud(float x, float y, int w){
        img = Assets.loadUniqueImage(Assets.Images.CLOUD);
        this.x = x;
        this.y = y;

        this.w = w;
        t = 0;

    }
    @Override
    public void render(Graphics graph) {
        graph.drawImage(img,x,y);
    }

    @Override
    public void update(int delta) {
        x += (Game.getWindScale()/2) * w;
        if(x < -img.getWidth()) x = Gorillas.FRAME_WIDTH;
        else if(x > Gorillas.FRAME_WIDTH) x = -img.getWidth();
    }

    @Override
    public boolean isCollidding(Banana b) {
        return false;
    }
}
