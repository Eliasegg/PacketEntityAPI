package com.fadecloud.packetentityapi.api;

import com.fadecloud.packetentityapi.api.wrappers.PacketEntityType;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This class manages all created entity objects. Contains many useful methods to operate with.
 *
 */
public class CustomEntityManager {

    private Map<CustomEntity, JavaPlugin> entities = new HashMap<>();

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
    public CustomEntity createEntity(JavaPlugin plugin, PacketEntityType type, Location location, UUID uuid) {
        CustomEntity entity = new CustomEntity().create(type, location, uuid);
        this.entities.put(entity, plugin);
        return entity;
    }

    /**
     * Gets the first entity in the stream.
     *
     * @return CustomEntity First entity in the list.
     */
    public CustomEntity getFirstEntity() {
        Optional<CustomEntity> customEntity = this.entities.keySet().stream().findFirst();
        if (customEntity.isPresent()) {
            return customEntity.get();
        }
        return null;
    }

    /**
     * Gets the first entity by their UUID. PS: Do your null-checks!
     *
     * @param uuid UUID to look for.
     * @return CustomEntity The custom entity associated with the UUID.
     */
    public CustomEntity get(UUID uuid) {
        Optional<CustomEntity> customEntity = this.entities.keySet().stream().filter(entity -> entity.getUUID().equals(uuid)).findFirst();
        if (customEntity.isPresent()) {
            return customEntity.get();
        }
        return null;
    }

    /**
     * Gets the first entity by their ID. PS: Do your null-checks!
     *
     * @param id ID to look for.
     * @return CustomEntity The custom entity associated with the ID.
     */
    public CustomEntity get(int id) {
        Optional<CustomEntity> customEntity = this.entities.keySet().stream().filter(entity -> entity.getId() == id).findFirst();
        if (customEntity.isPresent()) {
            return customEntity.get();
        }
        return null;
    }

    /**
     * Removes all custom entity objects from this plugin.
     */
    public void removeAll() {
        this.entities.keySet().stream().forEach(customEntity -> customEntity.destroyEntity());
        this.entities.clear();
    }

    /**
     * Removes all entities spawned by a plugin.
     *
     * @param plugin The plugin that spawned the entities.
     */
    public void removeAll(JavaPlugin plugin) {
        this.entities.entrySet().stream()
                .filter(entry -> plugin.getName().equals(entry.getValue().getName()))
                .forEach(entry -> entry.getKey().destroyEntity());
        this.entities.entrySet().removeIf(entry -> plugin.getName().equals(entry.getValue().getName()));
        System.out.println(this.entities);
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
