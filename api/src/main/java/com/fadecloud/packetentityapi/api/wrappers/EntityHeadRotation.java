package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.fadecloud.packetentityapi.api.CustomEntity;
import com.fadecloud.packetentityapi.api.PacketUtils;
import org.bukkit.Location;

public class EntityHeadRotation extends PacketWrapper {

    public EntityHeadRotation(CustomEntity entity) {
        super(entity);
    }

    public EntityHeadRotation queueRotate(Location location) {
        byte yawAngle = PacketUtils.getCompressedByte(location.getYaw());
        byte pitchAngle = PacketUtils.getCompressedByte(location.getPitch());

        PacketContainer entityHeadLookContainer = super.newContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        entityHeadLookContainer.getIntegers().write(0, super.entity.getId());
        entityHeadLookContainer.getBytes().write(0, yawAngle);

        PacketContainer bodyRotateContainer = super.newContainer(PacketType.Play.Server.ENTITY_LOOK);
        bodyRotateContainer.getIntegers().write(0, super.entity.getId());
        bodyRotateContainer.getBytes()
                .write(0, yawAngle)
                .write(1, pitchAngle);
        bodyRotateContainer.getBooleans().write(0, true);

        super.entity.getLocation().setPitch(location.getPitch());
        super.entity.getLocation().setYaw(location.getYaw());

        return this;
    }

}