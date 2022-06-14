package com.fadecloud.packetentityapi.plugin;

import com.fadecloud.packetentityapi.api.CustomEntity;
import com.fadecloud.packetentityapi.api.CustomEntityManager;
import com.fadecloud.packetentityapi.api.wrappers.EntityAnimation;
import com.fadecloud.packetentityapi.api.wrappers.EntityMetadata;
import com.fadecloud.packetentityapi.api.wrappers.PacketEntityType;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public class GeneralListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player name = event.getPlayer();
        name.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "PacketEntityAPI test!");
        name.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Right click " + ChatColor.RESET + ChatColor.GRAY + "the air to spawn a Zombie, Panda, Blaze or Bee that follows you around.");
        name.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Left click " + ChatColor.RESET + ChatColor.GRAY + "a block to destroy the first entity in the list.");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String[] mobList = {"ZOMBIE", "PANDA", "BLAZE", "BEE"};
            Random ran = new Random();
            String mob = mobList[ran.nextInt(mobList.length)];

            CustomEntity entity = CustomEntityManager.getManager().createEntity(PacketEntityType.valueOf(mob), event.getPlayer().getLocation().clone().add(2, 0, 0), UUID.randomUUID()); // Let's spawn a random mob in our location. We add +2 to the X location so it is not on top of us. We can also declare its UUID for later retrieval.

            entity.addToViewerList(event.getPlayer()); // Adds the player to the viewers list.
            entity.addDisplayName("TheBest!"); // We can set a display name.
            entity.addEquipment(new ItemStack(Material.DIAMOND_HELMET), EquipmentSlot.HEAD); // We can add Equipment,  as long as the entity supports it.
            entity.playAnimation(EntityAnimation.EntityAnimationType.CRITICAL_EFFECT); // We also are able to add animation effects.
            entity.setMetadata(EntityMetadata.EntityMetadataModifier.GLOWING); // Custom metadata modifiers such as SNEAKING, GLOWING, SWIMMING etc.

        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            CustomEntity entity = CustomEntityManager.getManager().getFirstEntity(); // Getting the first CustomEntity object stored.

            if (entity != null) {
                CustomEntityManager.getManager().removeAll();
            }
        }

    }

    // Listener for the mob that follows us around.
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        CustomEntity entity = CustomEntityManager.getManager().getFirstEntity(); // We get the first entity.
        if (entity != null) { // Null checking! important.
            entity.moveHead(event.getTo()); // Spawn the move head packet whenever our mouse is moved to make sure everything is in sync.
            if ((event.getTo().getX() == event.getFrom().getX()) || (event.getTo().getZ() == event.getFrom().getZ())) return; // Don't move the entity unless we moved.
            entity.moveHere(event.getPlayer().getLocation().clone().add(2, 0, 0)); // Move the entity 2 blocks next to us.
        }
    }

}
