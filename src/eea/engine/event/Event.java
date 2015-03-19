package eea.engine.event;

import eea.engine.action.Action;
import eea.engine.component.Component;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * An event describes something that "happened", for example, a collision
 * between two objects, mouse or keyboard actions. You can add an arbitrary
 * number of {@link eea.engine.action.Action} instances to a given event. Each
 * event is associated with an entity and possesses an identifier.
 * 
 * @author Alexander Jandousek, Timo B&auml;hr
 * @version 1.0
 */
public abstract class Event extends Component {

  /**
   * the list of actions associated with this event
   */
  private ArrayList<Action> actions = null;

  /**
   * Creates a new event with the given ID. Also initializes the internal list
   * of associated actions.
   * 
   * @param id
   *          the ID for this event
   */
  public Event(String id) {
    super(id);
    actions = new ArrayList<Action>();
  }

  /**
   * adds the given {@link eea.engine.action.Action} to the event
   * 
   * @param action
   *          the action to be added to the event
   */
  public void addAction(Action action) {
    actions.add(action);
  }

  /**
   * removes all {@link eea.engine.action.Action} instances from the event
   */
  public void clearActions() {
    actions.clear();
  }

  /**
   * checks if the action(s) associated with this event shall be performed. The
   * realization of this method is deferred to the concrete subtypes.
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
  protected abstract boolean performAction(GameContainer gc, StateBasedGame sb,
      int delta);

  /**
   * removes the given {@link eea.engine.action.Action} from the event
   * 
   * @param action
   *          the action to be removed from the event
   */
  public void removeAction(Action action) {
    actions.remove(action);
  }

  /**
   * removes the {@link eea.engine.action.Action} at a given index position from
   * the event
   * 
   * @param index
   *          the position of the action to be removed from the event
   */
  public void removeAction(int index) {
    actions.remove(index);
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
    }
  }

  // Package Private
  // So that only me and the Logical Conditions can access
  void executeActions(GameContainer gc, StateBasedGame sb, int delta) {
    for (Action action : actions) {
      action.update(gc, sb, delta, this);
    }
  }

}