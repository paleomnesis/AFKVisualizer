package me.paleomnesis.AFKVisualizer.afk;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;

public final class ParticleEffectTask extends BukkitRunnable {

    private static final int PARTICLE_COUNT = 5;
    private static final double PARTICLE_OFFSET = 0.3;
    private static final double Y_OFFSET = 2.0;
    private static final double EXTRA_SPEED = 0;

    private final AfkManager afkManager;
    private final Plugin plugin;
    private final long afkThresholdMillis;
    private final Particle particle;

    public ParticleEffectTask(Plugin plugin, AfkManager afkManager, long afkThresholdMillis, Particle particle) {
        this.plugin = plugin;
        this.afkManager = afkManager;
        this.afkThresholdMillis = afkThresholdMillis;
        this.particle = particle;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!afkManager.isAfk(player, afkThresholdMillis)) continue;

            World world = player.getWorld();
            Location loc = player.getLocation();
            double x = loc.getX();
            double y = loc.getY() + Y_OFFSET;
            double z = loc.getZ();

            world.spawnParticle(
                    particle,
                    x, y, z,
                    PARTICLE_COUNT,
                    PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET,
                    EXTRA_SPEED
            );
        }
    }
}
