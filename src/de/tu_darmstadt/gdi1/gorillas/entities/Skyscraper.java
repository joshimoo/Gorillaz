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

    public Skyscraper(int position, int width){
        this.height  = (int) (Math.random() * 450 + 50);
        this.width   = width;
        this.x       = position * width;
        this.y       = Gorillas.FRAME_HEIGHT - height;

        int red   = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue  = (int) (Math.random() * 255);
        Color color = new Color(red, green, blue);

        // The image needs an Alpha-component so we can erase some parts of it.
        img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        /* Draw the Skyscraper Wall */
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());

        /* Draw randomized Windows */
        int winSize    = 20;                              // Size of a window [px]
        int winColumns = (int) (Math.random() * 2 + 3);   // Amount of vertical columns [3-4]
        int winSpacing = getWidth() / winColumns;         // Spacing per Column
        int winPadding = (winSpacing - winSize) /2;       // Padding per Window
        int winRows    = getHeight() / winSpacing;        // Amount of Horizontal rows

        g.setColor(color.darker());
        for(int j = 0; j <= winRows; j++)
            for(int i = 0; i < winColumns; i++)
                g.fillRect( (i*winSpacing) + winPadding, (j*winSpacing) + winPadding, winSize, winSize);

        // Pointless, getTexture fails only if img == null
        try {
            slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, img));
        } catch (IOException e) { e.printStackTrace();}

    }

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
        // TODO: Switch to OGL Textruedrawing for improved Performance
        try {
            slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, img));
        } catch (IOException e) { e.printStackTrace();}
    }

    public int getHeight(){ return height; }
    public int getWidth() { return width;  }

    /* Not needed here */
    @Override public void update(int delta) {}
}
