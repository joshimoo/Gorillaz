package de.tu_darmstadt.gdi1.gorillas.utils;

import org.newdawn.slick.Input;
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

}
