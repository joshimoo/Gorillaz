package eea.engine.component.render;

import eea.engine.component.RenderComponent;
import eea.engine.entity.StateBasedEntityManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * An AnimationRenderComponent is a renderable (displayable) component that can
 * be animated with a given animation speed.
 *
 * Effectively, this cycles through a set of images that should have the same
 * size (think of an animated GIF as a similar application). Example
 * applications include "detonation" effects or "movement animation" (left leg,
 * right leg, ...)
 *
 * @author Alexander Jandousek, Timo B&auml;hr, Joshua Moody
 * @version 1.0
 */
public class FrameRenderComponent extends RenderComponent {

    /**
     * the size of this animation, based on the size of the individual images
     */
    private Vector2f    size;

    /**
     * The animation object modeling the transition
     */
    protected Animation animation;

    /**
     * Create a new AnimationRenderComponent based on the information provided
     *
     * @param frames
     *          the individual animation frames to be displayed in order
     * @param width
     *          the width of the images
     * @param height
     *          the height of the images
     */
    public FrameRenderComponent(Image[] frames, float width, float height) {
        super("AnimationRenderComponent");

        // set the size for this component
        size = new Vector2f(width, height);

        // create a new Animation object based on the frames with a duration of 1
        animation = new Animation(frames, 1, false);

        // start the animation
        animation.start();
    }

    public void cycleNextFrame() { animation.setCurrentFrame( (animation.getFrame() + 1) % animation.getFrameCount()); }
    public void switchToFrame(int frame) {
        animation.setCurrentFrame(frame);
    }

    /**
     * Returns the size of the component as a 2D floating point vector
     *
     * @return the size of this AnimationRenderComponent as a 2D floating point
     *         vector using Slick's @{link org.newdawn.slick.geom.Vector2f}
     */
    @Override
    public Vector2f getSize() {
        return size;
    }

    /**
     * The abstract method for rendering the current component based on the
     * GameContainer, the current state of the game, and the Graphics context.
     *
     * Unless the animation is stopped, draw it at the desired location and with
     * the desired size
     *
     * @param gc
     *          the GameContainer object that handles the game loop, recording of
     *          the frame rate, and managing the input system
     * @param sb
     *          the StateBasedGame that isolates different stages of the game
     *          (e.g., menu, ingame, highscores etc.) into different states so
     *          they can be easily managed and maintained.
     * @param graphicsContext
     *          the graphics context necessary for painting ("rendering") the
     *          component on the game container display
     */
    @Override
    public void render(GameContainer gc, StateBasedGame sb,
                       Graphics graphicsContext) {
        // unless stopped, draw it at the target (x, y) position and size
        if (!animation.isStopped())
            animation.draw(getOwnerEntity().getPosition().x - size.x / 2, getOwnerEntity()
                    .getPosition().y - size.y / 2, size.x, size.y);
    }

    /**
     * All components have to provide the update method. This method updates the
     * component based on GameContainer and the StateBasedGame instance that
     * represents the current game state
     *
     * @param gc    the {@link org.newdawn.slick.GameContainer} object that handles
     *              the game loop, recording of the frame rate, and managing the input
     *              system
     * @param sb    the {@link org.newdawn.slick.state.StateBasedGame} that isolates
     *              different stages of the game (e.g., menu, ingame, highscores etc.)
     *              into different states so they can be easily managed and
     *              maintained.
     * @param delta the time passed in nanoseconds (ns) since the start of the event,
     */
    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {}
}