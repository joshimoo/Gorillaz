package de.tu_darmstadt.gdi1.gorillas.entities.factories;

import de.tu_darmstadt.gdi1.gorillas.entities.CompoundDestructibleEntity;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import de.tu_darmstadt.gdi1.gorillas.entities.components.ShapeDestructionPattern;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.DestructionRenderComponent;
import eea.engine.entity.DestructibleImageEntity;
import eea.engine.entity.Entity;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Responsible for creating all Building Types
 * Most Types are Destructible
 */
public class BuildingFactory extends EntityFactory {
    // TODO: Decide whether to have single entities for each building or do a compound Entity
    // TODO: Decide whether to have one compound Entity for all Buildings
    // TODO: Decide whether to have a hybrid approach

    private static final float EXPLOSION_RADIUS = 32;

    private static BufferedImage createBuildingImage(int width, int height, java.awt.Color color) {
        // Build the House Image
        // The image needs an Alpha-component so we can erase some parts of it.
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        // TODO: Consider how to handle Alpha - // g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));

        /* Draw the Skyscraper Wall */
        g.setColor(color);
        g.fillRect(0, 0, width, height);

        /* Draw randomized Windows */
        int winSize    = 20;                              // Size of a window [px]
        int winColumns = (int) (Math.random() * 2 + 3);   // Amount of vertical columns [3-4]
        int winSpacing = width / winColumns;         // Spacing per Column
        int winPadding = (winSpacing - winSize) /2;       // Padding per Window
        int winRows    = height / winSpacing;        // Amount of Horizontal rows

        g.setColor(color.darker());
        for(int y = 0; y <= winRows; y++) {
            for(int x = 0; x < winColumns; x++) {
                g.fillRect( (x*winSpacing) + winPadding, (y*winSpacing) + winPadding, winSize, winSize);
            }
        }

        return img;
    }

    public static DestructibleImageEntity createBuilding(Vector2f pos, int width, int height) {
        // Color Information
        int red   = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue  = (int) (Math.random() * 255);
        Color color = new Color(red, green, blue);

        return createBuilding(pos, width, height, color);
    }

    public static DestructibleImageEntity createBuilding(Vector2f pos, int width, int height, Color color) {
        // Create the Destroyable Components
        DestructionRenderComponent destructionRenderComponent = new DestructionRenderComponent(
                createBuildingImage(width, height, new java.awt.Color(color.r, color.g, color.b, color.a)),
                ShapeDestructionPattern.createCircleDestructionPattern(EXPLOSION_RADIUS),
                Game.getInstance().getDebug()
        );

        DestructibleImageEntity building = new DestructibleImageEntity(EntityType.BUILDING.name(), destructionRenderComponent);
        building.setPosition(pos);

        return building;
    }

    // TODO: Refactor
    public static CompoundDestructibleEntity createCompoundSkyline(int buildingCount) {
        DestructibleImageEntity[] buildings = new DestructibleImageEntity[buildingCount];

        for(int i = 0; i < buildingCount; i++) {
            int buildingWidth = Gorillas.FRAME_WIDTH / buildingCount;
            int buildingHeight = (int)(Math.random() * 450 + 50);
            int x = i * buildingWidth;
            int y = Gorillas.FRAME_HEIGHT - buildingHeight;

            buildings[i] = createBuilding(new Vector2f(x, y), buildingWidth, buildingHeight);
        }

        return new CompoundDestructibleEntity(EntityType.BUILDING.name(), buildings);
    }

    public static Entity createSkyline(Vector2f pos){
        // TODO: Implement
        throw new NotImplementedException();
    }
}
