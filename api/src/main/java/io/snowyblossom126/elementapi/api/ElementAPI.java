package io.snowyblossom126.elementapi.api;

import io.snowyblossom126.elementapi.api.elements.Element;
import io.snowyblossom126.elementapi.api.elements.ElementRelation;
import io.snowyblossom126.elementapi.api.elements.relation.BasicRelation;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Central API for managing elements and their relations.
 * <p>
 * Provides:
 * <ul>
 *   <li>Element registration and lookup</li>
 *   <li>Defining relations between elements</li>
 *   <li>Player-specific element persistence via PDC (PersistentDataContainer)</li>
 * </ul>
 *
 * <p>
 * Implemented as a singleton via {@link #getInstance()}.
 * </p>
 *
 * Example usage:
 * <pre>{@code
 * ElementAPI api = ElementAPI.getInstance();
 * api.registerElement(new FireElement());
 * api.registerElement(new WaterElement());
 *
 * api.setRelation(FireElement.INSTANCE, WaterElement.INSTANCE, BasicRelation.STRONG);
 *
 * Optional<ElementRelation> rel = api.getRelation(FireElement.INSTANCE, WaterElement.INSTANCE);
 * rel.ifPresent(r -> System.out.println("Fire → Water: " + r.getMultiplier()));
 * }</pre>
 */
public final class ElementAPI {

    private static final ElementAPI instance = new ElementAPI();

    public static ElementAPI getInstance() {
        return instance;
    }

    /**
     * Helper record for storing a pair of elements.
     */
    private record ElementPair(Element from, Element to) {}

    private final Map<String, Element> registeredElements = new HashMap<>();
    private final Map<ElementPair, ElementRelation> relations = new HashMap<>();

    private Element defaultElement;
    private NamespacedKey pdcKey;

    private ElementAPI() {}

    // -------------------------
    // Initialization
    // -------------------------

    /**
     * Initializes PersistentDataContainer integration.
     * <p>
     * Must be called once during plugin startup to enable player data storage.
     * </p>
     *
     * @param plugin the owning plugin (used for creating the {@link NamespacedKey}).
     */
    public void initPDC(Plugin plugin) {
        this.pdcKey = new NamespacedKey(plugin, "element_api_registry");
    }

    // -------------------------
    // Element Registration
    // -------------------------

    /**
     * Registers an element into the API.
     *
     * @param element the element to register.
     * @return true if registration succeeded, false if the element already exists.
     */
    public boolean registerElement(Element element) {
        String key = element.getId().toUpperCase();
        if (registeredElements.containsKey(key)) return false;
        registeredElements.put(key, element);
        if (defaultElement == null) defaultElement = element;
        return true;
    }

    /**
     * Registers an element and assigns it to a specific player in their PDC.
     *
     * @param element the element to register.
     * @param player the player whose data container should be updated.
     * @return true if registration succeeded, false otherwise.
     */
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

    /**
     * Retrieves an element by ID.
     *
     * @param id the element identifier (case-insensitive).
     * @return an {@link Optional} containing the element, or empty if not found.
     */
    public Optional<Element> getElement(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(registeredElements.get(id.toUpperCase()));
    }

    /**
     * Returns the default element (the first one registered).
     *
     * @return the default {@link Element}, or {@code null} if none registered.
     */
    public Element getDefaultElement() {
        return defaultElement;
    }

    // -------------------------
    // Element Relations
    // -------------------------

    /**
     * Defines a relation between two elements.
     * <p>
     * - If the relation is symmetric, both directions are stored as the same relation.
     * <br>
     * - If the relation is asymmetric, the opposite direction stores {@link ElementRelation#getInverse()}.
     * </p>
     *
     * @param from source element
     * @param to target element
     * @param relation the relation type
     * @throws NullPointerException if any argument is null
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
     * If no relation is defined, returns a default normal relation.
     *
     * @param from source element
     * @param to target element
     * @return the relation, never null
     */
    public ElementRelation getRelation(Element from, Element to) {
        if (from == null || to == null) {
            return BasicRelation.GENERAL; // null 방어
        }
        return relations.getOrDefault(new ElementPair(from, to), BasicRelation.GENERAL);
    }

    /**
     * Retrieves the relation between two elements, or returns a default value if not defined.
     *
     * @param from source element
     * @param to target element
     * @param defaultRelation the fallback relation if none is defined
     * @return the existing relation, or the provided default
     */
    public ElementRelation getRelationOrDefault(Element from, Element to, ElementRelation defaultRelation) {
        ElementRelation relation = getRelation(from, to);
        return (relation != null) ? relation : defaultRelation;
    }
}
