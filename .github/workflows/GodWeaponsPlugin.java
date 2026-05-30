package com.godweapons;

import org.bukkit.plugin.java.JavaPlugin;

public class GodWeaponsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new WeaponListener(this), this);
        getCommand("godweapon").setExecutor(new WeaponManager());
    }
}
