package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.fadecloud.packetentityapi.api.CustomEntity;

import com.fadecloud.packetentityapi.api.PacketUtils;
import org.bukkit.Location;

public class EntityMovement extends PacketWrapper {

    public EntityMovement(CustomEntity entity) {
        super(entity);
    }

    public EntityMovement queue(Location location) {
        PacketContainer packetContainer = super.newContainer(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        packetContainer.getIntegers().write(0, super.entity.getId());
        packetContainer.getShorts()
                .write(0, PacketUtils.getCompressedShort(location.getX() - super.entity.getLocation().getX()))
                .write(1, PacketUtils.getCompressedShort(location.getY() - super.entity.getLocation().getY()))
                .write(2, PacketUtils.getCompressedShort(location.getZ() - super.entity.getLocation().getZ()));

        packetContainer.getBytes()
                .write(0, PacketUtils.getCompressedByte(location.getYaw()))
                .write(1, PacketUtils.getCompressedByte(location.getPitch()));

        super.entity.setLocation(location); // Update location to client

        return this;
    }


}
