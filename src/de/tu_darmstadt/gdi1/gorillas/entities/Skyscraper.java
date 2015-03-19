package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.entity.Entity;
import eea.engine.interfaces.ICollidable;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Skyscraper extends Entity implements ICollidable {

    private BufferedImage buf;
    private org.newdawn.slick.Image slickImg;

    public Skyscraper(Vector2f topLeftPos, int buildingWidth, int mapWidth, int mapHeight) {
        super("Skyscaper");
        int height = mapHeight - (int) topLeftPos.y;
        int width = buildingWidth;

        // Translate TopLeft to Center Position
        setPosition(new Vector2f(topLeftPos.x + width / 2, mapHeight - height / 2));
        setSize(new Vector2f(width, height)); // We set the size explicitly since we have no render component

        // Building
        Random r = new Random();
        Color color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));

        // Windows
        int winSize    = 20;                            // Size of a window [px]
        int winColumns = r.nextInt(2) + 3;              // Amount of vertical columns [3-4]
        int winSpacing = width / winColumns;       // Spacing per Column
        int winPadding = (winSpacing - winSize) /2;     // Padding per Window
        int winRows    = height / winSpacing;      // Amount of Horizontal rows

        // The image needs an Alpha-component so we can erase some parts of it.
        buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();

        /* Draw the Skyscraper Wall */
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(color.darker());
        for(int j = 0; j <= winRows; j++)
            for(int i = 0; i < winColumns; i++)
                g.fillRect( (i*winSpacing) + winPadding, (j*winSpacing) + winPadding, winSize, winSize);


        // NOTE: We are allowed to use an AWT Buffered image, we can even use a graphics draw ontop of the BufferedImage
        // But no Texture Generation, since that will require an OpenGL Context.
        if (!Game.getInstance().isTestMode()) {
            try { slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, buf)); }
            catch (Exception e) { e.printStackTrace();}
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sb, org.newdawn.slick.Graphics graph) {
        if (Game.getInstance().isTestMode()) { return; } // Don't draw anything in testmode
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
        Graphics2D graphic = buf.createGraphics();
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));

        // We don't need a destruction map. This way we could even use different weapons
        // or randomized damage.
        graphic.fillOval( (int) (x - getRenderPosition().x) - pow, (int) (y - getRenderPosition().y) - pow, pow*2, pow*2);

        // NOTE: We are allowed to use an AWT Buffered image, we can even use a graphics draw ontop of the BufferedImage
        // But no Texture Generation, since that will require an OpenGL Context.
        if (!Game.getInstance().isTestMode()) {
            try { slickImg = new org.newdawn.slick.Image(BufferedImageUtil.getTexture(null, buf)); }
            catch (Exception e) { e.printStackTrace();}
        }
    }

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
        boolean collision = (buf.getRGB(relX, relY) & 0xFF000000) != 0;
        if(Game.getInstance().isDebugMode() && collision) System.out.printf("Collision: %.0f, %.0f valid: %b %n", x, y, collision );
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
