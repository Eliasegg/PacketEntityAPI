package com.fadecloud.packetentityapi.plugin;

import com.fadecloud.packetentityapi.api.CustomEntityManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEntityAPI extends JavaPlugin {

    private static PacketEntityAPI instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new GeneralListener(), this);
    }

    @Override
    public void onDisable() {
        CustomEntityManager.getManager().removeAll();
    }

    public static PacketEntityAPI getInstance() {
        return instance;
    }

}

