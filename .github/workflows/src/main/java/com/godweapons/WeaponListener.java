package com.godweapons;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class WeaponListener implements Listener {

    private final Map<UUID, Long> cooldown = new HashMap<>();
    private final JavaPlugin plugin;

    public WeaponListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean ready(UUID id, long ms) {
        return !cooldown.containsKey(id) || System.currentTimeMillis() - cooldown.get(id) > ms;
    }

    private void setCD(UUID id) {
        cooldown.put(id, System.currentTimeMillis());
    }

    // 💥 ON HIT ABILITIES
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player p)) return;

        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();
        Location loc = e.getEntity().getLocation();

        // ⚡ GOD SLAYER
        if (name.contains("God Slayer")) {
            loc.getWorld().strikeLightningEffect(loc);
            p.setHealth(Math.min(p.getMaxHealth(), p.getHealth() + 4));
        }

        // 🔥 INFERNO AXE
        if (name.contains("Inferno Axe")) {
            e.getEntity().setFireTicks(140);
            e.setDamage(e.getDamage() + 6);
        }

        // 🔨 STORM HAMMER
        if (name.contains("Storm Hammer")) {
            e.getEntity().setVelocity(e.getEntity().getVelocity().setY(1.4));
            loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
        }

        // 🌑 VOID REAPER
        if (name.contains("Void Reaper")) {
            for (Entity en : loc.getWorld().getNearbyEntities(loc, 4, 4, 4)) {
                if (en instanceof LivingEntity le && !le.equals(p)) {
                    Vector v = loc.toVector().subtract(le.getLocation().toVector()).multiply(0.5);
                    le.setVelocity(v);
                    le.damage(2);
                }
            }
        }
    }

    // ⚡ RIGHT CLICK ABILITIES
    @EventHandler
    public void onUse(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();
        UUID id = p.getUniqueId();
        Location loc = p.getLocation();

        // ☀ CELESTIAL BLADE (ULTIMATE MODE)
        if (name.contains("Celestial Blade")) {

            if (!ready(id, 45000)) return;
            setCD(id);

            p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 0));

            loc.getWorld().strikeLightningEffect(loc);

            for (Entity en : loc.getWorld().getNearbyEntities(loc, 6, 6, 6)) {
                if (en instanceof LivingEntity le && !le.equals(p)) {
                    le.damage(8, p);
                    le.getWorld().spawnParticle(Particle.END_ROD, le.getLocation(), 15);
                }
            }
        }

        // 🔥 INFERNO ACTIVE (fire wave)
        if (name.contains("Inferno Axe")) {

            if (!ready(id, 15000)) return;
            setCD(id);

            for (int i = 0; i < 6; i++) {
                Location f = loc.clone().add(loc.getDirection().multiply(i));

                loc.getWorld().spawnParticle(Particle.FLAME, f, 25);

                for (Entity en : loc.getWorld().getNearbyEntities(f, 2, 2, 2)) {
                    if (en instanceof LivingEntity le && !le.equals(p)) {
                        le.setFireTicks(100);
                        le.damage(5, p);
                    }
                }
            }
        }

        // 🔨 HAMMER ACTIVE (shockwave slam)
        if (name.contains("Storm Hammer")) {

            if (!ready(id, 25000)) return;
            setCD(id);

            loc.getWorld().createExplosion(loc, 2f, false, false);

            for (Entity en : loc.getWorld().getNearbyEntities(loc, 6, 6, 6)) {
                if (en instanceof LivingEntity le && !le.equals(p)) {
                    le.setVelocity(le.getLocation().toVector().subtract(loc.toVector()).multiply(1.3).setY(1));
                }
            }
        }

        // 🌑 VOID ACTIVE (black hole)
        if (name.contains("Void Reaper")) {

            if (!ready(id, 30000)) return;
            setCD(id);

            for (int t = 0; t < 40; t++) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {

                    for (Entity en : loc.getWorld().getNearbyEntities(loc, 6, 6, 6)) {
                        if (en instanceof LivingEntity le && !le.equals(p)) {
                            Vector v = loc.toVector().subtract(le.getLocation().toVector()).multiply(0.6);
                            le.setVelocity(v);
                            le.damage(2, p);
                        }
                    }

                    loc.getWorld().spawnParticle(Particle.PORTAL, loc, 40);

                }, t);
            }
        }
    }
}
