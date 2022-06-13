package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import com.fadecloud.packetentityapi.api.CustomEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketWrapper {

    protected CustomEntity entity;
    private final List<PacketContainer> packetContainers = new CopyOnWriteArrayList<>();

    public PacketWrapper(CustomEntity entity) {
        this.entity = entity;
    }

    protected PacketContainer newContainer(PacketType packetType) {
        return this.newContainer(packetType, true);
    }

    protected PacketContainer newContainer(PacketType packetType, boolean withEntityId) {
        PacketContainer packetContainer = new PacketContainer(packetType);
        if (withEntityId) {
            packetContainer.getIntegers().write(0, this.entity.getId());
        }
        this.packetContainers.add(packetContainer);
        return packetContainer;
    }

    public void send() {
        entity.getViewers().forEach(uuid -> this.send(Bukkit.getPlayer(uuid)));
    }

    public void send(Player... targetPlayers) {
        for (Player targetPlayer : targetPlayers) {
            try {
                for (PacketContainer packetContainer : this.packetContainers) {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(targetPlayer, packetContainer);
                }
            } catch (InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }

        this.packetContainers.clear();
    }

}
