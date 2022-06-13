package com.fadecloud.packetentityapi.api;

import com.fadecloud.packetentityapi.api.wrappers.EntityAnimation;
import com.fadecloud.packetentityapi.api.wrappers.EntityDisplayName;
import com.fadecloud.packetentityapi.api.wrappers.EntityEquipment;
import com.fadecloud.packetentityapi.api.wrappers.EntityHeadRotation;
import com.fadecloud.packetentityapi.api.wrappers.EntityMetadata;
import com.fadecloud.packetentityapi.api.wrappers.EntityMovement;
import com.fadecloud.packetentityapi.api.wrappers.EntityTeleport;
import com.fadecloud.packetentityapi.api.wrappers.EntityVisibility;
import com.fadecloud.packetentityapi.api.wrappers.PacketEntityType;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.concurrent.ThreadLocalRandom;

public class CustomEntity implements EntityBase {

    private PacketEntityType entityType;
    private int entityId;
    private UUID uuid;
    private Location location;
    private List<UUID> viewers = new ArrayList<>();

    @Override
    public CustomEntity create(PacketEntityType entityType, Location location, UUID uuid) {
        this.entityType = entityType;
        this.location = location;
        this.entityId = ThreadLocalRandom.current().nextInt(800000, 9999999 + 1);
        this.uuid = uuid;
        
        return this;
    }

    @Override
    public void addToViewerList(Player player) {
        EntityVisibility visibility = new EntityVisibility(this);
        visibility.spawn(this.location).send(player);
        this.viewers.add(player.getUniqueId());
    }

    @Override
    public void removeFromViewerList(Player player) {
        EntityVisibility visibility = new EntityVisibility(this);
        visibility.destroy().send(player);
        this.viewers.remove(player.getUniqueId());
    }

    @Override
    public void addDisplayName(String display) {
        EntityDisplayName name = new EntityDisplayName(this);
        name.queue(display).send();
    }

    @Override
    public void hide(Player player) {
        EntityMetadata meta = new EntityMetadata(this);
        meta.queue(EntityMetadata.EntityMetadataModifier.INVISIBLE, true).send(player);
    }

    @Override
    public void hide() {
        this.viewers.forEach(uuid -> this.hide(Bukkit.getPlayer(uuid)));
    }

    @Override
    public void unhide(Player player) {
        EntityMetadata meta = new EntityMetadata(this);
        meta.queue(EntityMetadata.EntityMetadataModifier.INVISIBLE, false).send(player);
    }

    @Override
    public void unhide() {
        this.viewers.forEach(uuid -> this.unhide(Bukkit.getPlayer(uuid)));
    }

    @Override
    public void destroy() {
        EntityVisibility visibility = new EntityVisibility(this);
        visibility.destroy().send();
        CustomEntityManager.getManager().remove(this.entityId);
    }

    @Override
    public void teleport(Location location, boolean onGround) {
        EntityTeleport teleport = new EntityTeleport(this);
        teleport.queue(location, onGround).send();
    }

    @Override
    public void moveHere(Location location) {
        EntityMovement movement = new EntityMovement(this);
        movement.queue(location).send();
    }

    @Override
    public void moveHead(Location location) {
        EntityHeadRotation rotation = new EntityHeadRotation(this);
        rotation.queueRotate(location).send();
    }

    @Override
    public void addEquipment(ItemStack item, EquipmentSlot slot) {
        EntityEquipment entityEquipment = new EntityEquipment(this);
        entityEquipment.queue(item, slot).send();
    }

    @Override
    public void playAnimation(EntityAnimation.EntityAnimationType animationType) {
        EntityAnimation entityAnimation = new EntityAnimation(this);
        entityAnimation.queue(animationType).send();
    }

    @Override
    public void setMetadata(EntityMetadata.EntityMetadataModifier metadata) {
        EntityMetadata meta = new EntityMetadata(this);
        meta.queue(metadata, true).send();
    }

    @Override
    public <T> void setMetadata(int index, T value, Class<T> clazz) {
        EntityMetadata meta = new EntityMetadata(this);
        meta.queue(index, value, clazz).send();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public PacketEntityType getType() {
        return this.entityType;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public int getId() {
        return this.entityId;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public List<UUID> getViewers() {
        return this.viewers;
    }

}
