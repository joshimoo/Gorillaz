package eea.engine.event;

import eea.engine.action.Action;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import eea.engine.entity.Entity;

/**
 * Multiple events can be combined using a logical conjunction (AND). In this
 * case, the associated action(s) will only be evaluated using the
 * {@link #update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)} method if <em>all</em>
 * event signal that they can be performed.
 * 
 * @author Alexander Jandousek, Timo B&auml;hr
 * @version 1.0
 */
public class ANDEvent extends Event {

  /**
   * the events associated with this combined event
   */
  private final Event[] events;

  /**
   * create a new ANDEvent by combining the arbitrary many events passed in.
   * 
   * @param eventsToCombine
   *          an arbitrarily long sequence of events. To make sense, there
   *          should be at least two events.
   */
  public ANDEvent(Event... eventsToCombine) {
    super("ANDEvent");
    events = eventsToCombine;
  }

  /**
   * checks if the action(s) associated with this event shall be performed. For
   * an ANDEvent, <em>all</em> associated events must signal their readiness to
   * be performed.
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
      // ... and if one is not ready to be performed...
      if (!event.performAction(gc, sb, delta))
        // .. then nothing is done.
        return false;
    }
    // all events have indicate their readiness
    return true;
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


  /**
   * This method first checks if the action(s) associated with this event should
   * be performed based on the state of the GameContainer, StateBasedGame and
   * time elapsed. If yes, all actions will be evaluated; otherwise, nothing
   * will happen.
   *
   * @param gc
   *          the {@link org.newdawn.slick.GameContainer} object that handles
   *          the game loop, recording of the frame rate, and managing the input
   *          system
   * @param sb
   *          the {@link org.newdawn.slick.state.StateBasedGame} that isolates
   *          different stages of the game (e.g., menu, ingame, highscores etc.)
   *          into different states so they can be easily managed and
   *          maintained.
   * @param delta
   *          the time passed in nanoseconds (ns) since the start of the event,
   *          used to interpolate the current target position
   */
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    // really perform the action(s)?
    if (performAction(gc, sb, delta)) {
      // yes => loop over all actions...
      executeActions(gc,sb,delta);

      // execute all of our children
      for (Event event : events) {
        // since we only trigger when all child conditions have been evaluated and they are all true
        // we can trigger their actions without checking their performAction again
        // this is good for performance, not so good if any of their events overwrites update.
        event.executeActions(gc, sb, delta);
      }
    }
  }


}
