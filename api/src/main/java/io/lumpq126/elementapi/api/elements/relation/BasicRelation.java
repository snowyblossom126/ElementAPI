package io.lumpq126.elementapi.api.elements.relation;

import io.lumpq126.elementapi.api.elements.ElementRelation;

public enum BasicRelation implements ElementRelation {
    STRONG(2.0, 0.5, false),          // 한쪽은 강점, 반대쪽은 약점
    WEAK(0.5, 2.0, false),           // 한쪽은 약점, 반대쪽은 강점
    NEUTRAL(1.0, 1.0, true),         // 서로 중립
    MUTUAL_STRONG(2.0, 2.0, true),   // 서로 강점 (빛 ↔ 어둠 등)
    MUTUAL_WEAK(0.5, 0.5, true);

    private final double multiplier;
    private final double inverse;
    private final boolean symmetric;

    BasicRelation(double multiplier, double inverse, boolean symmetric) {
        this.multiplier = multiplier;
        this.inverse = inverse;
        this.symmetric = symmetric;
    }

    @Override
    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public ElementRelation getInverse() {
        if (symmetric) {
            return this; // 대칭 관계면 자기 자신 반환
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
