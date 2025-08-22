package elementapi.api.elements;

/**
 * Defines a relation between two Elements.
 * Implementations could represent damage multipliers, strengths, weaknesses, etc.
 */
public interface ElementRelation {

    /**
     * Returns the numeric strength multiplier of this relation.
     * Example: 2.0 = strong, 0.5 = weak, 1.0 = neutral.
     */
    double getMultiplier();

    /**
     * Returns the inverse relation for the opposite direction.
     * Example: Fire -> Water is 0.5x, then Water -> Fire should be 2.0x.
     */
    ElementRelation getInverse();
}
