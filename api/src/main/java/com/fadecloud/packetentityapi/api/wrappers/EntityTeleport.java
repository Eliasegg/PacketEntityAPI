package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import com.fadecloud.packetentityapi.api.CustomEntity;
import com.fadecloud.packetentityapi.api.PacketUtils;

import org.bukkit.Location;

public class EntityTeleport extends PacketWrapper {

    public EntityTeleport(CustomEntity entity) {
        super(entity);
    }

    public EntityTeleport queue(Location location, boolean onGround) {
        byte yawAngle = PacketUtils.getCompressedByte(location.getYaw());
        byte pitchAngle = PacketUtils.getCompressedByte(location.getPitch());
        PacketContainer container = super.newContainer(PacketType.Play.Server.ENTITY_TELEPORT);

        container.getIntegers().write(0, super.entity.getId());

        container.getDoubles().write(0, location.getX());
        container.getDoubles().write(1, location.getY());
        container.getDoubles().write(2, location.getZ());

        container.getBytes().write(0, yawAngle);
        container.getBytes().write(1, pitchAngle);
        container.getBooleans().write(0, onGround);

        super.entity.setLocation(location); // Update location to client

        return this;
    }

}