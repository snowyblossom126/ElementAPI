package io.lumpq126.elementapi.api.elements;

import java.util.Objects;

/**
 * Represents a base element type within the system.
 * <p>
 * Each element is uniquely identified by a case-insensitive string ID
 * (e.g., "FIRE", "WATER"). Subclasses should extend this class
 * to define custom elements.
 * </p>
 */
public abstract class Element {
    private final String id;

    /**
     * Creates a new {@code Element} with the specified unique ID.
     *
     * @param id a non-null, non-blank string used to identify this element.
     *           Case is ignored; IDs are stored in uppercase.
     * @throws IllegalArgumentException if {@code id} is null or blank
     */
    protected Element(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Element ID cannot be null or empty.");
        }
        this.id = id.toUpperCase();
    }

    /**
     * Returns the unique identifier of this element.
     *
     * @return the element ID in uppercase
     */
    public final String getId() {
        return id;
    }

    /**
     * Returns the string representation of this element,
     * which is its unique ID.
     *
     * @return the element ID
     */
    @Override
    public final String toString() {
        return id;
    }

    /**
     * Compares this element with another for equality.
     * <p>
     * Two elements are considered equal if their IDs match,
     * regardless of their concrete class type.
     * </p>
     *
     * @param o the object to compare with
     * @return {@code true} if both elements have the same ID, otherwise {@code false}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element element)) return false;
        return id.equals(element.id);
    }

    /**
     * Returns the hash code for this element, based on its ID.
     *
     * @return the hash code value
     */
    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
