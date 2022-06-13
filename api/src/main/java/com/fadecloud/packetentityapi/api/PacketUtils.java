package com.fadecloud.packetentityapi.api;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.ConstructorAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PacketUtils {

    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<>(); // Using String as 1st param because Class<?> has no hashcode

    static {
        PRIMITIVES.put("int", Integer.class);
        PRIMITIVES.put("byte", Byte.class);
        PRIMITIVES.put("boolean", Boolean.class);
    }

    private static final Class<?> HANDLE_TYPE = MinecraftReflection.getDataWatcherClass();

    public PacketUtils(Object entity) {
        this.entity = entity;
    }

    private static ConstructorAccessor constructor = null;

    private final Object entity;
    private final Map<Integer, Object> emptyOptionalData = new HashMap<>(); // Empty optionals, used for chatcomponents if the text is empty
    private final Map<Integer, Object> optionalData = new HashMap<>(); // Optional data, used for data types that have the "Opt" prefix
    private final Map<Integer, Object> data = new HashMap<>(); // All other data

    private static Object newHandle(Object entity) {
        if (constructor == null) {
            constructor = Accessors.getConstructorAccessor(HANDLE_TYPE, MinecraftReflection.getEntityClass());
        }

        return constructor.invoke(entity);
    }
    // -- PROTOCOLLIB END

    /**
     * Debugs values
     */
    public void print() {
        for (Map.Entry<Integer, Object> entry : data.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    /**
     * Writes an object into the internal data, to later be serialized into
     * the DataWatcher
     *
     * @param index - The object index
     * @param value - The object value
     */
    public void write(int index, Object value) {
        data.put(index, value);
    }

    /**
     * Writes an Optional Object into the internal data, to later be serialized
     * into the DataWatcher
     *
     * @param index - The object index
     * @param value - The object value
     */
    public void writeOptional(int index, Object value) {
        optionalData.put(index, value);
    }

    /**
     * Writes an empty optional object into the internal data, to later
     * be serialized into the DataWatcher
     *
     * @param index       - Object index
     * @param randomValue - Random object instance, used to obtain the class
     */
    public void writeEmptyData(int index, Object randomValue) {
        emptyOptionalData.put(index, randomValue);
    }

    /**
     * Exports the metadata as a List<WrappedWatchableObject>,
     * to be used directly into the metadata packet.
     *
     * @return - Metadata values
     */
    public List<WrappedWatchableObject> export() {
        // Makes a data watcher, uses fake internal entity if no entity is provided.
        WrappedDataWatcher watcher = (entity == null) ? new WrappedDataWatcher() : new WrappedDataWatcher(newHandle(entity));
        writeData(watcher, emptyOptionalData, true, true); // Writes empty optional data
        writeData(watcher, optionalData, true, false); // Writes optional data
        writeData(watcher, data, false, false); // Writes remainding data

        return watcher.getWatchableObjects();
    }

    /**
     * Method to write internal data. Pure spaghetti
     *
     * @param watcher  - Data watcher to write to
     * @param data     - Internal data to write
     * @param optional - TRUE if data is purely optional, FALSE otherwise
     * @param empty    - TRUE if data is purely empty and optional, FALSE otherwise
     */
    private void writeData(WrappedDataWatcher watcher, Map<Integer, Object> data, boolean optional, boolean empty) {
        for (Map.Entry<Integer, Object> entry : data.entrySet()) { // Loops through all data
            int index = entry.getKey();
            Object value = entry.getValue();

            Class<?> clazz = value.getClass(); // Obtains value class, to later be implemented as a serializer

            if (clazz.isPrimitive()) // Boxes primitives
                clazz = PRIMITIVES.get(clazz.getName());

            if (clazz.equals(ItemStack.class)) { // Item serializer special handling
                watcher.setObject(index, WrappedDataWatcher.Registry.getItemStackSerializer(false), value);
                continue;
            }

            if (clazz.equals(WrappedChatComponent.class)) { // Chat serializer special handling
                if (optional) {
                    value = empty ? Optional.empty() : Optional.of(((WrappedChatComponent) value).getHandle());
                }
                watcher.setObject(index, WrappedDataWatcher.Registry.getChatComponentSerializer(optional), value);
                continue;
            }

            if(clazz.equals(WrappedBlockData.class)) {
                if (optional) {
                    value = empty ? Optional.empty() : Optional.of(((WrappedBlockData) value).getHandle());
                }

                watcher.setObject(index, WrappedDataWatcher.Registry.getBlockDataSerializer(optional), value);
                continue;
            }

            // Serializes everything else
            watcher.setObject(index, WrappedDataWatcher.Registry.get(clazz, optional), value);

        }
    }

    public static byte getCompressedByte(double value) {
        return (byte) (value * 256F / 360F);
    }

    public static short getCompressedShort(double value) {
        return (short) (value * 4096);
    }

}
