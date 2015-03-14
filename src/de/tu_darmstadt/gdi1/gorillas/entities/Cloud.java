package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Cloud extends Entity {
    private int w;
    private float t;

    public Cloud(Vector2f pos, int windSpeed) {
        super("Cloud");
        setPosition(pos);
        w = windSpeed;
        t = 0;

        // Rendering
        addComponent(new ImageRenderComponent(Assets.loadImage(Assets.Images.CLOUD)));
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc, sb, delta);

        // TODO: Add random delay before cloud comes back into screen
        Vector2f pos = getPosition();
        pos.x += (Game.getInstance().getWindScale() / 2) * w;

        if(pos.x < -getSize().x) {
            pos.x = gc.getWidth();
        } else if(pos.x > gc.getWidth() + getSize().x / 2) {
            pos.x = -getSize().x;
        }

        setPosition(pos);
    }
}
