package de.tu_darmstadt.gdi1.gorillas.entities;

/**
 * This contains all EntityTypes, we use the types as string identifiers for EEA
 * By doing it this way, we have compile time checking. (static analysis)
 * And it will make it easier if we decide in the future todo data driven design (entity serialization)
 * Or if we want todo a collision matrix, so that only specific entities collide with each other
 * NOTE: Entities of the same Type don't collide with each other
 */
public enum EntityType {
    GORILLA,
    BUILDING,
    PROJECTILE,
    CLOUD,
    SUN
}