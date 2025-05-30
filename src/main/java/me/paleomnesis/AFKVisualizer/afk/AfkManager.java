package me.paleomnesis.AFKVisualizer.afk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

public final class AfkManager implements Listener {

    private final Map<UUID, Long> lastActivityMap = new ConcurrentHashMap<>();
    private static final double MOVEMENT_EPSILON = 1e-4;

    public void updateActivity(Player player) {
        lastActivityMap.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public boolean isAfk(Player player, long thresholdMillis) {
        long lastActive = lastActivityMap.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
        return (System.currentTimeMillis() - lastActive) >= thresholdMillis;
    }

    public List<Player> getAfkPlayers(long thresholdMillis) {
        List<Player> afkPlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isAfk(player, thresholdMillis)) {
                afkPlayers.add(player);
            }
        }
        return afkPlayers;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom() == null || event.getTo() == null) return;

        if (event.getFrom().toVector().distanceSquared(event.getTo().toVector()) > MOVEMENT_EPSILON) {
            updateActivity(event.getPlayer());
        }
    }
}
