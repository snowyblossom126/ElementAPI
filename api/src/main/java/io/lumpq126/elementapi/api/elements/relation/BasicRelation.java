package io.lumpq126.elementapi.api.elements.relation;

import io.lumpq126.elementapi.api.elements.ElementRelation;

/**
 * Defines basic types of elemental relations.
 * <p>
 * Relations describe how much damage (or effect strength) one element has against another.
 * </p>
 *
 * <ul>
 *   <li>{@link #STRONG} – Attacker is strong (2x), defender is weak (0.5x).</li>
 *   <li>{@link #WEAK} – Attacker is weak (0.5x), defender is strong (2x).</li>
 *   <li>{@link #NEUTRAL} – No advantage (1x both directions).</li>
 *   <li>{@link #MUTUAL_STRONG} – Both directions are strong (2x each way).</li>
 *   <li>{@link #MUTUAL_WEAK} – Both directions are weak (0.5x each way).</li>
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
    STRONG(2.0, 0.5, false),
    WEAK(0.5, 2.0, false),
    NEUTRAL(1.0, 1.0, true),
    MUTUAL_STRONG(2.0, 2.0, true),
    MUTUAL_WEAK(0.5, 0.5, true);

    private final double multiplier;
    private final double inverse;
    private final boolean symmetric;

    BasicRelation(double multiplier, double inverse, boolean symmetric) {
        this.multiplier = multiplier;
        this.inverse = inverse;
        this.symmetric = symmetric;
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
     *   this returns the corresponding opposite relation.
     * <br>
     * - For symmetric relations ({@link #NEUTRAL}, {@link #MUTUAL_STRONG}, {@link #MUTUAL_WEAK}),
     *   this returns {@code this}.
     * </p>
     *
     * @return the opposite {@link ElementRelation}.
     */
    @Override
    public ElementRelation getInverse() {
        if (symmetric) {
            return this;
        }
        for (BasicRelation r : values()) {
            if (r.multiplier == inverse && r.inverse == multiplier) {
                return r;
            }
        }
        return NEUTRAL;
    }

    @Override
    public String toString() {
        return name() + "(multiplier=" + multiplier + ")";
    }
}
