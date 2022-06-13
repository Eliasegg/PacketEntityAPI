package com.fadecloud.packetentityapi.api;

import com.fadecloud.packetentityapi.api.wrappers.EntityAnimation;
import com.fadecloud.packetentityapi.api.wrappers.EntityMetadata;
import com.fadecloud.packetentityapi.api.wrappers.PacketEntityType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface EntityBase {

    /**
     * Creates an entity with its given parameters. It will create a random entity ID which can be used to retrieve later on.
     * You need to call {@link CustomEntity#addToViewerList(Player)} for players to actually see the entity.
     *
     * @param entityType Entity type you want spawned. Use PacketEntity enum for supported values.
     * @param location The location this entity will spawn in.
     * @return
     */
    EntityBase create(PacketEntityType entityType, Location location, UUID uuid);

    /**
     * Gets the entity ID assigned to the entity.
     *
     * @return int The entity ID assigned.
     */
    int getId();

    /**
     * Returns the entity type assigned to the entity.
     *
     * @return EntityType Entity Type of the Custom Entity.
     */
    PacketEntityType getType();

    /**
     * Returns the random UUID assigned to the entity.
     *
     * @return UUID UUID assigned to the entity.
     */
    UUID getUUID();

    /**
     * Gets the current client location of the entity.
     *
     * @return Location location of the custom entity.
     */
    Location getLocation();

    /**
     * Gets the list of UUIDs that can see the entity.
     *
     * @return List#UUID List of UUID's of players that can see the entity.
     */
    List<UUID> getViewers();

    /**
     * Adds a player to the viewer list. They will be able to see the entity.
     * This invokes the SPAWN_ENTITY_LIVING packet for the player.
     *
     * Note: Make sure to use this before doing anything to the entity.
     *
     *
     * @param player Player that can see the entity.
     */
    void addToViewerList(Player player);

    /**
     * Removes a player from the viewer list.
     * This invokes the ENTITY_DESTROY packet for the player.
     *
     *  @param player Player that will be removed from the list.
     */
    void removeFromViewerList(Player player);

    /**
     * Add a display name to the entity! supports color codes.
     *
     * @param display Display name of the entity.
     */
    void addDisplayName(String display);

    /**
     * Hide the entity for everyone.
     * This does not destroy the entity. Use {@link EntityBase#destroy} to get rid of the entity.
     * NOTE: THIS ONLY MAKES THE ENTITY INVISIBLE. REMOVE ARMORS AND EFFECTS!
     */
    void hide();

    /**
     * Hides the entity for a specific player.
     * This does not destory the entity. Use {@link EntityBase#destroy} to get rid of the entity.
     *
     * @param player The player
     */
    void hide(Player player);

    /**
     * Unhides a hidden entity to all players.
     * Requires {@link EntityBase#create} to be used first.
     */
    void unhide();

    /**
     * Unhides a hidden entity to a specific player.
     *
     * @param player The player that will be able to see the entity.
     */
    void unhide(Player player);

    /**
     * Destroys the entity. Effectively removing it from the managers.
     */
    void destroy();

    /**
     * Teleports the entity to the specified location.
     *
     * @param location Location to teleport the entity to.
     * @param onGround Whether it will be on ground or not.
     */
    void teleport(Location location, boolean onGround);

    /**
     * Moves the entity to the specified location. It does not take in consideration obstacles and whatnot.
     * This is best to use with a scheduler that listens every tick and moves a few blocks every few seconds as the packet behaves incorrectly in some ways.
     * It is recommended to take a look at the example plugin.
     *
     * @param location Location the entity is going to go to.
     */
    void moveHere(Location location);

    /**
     * Makes the entity look to a certain location.
     *
     * @param location Location you want the entity to look at.
     */
    void moveHead(Location location);

    /**
     * Adds the specified material to a certain equipment slot.
     * The entity must be spawned first using {@link EntityBase#create}
     *
     * @param item Item to add.
     * @param slot Where to.
     */
    void addEquipment(ItemStack item, EquipmentSlot slot);

    /**
     * Plays an animation for the entity. Not all entities will work with these animation types.
     *
     * @param animationType Type of animation you want played.
     */
    void playAnimation(EntityAnimation.EntityAnimationType animationType);

    /**
     * Sets the metadata of the entity.
     * Not all Metadata modifiers will work for all entities. For instance: even though Skin layers are supported they most likely won't work for any entities besides players.
     *
     * @param metadata Metadata to be applied to the entity.
     */
    void setMetadata(EntityMetadata.EntityMetadataModifier metadata);

    /**
     * Sets the metadata to any extra values you want.
     * @param index Index of the value.
     * @param value Which value to put.
     * @param clazz Which class does the index belongs to.
     * @param <T>
     */
    <T> void setMetadata(int index, T value, Class<T> clazz);

}