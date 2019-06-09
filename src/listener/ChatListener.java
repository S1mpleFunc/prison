package listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import prison.PrisonPlayer;
import prison.PrisonVariables;

import java.util.HashMap;
import java.util.UUID;

public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncChat (AsyncPlayerChatEvent e) {
        if (e.getMessage().contains("%")) e.setMessage(e.getMessage().replace("%", " процентов"));
        Player p = e.getPlayer();
        PrisonPlayer prisonPlayer = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
        e.setFormat("§b§l" + prisonPlayer.getLevel() + "§bLVL " + prisonPlayer.getPrisonClanName() + " §f§l" + p.getName().toUpperCase() + " §f§l>> §7" + e.getMessage());
    }
}
