package com.fadecloud.packetentityapi.api.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import com.fadecloud.packetentityapi.api.CustomEntity;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.function.Function;

public class EntityMetadata extends PacketWrapper {

    private final List<WrappedWatchableObject> metadata = new ArrayList<>();

    public EntityMetadata(CustomEntity entity) {
        super(entity);
    }

    public <I, O> EntityMetadata queue(EntityMetadataModifier<I, O> metadata, I value) {
        return this.queue(metadata.getIndex(), metadata.getMapper().apply(value), metadata.getOutputType());
    }

    public <T> EntityMetadata queue(int index, T value, Class<T> clazz) {
        return this.queue(index, value, ProtocolLibrary.getProtocolManager().getMinecraftVersion().getMinor() < 9 ? null : WrappedDataWatcher.Registry.get(clazz));
    }

    private <T> EntityMetadata queue(int index, T value, WrappedDataWatcher.Serializer serializer) {
        this.metadata.add(
                serializer == null ?
                        new WrappedWatchableObject(
                                index,
                                value
                        ) :
                        new WrappedWatchableObject(
                                new WrappedDataWatcher.WrappedDataWatcherObject(index, serializer),
                                value
                        )
        );

        return this;
    }

    @Override
    public void send(Player... targetPlayers) {
        PacketContainer packetContainer = super.newContainer(PacketType.Play.Server.ENTITY_METADATA);
        packetContainer.getWatchableCollectionModifier().write(0, this.metadata);
        super.send(targetPlayers);
    }

    public static class EntityMetadataModifier<I, O> {

        public static final EntityMetadataModifier<Boolean, Byte> SNEAKING = new EntityMetadataModifier<>(
                0,
                Byte.class,
                Collections.emptyList(),
                input -> (byte) (input ? 0x02 : 0)
        );

        public static final EntityMetadataModifier<Boolean, Byte> SWIMMING = new EntityMetadataModifier<>(
                0,
                Byte.class,
                Collections.emptyList(),
                input -> (byte) (input ? 0x10 : 0)
        );

        public static final EntityMetadataModifier<Boolean, Byte> INVISIBLE = new EntityMetadataModifier<>(
                0,
                Byte.class,
                Collections.emptyList(),
                input -> (byte) (input ? 0x20 : 0)
        );

        public static final EntityMetadataModifier<Boolean, Byte> GLOWING = new EntityMetadataModifier<>(
                0,
                Byte.class,
                Collections.emptyList(),
                input -> (byte) (input ? 0x40 : 0)
        );

        public static final EntityMetadataModifier<Boolean, Byte> FLYING_ELYTRA = new EntityMetadataModifier<>(
                0,
                Byte.class,
                Collections.emptyList(),
                input -> (byte) (input ? 0x80 : 0)
        );

        public static final EntityMetadataModifier<Boolean, Byte> SKIN_LAYERS = new EntityMetadataModifier<>(
                10,
                Byte.class,
                Arrays.asList(9, 9, 10, 14, 14, 15),
                input -> (byte) (input ? 0xff : 0)
        );

        public static final EntityMetadataModifier<Boolean, Integer> SLEEPING = new EntityMetadataModifier<>(
                6,
                Integer.class,
                Collections.emptyList(),
                input -> (input ? 2 : 0)
        );

        private final int baseIndex;
        private final Class<O> outputType;
        private final Collection<Integer> shiftVersions;
        private final Function<I, O> mapper;

        EntityMetadataModifier(int baseIndex, Class<O> outputType, Collection<Integer> shiftVersions, Function<I, O> mapper) {
            this.baseIndex = baseIndex;
            this.outputType = outputType;
            this.shiftVersions = shiftVersions;
            this.mapper = mapper;
        }

        public int getIndex() {
            return this.baseIndex + Math.toIntExact(this.shiftVersions.stream().filter(minor -> ProtocolLibrary.getProtocolManager().getMinecraftVersion().getMinor() >= minor).count());
        }

        public Class<O> getOutputType() {
            return outputType;
        }

        public Function<I, O> getMapper() {
            return mapper;
        }
    }

}