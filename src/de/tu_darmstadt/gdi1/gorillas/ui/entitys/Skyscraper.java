package de.tu_darmstadt.gdi1.gorillas.ui.entitys;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created by Tamara on 17.02.2015.
 */
public class Skyscraper extends Entity{
    private int height;
    private Color color;

    Skyscraper(int position){
        x = position * Gorillas.FRAME_WIDTH;
        height = (int) (Math.random() * 450 + 50);
        y = Gorillas.FRAME_HEIGHT - height;
        color = new Color((int)(Math.random()*65536));
    }

    public int getHeigth(){
        return height;
    }

    @Override
    public void render(Graphics graph) {
        graph.setColor(color);
        graph.drawRect(x, y, Gorillas.FRAME_WIDTH / 6, height);
    }

    @Override
    public void update(int delta) {
        //TODO: Explosionen
    }
}
