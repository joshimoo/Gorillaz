package eea.engine.event;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.Entity;

/**
 * Multiple events can be combined using a logical disjunction (OR). In this
 * case, the associated action(s) will be evaluated using the
 * {@link #update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)} method if
 * <em>at least</em> event signals that it can be performed.
 * 
 * @author Alexander Jandousek, Timo B&auml;hr
 * @version 1.0
 */
public class OREvent extends Event {

  /**
   * the events associated with this combined event
   */
  private final Event[] events;

  /**
   * create a new OREvent by combining the arbitrary many events passed in.
   * 
   * @param eventsToCombine
   *          an arbitrarily long sequence of events. To make sense, there
   *          should be at least two events.
   */
  public OREvent(Event... eventsToCombine) {
    super("OREvent");
    events = eventsToCombine;
  }

  /**
   * checks if the action(s) associated with this event shall be performed. For
   * an OREvent, <em>at least</em> associated events must signal their readiness
   * to be performed.
   * 
   * @param gc
   *          the GameContainer object that handles the game loop, recording of
   *          the frame rate, and managing the input system
   * @param sb
   *          the StateBasedGame that isolates different stages of the game
   *          (e.g., menu, ingame, highscores etc.) into different states so
   *          they can be easily managed and maintained.
   * @param delta
   *          the time passed in nanoseconds (ns) since the start of the event,
   *          used to interpolate the current target position
   * 
   * @return true if the action(s) associated with this event shall be
   *         performed, else false
   */
  @Override
  protected boolean performAction(GameContainer gc, StateBasedGame sb, int delta) {
    // iterate over all events...
    for (Event event : events) {
      // ... and if at least one is ready to be performed...
      if (event.performAction(gc, sb, delta))
        // .. then indicate that we can start the update process
        return true;
    }
    // no event has indicated its readiness
    return false;
  }

  /**
   * assigns the owning entity for this combined event
   * 
   * @param owner
   *          the owner for this event
   */
  public void setOwnerEntity(Entity owner) {
    // locally assign the owning entity
    super.setOwnerEntity(owner);

    // also assign the owner for all sub-events
    for (Event event : events) {
      event.setOwnerEntity(owner);
    }
  }

}
