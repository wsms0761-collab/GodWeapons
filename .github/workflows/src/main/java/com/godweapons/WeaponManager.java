package com.godweapons;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;
        if (args.length == 0) {
            p.sendMessage("§cUse: /godweapon <slayer|inferno|hammer|void|celestial>");
            return true;
        }

        ItemStack item;
        ItemMeta meta;

        switch (args[0].toLowerCase()) {

            // ⚡ GOD SLAYER
            case "slayer":
                item = new ItemStack(Material.NETHERITE_SWORD);
                meta = item.getItemMeta();
                meta.setDisplayName("§6⚡ God Slayer");
                meta.setCustomModelData(1001);
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
                break;

            // 🔥 INFERNO AXE
            case "inferno":
                item = new ItemStack(Material.NETHERITE_AXE);
                meta = item.getItemMeta();
                meta.setDisplayName("§c🔥 Inferno Axe");
                meta.setCustomModelData(1002);
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
                break;

            // 🔨 STORM HAMMER
            case "hammer":
                item = new ItemStack(Material.NETHERITE_AXE);
                meta = item.getItemMeta();
                meta.setDisplayName("§b🔨 Storm Hammer");
                meta.setCustomModelData(1003);
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
                break;

            // 🌑 VOID REAPER
            case "void":
                item = new ItemStack(Material.NETHERITE_HOE);
                meta = item.getItemMeta();
                meta.setDisplayName("§5🌑 Void Reaper");
                meta.setCustomModelData(1004);
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
                break;

            // ☀ CELESTIAL BLADE
            case "celestial":
                item = new ItemStack(Material.NETHERITE_SWORD);
                meta = item.getItemMeta();
                meta.setDisplayName("§e☀ Celestial Blade");
                meta.setCustomModelData(1005);
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
                break;

            default:
                p.sendMessage("§cUnknown weapon!");
                return true;
        }

        p.getInventory().addItem(item);
        p.sendMessage("§aGiven: " + args[0]);
        return true;
    }
}
