package io.snowyblossom126.elementapi.api;

import io.snowyblossom126.elementapi.api.elements.Element;
import io.snowyblossom126.elementapi.api.elements.ElementRelation;
import io.snowyblossom126.elementapi.api.elements.relation.BasicRelation;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
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

    private NamespacedKey elementKey;

    private ElementAPI() {}

    // -------------------------
    // Initialization
    // -------------------------

    /**
     * Initializes PersistentDataContainer integration.
     * <p>
     * Must be called once during plugin startup to enable player/item data storage.
     * </p>
     *
     * @param plugin the owning plugin (used for creating the {@link NamespacedKey}).
     */
    public void init(Plugin plugin) {
        this.elementKey = new NamespacedKey(plugin, "element_id");
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

    // -------------------------
    // Element Lookup
    // -------------------------

    /**
     * Adds an Element to an ItemStack using PersistentDataContainer.
     *
     * @param element   the element to attach
     * @param itemStack the item to modify
     * @return true if successful, false otherwise
     */
    public boolean addElementToItem(Element element, ItemStack itemStack) {
        if (elementKey == null) {
            throw new IllegalStateException("PDC has not been initialized. Call initPDC(plugin) first.");
        }
        if (element == null || itemStack == null) return false;

        var meta = itemStack.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(elementKey, PersistentDataType.STRING, element.getId());
        itemStack.setItemMeta(meta);
        return true;
    }

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
     * Retrieves an Element from an ItemStack's PersistentDataContainer.
     *
     * @param itemStack the item to read
     * @return an Optional containing the element, or empty if not found
     */
    public Optional<Element> getElementFromItem(ItemStack itemStack) {
        if (elementKey == null || itemStack == null || !itemStack.hasItemMeta()) {
            return Optional.empty();
        }

        var meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String id = container.get(elementKey, PersistentDataType.STRING);
        return getElement(id);
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
