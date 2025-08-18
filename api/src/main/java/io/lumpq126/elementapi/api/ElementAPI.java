package io.lumpq126.elementapi.api;

import io.lumpq126.elementapi.api.elements.Element;
import io.lumpq126.elementapi.api.elements.ElementRelation;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Central API for managing and interacting with the element system.
 * <p>
 * Provides registration, lookup, relation mapping between elements, and
 * PDC-based persistence for player-specific element data.
 * </p>
 * <p>
 * Implemented as a singleton to ensure a single global instance.
 * </p>
 */
public final class ElementAPI {

    private static final ElementAPI instance = new ElementAPI();

    public static ElementAPI getInstance() {
        return instance;
    }

    // --- Helper Record for Relation Map Key ---
    private record ElementPair(Element from, Element to) {}

    // -------------------------
    // Internal Storage
    // -------------------------

    private final Map<String, Element> registeredElements = new HashMap<>();
    private final Map<ElementPair, ElementRelation> relations = new HashMap<>();

    private Element defaultElement;
    private NamespacedKey pdcKey;

    private ElementAPI() {}

    // -------------------------
    // Initialization
    // -------------------------

    public void initPDC(Plugin plugin) {
        this.pdcKey = new NamespacedKey(plugin, "element_api_registry");
    }

    // -------------------------
    // Element Registration
    // -------------------------

    public boolean registerElement(Element element) {
        String key = element.getId().toUpperCase();
        if (registeredElements.containsKey(key)) return false;
        registeredElements.put(key, element);
        if (defaultElement == null) defaultElement = element;
        return true;
    }

    public boolean registerElement(Element element, Player player) {
        boolean success = registerElement(element);
        if (success && player != null && pdcKey != null) {
            player.getPersistentDataContainer()
                    .set(pdcKey, PersistentDataType.STRING, element.getId().toUpperCase());
        }
        return success;
    }

    // -------------------------
    // Element Lookup
    // -------------------------

    public Optional<Element> getElement(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(registeredElements.get(id.toUpperCase()));
    }

    public Element getDefaultElement() {
        return defaultElement;
    }

    // -------------------------
    // Element Relations
    // -------------------------

    /**
     * Defines a symmetric relation between two elements.
     * If the relation is not symmetric, relation.getInverse() will be stored in the opposite direction.
     */
    public void setRelation(Element from, Element to, ElementRelation relation) {
        Objects.requireNonNull(from, "Source element cannot be null");
        Objects.requireNonNull(to, "Target element cannot be null");
        Objects.requireNonNull(relation, "Relation cannot be null");

        relations.put(new ElementPair(from, to), relation);
        relations.put(new ElementPair(to, from), relation.getInverse());
    }

    /**
     * Retrieves the relation between two elements.
     */
    public Optional<ElementRelation> getRelation(Element from, Element to) {
        if (from == null || to == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(relations.get(new ElementPair(from, to)));
    }

    /**
     * Retrieves the relation between two elements, or returns a default relation if none exists.
     */
    public ElementRelation getRelationOrDefault(Element from, Element to, ElementRelation defaultRelation) {
        return getRelation(from, to).orElse(defaultRelation);
    }
}
