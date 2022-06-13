package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;

import com.comphenix.protocol.wrappers.Pair;
import com.fadecloud.packetentityapi.api.CustomEntity;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class EntityEquipment extends PacketWrapper {

    public EntityEquipment(CustomEntity entity) {
        super(entity);
    }

    public EntityEquipment queue(ItemStack item, EquipmentSlot itemSlot) {
        PacketContainer packetContainer = super.newContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packetContainer.getIntegers().write(0, super.entity.getId());
        packetContainer.getSlotStackPairLists()
                .write(0, Collections.singletonList(new Pair<>(EnumWrappers.ItemSlot.valueOf(itemSlot.toString()), item)));
        return this;
    }

}
