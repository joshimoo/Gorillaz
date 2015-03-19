package de.tu_darmstadt.gdi1.gorillas.entities;

import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import de.tu_darmstadt.gdi1.gorillas.main.Gorillas;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Cloud extends Entity {
    private int windSpeed;

    public Cloud(Vector2f pos, int windSpeed) {
        super("Cloud");
        setPosition(pos);
        this.windSpeed = windSpeed;


        if (!Game.getInstance().isTestMode()) {
            // Rendering
            addComponent(new ImageRenderComponent(Assets.loadImage(Assets.Images.CLOUD)));
        } else {
            // In Test Mode set the size explicitly
            setSize(new Vector2f(128, 64));
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc, sb, delta);

        // TODO: Add random delay before cloud comes back into screen
        Vector2f pos = getPosition();
        pos.x += (windSpeed / 2) * Game.getInstance().getWindScale();

        if(pos.x < -getSize().x) {
            pos.x = Gorillas.CANVAS_WIDTH;
        } else if(pos.x > Gorillas.CANVAS_WIDTH + getSize().x / 2) {
            pos.x = -getSize().x;
        }

        setPosition(pos);
    }
}
