package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Skyscraper extends Entity{

    private BufferedImage img;
    private org.newdawn.slick.Image slickImg;

    private final int width;
    private final int height;
    private final Color color;

    public Skyscraper(int position, int width){
        this.height  = (int) (Math.random() * 450 + 50);
        this.width   = width;
        this.x       = position * width;
        this.y       = Gorillas.FRAME_HEIGHT - height;

        this.color   = new Color((int)(Math.random()*65536));

        // The image needs an Alpha-component so we can erase some parts of it.
        img = new BufferedImage(Gorillas.FRAME_WIDTH/6, getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = img.createGraphics();
        graphic.setColor(color);
        graphic.fillRect(0, 0, Gorillas.FRAME_WIDTH/6, getHeight());

        // Pointless, getTexture fails only if img == null
        try {
            slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, img));
        } catch (IOException e) { e.printStackTrace();}

    }

    public int getHeight(){ return height; }
    public int getWidth() { return width;  }

    @Override
    public void render(org.newdawn.slick.Graphics graph) {
        // Stupid pointless incompatibility between atw.BuffImg & slick.Img
        // Why cant we just draw the BuffImg, in OpenGL we could use textures.
        graph.drawImage(slickImg, x, y);
    }

    public void destroy(final int x, final int y, final int pow){
        Graphics2D graphic = img.createGraphics();
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));

        // We don't need a destruction map. This way we could even use different weapons
        // or randomized damage.
        graphic.fillOval( (int) (x - this.x) - pow/2 , (int) (y - this.y) - pow/2, pow, pow);

        // still need to update this :/
        try {
            slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, img));
        } catch (IOException e) { e.printStackTrace();}
    }

    /* Not needed here */
    @Override public void update(int delta) {}
}
