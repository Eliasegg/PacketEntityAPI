package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

import com.fadecloud.packetentityapi.api.CustomEntity;

import org.bukkit.Location;

import java.util.Collections;

public class EntityVisibility extends PacketWrapper {

    public EntityVisibility(CustomEntity entity) {
        super(entity);
    }

    public EntityVisibility spawn(Location location) {
        PacketContainer packetContainer = super.newContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

        packetContainer.getIntegers().write(0, super.entity.getId()); // runtime ID.
        packetContainer.getUUIDs().write(0, super.entity.getUUID()); // UUID
        packetContainer.getIntegers().write(1, super.entity.getType().getValue()); // entity type

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        packetContainer.getDoubles()
                .write(0, x)
                .write(1, y)
                .write(2, z);

        packetContainer.getBytes()
                .write(0, (byte) (location.getYaw() * 256.0F / 360.0F)) // yaw
                .write(1, (byte) (location.getPitch() * 256.0F / 360.0F)) // pitch
                .write(2, (byte) (location.getYaw() * 256.0F / 360.0F)); // head yaw

        return this;
    }

    public EntityVisibility destroy() {
        PacketContainer packetContainer = super.newContainer(PacketType.Play.Server.ENTITY_DESTROY, false);
        packetContainer.getIntLists().write(0, Collections.singletonList(super.entity.getId()));
        return this;
    }

}