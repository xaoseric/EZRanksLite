package me.clip.ezrankslite.scoreboard;

import com.google.common.base.Charsets;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EZOfflinePlayer implements OfflinePlayer {

    private final String playerName;

    public EZOfflinePlayer(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public String getName() {
        return playerName;
    }

    @Override
    public UUID getUniqueId() {
        return UUID.nameUUIDFromBytes(playerName.getBytes(Charsets.UTF_8));
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public void setBanned(boolean banned) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }

    @Override
    public void setWhitelisted(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player getPlayer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getFirstPlayed() {
        return System.currentTimeMillis();
    }

    @Override
    public long getLastPlayed() {
        return System.currentTimeMillis();
    }

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    @Override
    public Location getBedSpawnLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException();
    }
}
