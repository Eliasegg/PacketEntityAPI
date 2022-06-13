package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import com.fadecloud.packetentityapi.api.CustomEntity;
import com.fadecloud.packetentityapi.api.PacketUtils;

public class EntityDisplayName extends PacketWrapper {

    public EntityDisplayName(CustomEntity entity) {
        super(entity);
    }

    public EntityDisplayName queue(String displayName) {
        PacketContainer packet = super.newContainer(PacketType.Play.Server.ENTITY_METADATA);
        PacketUtils metadata = new PacketUtils(null);
        metadata.writeOptional(2, WrappedChatComponent.fromText(displayName));
        packet.getIntegers().write(0, super.entity.getId());
        packet.getWatchableCollectionModifier().write(0, metadata.export());

        return this;
    }

}
