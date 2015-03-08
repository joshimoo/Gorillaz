package de.tu_darmstadt.gdi1.gorillas.entities.factories;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import de.tu_darmstadt.gdi1.gorillas.entities.events.CollidedWithEvent;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.ui.states.GamePlayState;
import de.tu_darmstadt.gdi1.gorillas.main.Player;
import eea.engine.component.render.AnimationRenderComponent;
import eea.engine.entity.Entity;
import eea.engine.event.ANDEvent;
import eea.engine.event.Event;
import eea.engine.event.NOTEvent;
import eea.engine.event.basicevents.CollisionEvent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Responsible for Creation of all Player Entities
 * For Example: Gorillas
 */
public class PlayerFactory extends EntityFactory {

    public static Entity createGorilla(Vector2f pos, Player player){
        Entity gorilla = createEntity(EntityType.PLAYER, pos);
        float ANIMATION_SPEED = 1.0f; /* How fast should the Animation be */
        int   FRAME_LENGTH    = 1000; /* How long a single frame should be displayed [ms] */

        // Rendering
        Image[] frames = new Image[] {
                Assets.loadImage(Assets.Images.GORRILA_LEFT),
                Assets.loadImage(Assets.Images.GORRILA),
                Assets.loadImage(Assets.Images.GORRILA_RIGHT)
        };
        gorilla.addComponent(new AnimationRenderComponent(frames, ANIMATION_SPEED / FRAME_LENGTH, frames[0].getWidth(), frames[0].getHeight(), true));
        // TODO: ADD MORE CONSTRUCTORS, default constructor sets duration = 1


        // Collisions
        // TODO: Add Collisions, if we plan on implementing movement
        // TODO: Add manual animation switch action for playing an animation when hit.
        // TODO: Think about active player switching

        CollisionEvent collision = new CollisionEvent();
        NOTEvent notOurProjectile = new NOTEvent(new Event("Filter") {
            @Override
            protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
                if (sb.getCurrentStateID() == Game.GAMEPLAYSTATE) {
                    GamePlayState gs = (GamePlayState) sb.getCurrentState();
                    return gs.getActivePlayer() == player;
                }
                return false;
            }
        });
        ANDEvent validCollision = new ANDEvent(collision, new CollidedWithEvent(collision, EntityType.PROJECTILE.name()), notOurProjectile );
        validCollision.addAction((gc, sb, delta, event) -> player.setWin());
        // gorilla.addComponent(validCollision);

        return gorilla;
    }
}
