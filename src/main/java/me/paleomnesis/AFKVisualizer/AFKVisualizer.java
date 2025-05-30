package me.paleomnesis.AFKVisualizer;

import me.paleomnesis.AFKVisualizer.afk.AfkManager;
import me.paleomnesis.AFKVisualizer.afk.ParticleEffectTask;
import me.paleomnesis.AFKVisualizer.commands.AfkStatusCommand;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

public final class AFKVisualizer extends JavaPlugin {

    private AfkManager afkManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initAfkManager();
        registerCommands();
        registerTasks();
        getLogger().info("AFKVisualizer enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AFKVisualizer disabled.");
    }

    private void initAfkManager() {
        this.afkManager = new AfkManager();
        getServer().getPluginManager().registerEvents(afkManager, this);
    }

    private void registerCommands() {
        getCommand("afkstatus").setExecutor(new AfkStatusCommand(afkManager));
    }

    private void registerTasks() {
        long thresholdMillis = 1000L * getConfig().getInt("afk-threshold-seconds", 120);
        String particleName = getConfig().getString("particle-type", "CLOUD");
        long intervalTicks = getConfig().getLong("particle-interval-ticks", 40);

        Particle particle = parseParticle(particleName);
        new ParticleEffectTask(this, afkManager, thresholdMillis, particle).runTaskTimer(this, 0L, intervalTicks);
    }

    private Particle parseParticle(String name) {
        try {
            return Particle.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            getLogger().warning("Invalid particle type in config: " + name + ". Falling back to CLOUD.");
            return Particle.CLOUD;
        }
    }

}
