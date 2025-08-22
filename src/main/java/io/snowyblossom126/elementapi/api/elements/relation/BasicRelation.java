package io.snowyblossom126.elementapi.api.elements.relation;

import io.snowyblossom126.elementapi.api.elements.ElementRelation;

/**
 * Defines basic types of elemental relations.
 * <p>
 * Relations describe how much damage (or effect strength) one element has against another.
 * </p>
 *
 * <ul>
 * <li>{@link #STRONG} – Attacker is strong (2x), defender is weak (0.5x).</li>
 * <li>{@link #WEAK} – Attacker is weak (0.5x), defender is strong (2x).</li>
 * <li>{@link #GENERAL} – No advantage (1x both directions).</li>
 * <li>{@link #MUTUAL_STRONG} – Both directions are strong (2x each way).</li>
 * </ul>
 *
 * <p>
 * Example use:
 * <pre>{@code
 * ElementRelation r = BasicRelation.STRONG;
 * System.out.println(r.getMultiplier()); // 2.0
 * System.out.println(r.getInverse().getMultiplier()); // 0.5
 * }</pre>
 * </p>
 */
public enum BasicRelation implements ElementRelation {
    STRONG(2.0),
    WEAK(0.5),
    GENERAL(1.0),
    MUTUAL_STRONG(2.0);

    private final double multiplier;

    BasicRelation(double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Returns the damage multiplier of this relation.
     *
     * @return a numeric multiplier (e.g. 2.0 for strong, 0.5 for weak, 1.0 for neutral).
     */
    @Override
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * Returns the inverse relation, i.e., the relation in the opposite direction.
     * <p>
     * - For asymmetric relations ({@link #STRONG}, {@link #WEAK}),
     * this returns the corresponding opposite relation.
     * <br>
     * this returns {@code this}.
     * </p>
     *
     * @return the opposite {@link ElementRelation}.
     */
    @Override
    public ElementRelation getInverse() {
        return switch (this) {
            case STRONG -> WEAK;
            case WEAK -> STRONG;
            default -> this;
        };
    }

    @Override
    public String toString() {
        return name() + "(multiplier=" + multiplier + ")";
    }
}