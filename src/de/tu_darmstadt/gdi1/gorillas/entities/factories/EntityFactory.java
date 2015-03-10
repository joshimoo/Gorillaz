package de.tu_darmstadt.gdi1.gorillas.entities.factories;
import de.tu_darmstadt.gdi1.gorillas.entities.EntityType;
import eea.engine.entity.Entity;
import org.newdawn.slick.geom.Vector2f;

/**
 * This is a abstract Factory, all implementers must use the default EntityCreation Routine
 * For more information about the factory pattern have a look here: TODO: Add Reference Documentation
 */
abstract class EntityFactory {
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
