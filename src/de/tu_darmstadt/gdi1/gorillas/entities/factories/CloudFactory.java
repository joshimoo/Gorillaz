package de.tu_darmstadt.gdi1.gorillas.entities.factories;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import eea.engine.action.Action;
import eea.engine.action.basicactions.MoveRightAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.basicevents.LeavingScreenEvent;
import eea.engine.event.basicevents.LoopEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Responsible for Creation of the Cloud Entity
 */
public class CloudFactory extends EntityFactory {

    // TODO: Experiment, with this value
    // Original values where FPS dependent
    // (0.6 / 2) * w * fps
    // 0.3 * w * fps
    // where w[-15, 15), fps = 120
    // 0.3 * 7.5 * 120
    // 2.5 * 120 = 300 pixel pro sekunde
    // pretty high
    // maybe 1m = 20 pixels, which would lead to 150 pixels/s at 7.5 m/s
    private static final float WIND_SCALE = 20;
    public static float getWindScale() { return WIND_SCALE; }

    /**
     * Creates a constant windSpeed Cloud
     * @param windSpeed should be provided in m/s
     */
    public static Entity createCloud(Vector2f pos, float windSpeed) {
        Entity cloud = createEntity(EntityType.CLOUD, pos);

        // Rendering
        cloud.addComponent(new ImageRenderComponent(Assets.loadImage(Assets.Images.CLOUD)));

        // Static Wind
        // TODO: speed should be in m/s, add conversion since it's multiplied by delta
        // TODO: Dynamic Wind Update
        LoopEvent update = new LoopEvent();
        update.addAction(new MoveRightAction(windSpeed * MS_TO_S * getWindScale()));
        cloud.addComponent(update);

        // Wrap Around
        LeavingScreenEvent leave = new LeavingScreenEvent();
        leave.addAction(new Action() {
            @Override
            public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {
                // make sure we have left the screen completely
                Entity e = event.getOwnerEntity();
                Vector2f pos = e.getPosition();
                if (pos.x < -e.getSize().x) { e.setPosition(new Vector2f(gc.getWidth(), pos.y)); }
                else if(pos.x > gc.getWidth()) { e.setPosition(new Vector2f(-e.getSize().x, pos.y)); }
            }
        });
        cloud.addComponent(leave);

        return cloud;
    }
}
