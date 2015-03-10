package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.entity.Entity;
import eea.engine.interfaces.ICollidable;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.BufferedImageUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Skyscraper extends Entity implements ICollidable {

    private BufferedImage img;
    private org.newdawn.slick.Image slickImg;

    private final int width;
    private final int height;

    public Skyscraper(int position, int width){
        super("Skyscaper");
        this.height  = (int) (Math.random() * 450 + 50);
        this.width   = width;
        setPosition(new Vector2f(position * width + width / 2, Gorillas.FRAME_HEIGHT - height / 2));
        setSize(new Vector2f(getWidth(), getHeight())); // We set the size explicitly since we have no render component

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
    public void render(GameContainer gc, StateBasedGame sb, org.newdawn.slick.Graphics graph) {
        super.render(gc, sb, graph);

        // Stupid pointless incompatibility between atw.BuffImg & slick.Img
        // Why cant we just draw the BuffImg, in OpenGL we could use textures.
        // Slick draws top left, while position is center position
        graph.drawImage(slickImg, getRenderPosition().x, getRenderPosition().y);
    }

    @Deprecated
    private Vector2f getRenderPosition() {
        return new Vector2f(getPosition().x - getSize().x / 2, getPosition().y - getSize().y / 2);
    }

    public void destroy(final int x, final int y, final int pow){
        Graphics2D graphic = img.createGraphics();
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));

        // We don't need a destruction map. This way we could even use different weapons
        // or randomized damage.
        graphic.fillOval( (int) (x - getRenderPosition().x) - pow, (int) (y - getRenderPosition().y) - pow, pow*2, pow*2);

        // still need to update this :/
        // TODO: Switch to OGL Textruedrawing for improved Performance
        try {
            slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, img));
        } catch (IOException e) { e.printStackTrace();}
    }

    public int getHeight(){ return height; }
    public int getWidth() { return width;  }

    @Override
    public boolean collides(float x, float y) {
        // calculate the relative coordinates (relative to the upper left corner
        // of the buffer used in the renderer)
        float halfWidth = this.getSize().x / 2;
        float halfHeight = this.getSize().y / 2;
        int relX = Math.round(x - this.getPosition().x + halfWidth);
        int relY = Math.round(y - this.getPosition().y + halfHeight);

        if (relX < 0 || relY < 0 || relX >= this.getSize().x || relY >= this.getSize().y) {
            return false; // not in buffer area
        }

        // return true, if a pixel is hit that is not fully transparent
        boolean collision = (img.getRGB(relX, relY) & 0xFF000000) != 0;
        if(Game.getInstance().getDebug() && collision) System.out.printf("Collision: %.0f, %.0f valid: %b %n", x, y, collision );
        return collision;
    }

    @Override
    public boolean collides(Entity otherEntity) {
        // Check if any part of Banana is inside of buffer area
        Vector2f pos = otherEntity.getPosition();
        float halfWidth = otherEntity.getSize().x / 2;
        float halfHeight = otherEntity.getSize().y / 2;

        // do a bounding box test - for performance we short circuit the evaluation
        return collides(pos.x - halfWidth, pos.y - halfHeight) ||
                collides(pos.x + halfWidth, pos.y - halfHeight) ||
                collides(pos.x - halfWidth, pos.y + halfHeight) ||
                collides(pos.x + halfWidth, pos.y + halfHeight);
    }
}
