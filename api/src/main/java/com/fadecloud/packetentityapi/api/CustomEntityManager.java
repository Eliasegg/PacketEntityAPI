package com.fadecloud.packetentityapi.api;

import com.fadecloud.packetentityapi.api.wrappers.PacketEntityType;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This class manages all created entity objects. Contains many useful methods to operate with.
 *
 */
public class CustomEntityManager {

    private Set<CustomEntity> entities = new HashSet<>();

    public static CustomEntityManager cem;

    public CustomEntityManager() {}

    public static CustomEntityManager getManager() {
        if (cem == null)
            cem = new CustomEntityManager();
        return cem;
    }

    /**
     * Creates a new custom entity and adds it to the manager list.
     *
     * @param type Type of entity. See {@link PacketEntityType#values()} for a full list of values.
     * @param location Location the entity will be spawned in.
     * @param uuid UUID attached to the entity.
     * @return CustomEntity The Custom Entity object for chaining purposes.
     */
    public CustomEntity createEntity(PacketEntityType type, Location location, UUID uuid) {
        CustomEntity entity = new CustomEntity().create(type, location, uuid);
        this.entities.add(entity);
        return entity;
    }

    /**
     * Gets the first entity in the stream.
     *
     * @return CustomEntity First entity in the list.
     */
    public CustomEntity getFirstEntity() {
        return entities.stream().findFirst().orElse(null);
    }

    /**
     * Gets the first entity by their UUID. PS: Do your null-checks!
     *
     * @param uuid UUID to look for.
     * @return CustomEntity The custom entity associated with the UUID.
     */
    public CustomEntity get(UUID uuid) {
        return this.entities.stream().filter(customEntity -> customEntity.getUUID().equals(uuid)).findFirst().orElse(null);
    }

    /**
     * Gets the first entity by their ID. PS: Do your null-checks!
     *
     * @param id ID to look for.
     * @return CustomEntity The custom entity associated with the ID.
     */
    public CustomEntity get(int id) {
        return this.entities.stream().filter(customEntity -> customEntity.getId() == id).findFirst().orElse(null);
    }

    /**
     * Removes all custom entity objects from this plugin.
     */
    public void removeAll() {
        this.entities.stream().forEach(customEntity -> customEntity.destroyEntity());
        this.entities.clear();
    }

    /**
     * Removes the custom entity from the manager list. For internal use only.
     *
     * @param uuid The UUID of the entity to remove from the list.
     */
    protected void remove(UUID uuid) {
        CustomEntity entity = this.get(uuid);
        if (entity != null) this.entities.remove(entity);
    }

    /**
     * Removes the custom entity from the manager list. For internal use only.
     *
     * @param id The ID of the entity to remove from the list.
     */
    protected void remove(int id) {
        CustomEntity entity = this.get(id);
        if (entity != null) this.entities.remove(entity);
    }

}
