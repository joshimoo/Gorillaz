package de.tu_darmstadt.gdi1.gorillas.entities.factories;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import eea.engine.entity.Entity;
import org.newdawn.slick.geom.Vector2f;

/**
 * This is a abstract Factory, all implementers must use the default EntityCreation Routine
 * For more information about the factory pattern have a look here: TODO: Add Reference Documentation
 */
abstract class EntityFactory {

    // TODO: consider whether we need, an extra scale if we are doing everything in m/s
    // If we design everything on m/s, we can then define a scale of 1 m == X pixels
    // Or we have custom scales, for our individual components of 1m == x pixels for WIND
    protected static final float MS_TO_S = 1.0f / 1000;
    private static final float DEFAULT_SCALE = 10;

    /**
     * Returns the default M to Pixels scale
     * if a entity type requires a different scale
     * override this method, in the child factory
     * TODO: Consider moving global constants into a global config class, which can be de/serialized
     * TODO: experiment with the scale values
     * @return default scale of 1m = 10 pixels
     */
    public static float getDefaultScale() { return DEFAULT_SCALE; }


    /**
     * All Entity Creation should happen via these wrappers
     * That will make sure that all used entity names are known
     */
    protected static Entity createEntity(EntityType type, Vector2f pos) {
        Entity entity = new Entity(type.name());
        entity.setPosition(pos);
        return entity;
    }


}
