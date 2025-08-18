package io.lumpq126.elementapi.api.elements;

import java.util.Objects;

public abstract class Element {
    private final String id;

    /**
     * 새로운 속성 클래스를 만들 때 호출해야 하는 생성자입니다.
     * @param id 속성을 식별할 고유 ID (예: "FIRE", "WATER"). 대소문자를 구분하지 않습니다.
     */
    protected Element(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Element ID cannot be null or empty.");
        }
        this.id = id.toUpperCase();
    }

    /**
     * 속성의 고유 ID를 반환합니다.
     * @return 속성 ID
     */
    public final String getId() {
        return id;
    }

    @Override
    public final String toString() {
        return id;
    }

    /**
     * 모든 Element는 ID를 기준으로 비교됩니다. 이 메서드는 final로 오버라이드할 수 없습니다.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return id.equals(element.id);
    }

    /**
     * 모든 Element의 해시코드는 ID를 기반으로 생성됩니다. 이 메서드는 final로 오버라이드할 수 없습니다.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
