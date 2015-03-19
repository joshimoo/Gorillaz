package de.tu_darmstadt.gdi1.gorillas.utils;

import de.tu_darmstadt.gdi1.gorillas.main.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.EnumMap;

import static org.newdawn.slick.Input.*;

public class KeyMap {

    public static final KeyMap mapWASD1  = new KeyMap(KEY_A, KEY_D, KEY_W, KEY_S, KEY_SPACE);
    public static final KeyMap mapWASD2  = new KeyMap(KEY_W, KEY_S, KEY_A, KEY_D, KEY_SPACE);
    public static final KeyMap mapArrow1 = new KeyMap(KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN, KEY_ENTER);
    public static final KeyMap mapArrow2 = new KeyMap(KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT, KEY_ENTER);

    private EnumMap<Actions, Integer> map;

    public KeyMap(final int ... key) {
        if(key.length != 5)
            throw new RuntimeException("Need 5 Keys!");

        map = new EnumMap<>(Actions.class);
        map.put(Actions.ANGLE_INC, key[0]);
        map.put(Actions.ANGLE_DEC, key[1]);
        map.put(Actions.SPEED_INC, key[2]);
        map.put(Actions.SPEED_DEC, key[3]);
        map.put(Actions.THROW_NOW, key[4]);
    }

    /** True if the key associated with the given action was pressed */
    public boolean isAction(Input in, Actions a) {
        return in.isKeyPressed(map.get(a));
    }

    /** Controlling actions a player may take */
    public enum Actions { ANGLE_INC, ANGLE_DEC, SPEED_INC, SPEED_DEC, THROW_NOW }

    /**
     * Global KeyPressAction Switch mostly State-Transitions
     */
    public static void globalKeyPressedActions(Input input, StateBasedGame game)
    {
        switch (game.getCurrentStateID()) {
            case Game.MAINMENUSTATE:
                if (input.isKeyPressed(Input.KEY_RETURN) || input.isKeyPressed(Input.KEY_N))    game.enterState(Game.GAMESETUPSTATE);
                if (input.isKeyPressed(Input.KEY_ESCAPE))                                       Game.getInstance().exitGame();
                if (input.isKeyPressed(Input.KEY_S))                                            game.enterState(Game.HIGHSCORESTATE);
                if (input.isKeyPressed(Input.KEY_H))                                            game.enterState(Game.HELPSTATE);
                if (input.isKeyPressed(Input.KEY_O))                                            game.enterState(Game.OPTIONSTATE);
                if (input.isKeyPressed(Input.KEY_M))                                            Game.getInstance().toggleMute();
                break;
            case Game.GAMEPLAYSTATE:
                /**
                 * KEY_RETURN / KEY_SPACE                   -> throwBanana()
                 * KEY_RIGHT / KEY_LEFT / KEY_UP / KEY_DOWN -> Values
                 * KEY_D / KEY_A / KEY_W / KEY_S            -> Values
                 *
                 * IF DEVELOPER-MODE
                 * KEY_Q                                    -> startGame()
                 * KEY_1                                    -> createDebugFlatMap() + setLastAngle(45) + setLastSpeed(95)
                 * KEY_V                                    -> VICTORY
                 */
                if (input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Input.KEY_P))    game.enterState(Game.INGAMEPAUSE);
                if (input.isKeyPressed(Input.KEY_H))                                            game.enterState(Game.HELPSTATE);
                if (input.isKeyPressed(Input.KEY_O))                                            game.enterState(Game.OPTIONSTATE);
                if (input.isKeyPressed(Input.KEY_M))                                            Game.getInstance().toggleMute();
                break;
            case Game.GAMESETUPSTATE:
                /**
                 * KEY_RETURN   -> startGame();
                 * KEY_TAB      -> requestKeyboardFocus()
                 */
                if (input.isKeyPressed(Input.KEY_ESCAPE))        game.enterState(Game.MAINMENUSTATE);
                break;
            case Game.GAMEVICTORY:
                //if (input.isKeyPressed(Input.KEY_S))             game.enterState(Game.HIGHSCORESTATE);
                if (input.isKeyPressed(Input.KEY_ESCAPE))        game.enterState(Game.MAINMENUSTATE);
                if (input.isKeyPressed(Input.KEY_RETURN))        game.enterState(Game.GAMESETUPSTATE);
                //if (input.isKeyPressed(Input.KEY_H))             game.enterState(Game.HELPSTATE);
                //if (input.isKeyPressed(Input.KEY_O))             game.enterState(Game.OPTIONSTATE);
                //if (input.isKeyPressed(Input.KEY_M))             Game.getInstance().toggleMute();
                break;
            case Game.HELPSTATE:
                /**
                 * KEY_RIGHT / KEY_D    ->  nextPage()
                 * KEY_LEFT / KEY_A     ->  prevPage()
                 */
                if (input.isKeyPressed(Input.KEY_RETURN) || input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Input.KEY_H))    game.enterLastState();
                break;
            case Game.HIGHSCORESTATE:
                if (input.isKeyPressed(Input.KEY_RETURN) || input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Input.KEY_S))    game.enterLastState();
                break;
            case Game.INGAMEPAUSE:
                if (input.isKeyPressed(Input.KEY_ESCAPE) || input.isKeyPressed(Input.KEY_P) || input.isKeyPressed(Input.KEY_RETURN))
                                                                game.enterState(Game.GAMEPLAYSTATE);
                if (input.isKeyPressed(Input.KEY_N))            game.enterState(Game.GAMESETUPSTATE);
                if (input.isKeyPressed(Input.KEY_E))            Game.getInstance().exitGame();
                if (input.isKeyPressed(Input.KEY_S))            game.enterState(Game.MAINMENUSTATE);
                if (input.isKeyPressed(Input.KEY_H))            game.enterState(Game.HELPSTATE);
                if (input.isKeyPressed(Input.KEY_O))            game.enterState(Game.OPTIONSTATE);
                if (input.isKeyPressed(Input.KEY_M))            Game.getInstance().toggleMute();
                break;
            case Game.OPTIONSTATE:
                /**
                 * KEY_ESCAPE / KEY_O / KEY_ENTER   -> returnToPrevScreen()
                 * KEY_UP                           -> valueGravity.setValue( + )
                 * KEY_DOWN                         -> valueGravity.setValue( - )
                 * KEY_C                            -> toggleInverseControlKeys()
                 * KEY_W                            -> toggleWind()
                 * KEY_P                            -> toggleStorePlayerNames()
                 * KEY_M                            -> Option::toggleMute()
                  */
                break;
            default:
        }
    }
}
