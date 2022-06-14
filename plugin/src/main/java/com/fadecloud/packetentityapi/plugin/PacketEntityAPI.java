package com.fadecloud.packetentityapi.plugin;

import com.fadecloud.packetentityapi.api.CustomEntityManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEntityAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new GeneralListener(), this);
    }

    @Override
    public void onDisable() {
        CustomEntityManager.getManager().removeAll();
    }

}

