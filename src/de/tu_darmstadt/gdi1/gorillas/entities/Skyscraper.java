package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

public class Skyscraper extends Entity{

    private BufferedImage img;
    private BufferedImage destructionMap;
    private org.newdawn.slick.Image slickImg;
    private int height;
    private Color color;

    public Skyscraper(int position){
        x       = position * Gorillas.FRAME_WIDTH/6;
        height  = (int) (Math.random() * 450 + 50);
        y       = Gorillas.FRAME_HEIGHT - height;
        color   = new Color((int)(Math.random()*65536));

        // The image needs an Alphapart so we can erase some parts of it.
        img = new BufferedImage(Gorillas.FRAME_WIDTH, Gorillas.FRAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = img.createGraphics();
        graphic.setColor(color);
        graphic.fillRect((int)x, (int)y, Gorillas.FRAME_WIDTH / 6, height);

        try {
            destructionMap = ImageIO.read(new File("assets/gorillas/destruction.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pointless, getTexture fails only if img == null
        try {
            // TODO: does this update?
            slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, img));
        } catch (IOException e) { e.printStackTrace();}


    }

    public int getHeight(){
        return height;
    }

    @Override
    public void render(org.newdawn.slick.Graphics graph) {
        // Stupid pointless incompatibility between atw.BuffImg & slick.Img
        // Why cant we just draw the BuffImg, in OpenGL we could use textures.
        graph.drawImage(slickImg, 0, 0);
    }

    @Override public void update(int delta) {}

    public void destroy(final int x, final int y){
        Graphics2D graphic = img.createGraphics();
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        graphic.drawImage(destructionMap, null, x - (destructionMap.getWidth() >> 1), y - (destructionMap.getHeight() >> 1));

        // Oh god, why is life so terrible :(
        System.out.println(x + "," + y);

        try {
            // TODO: does this update?
            slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, img));
        } catch (IOException e) { e.printStackTrace();}
    }


}
