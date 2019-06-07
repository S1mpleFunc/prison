package listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import prison.PrisonMain;
import prison.PrisonPlayer;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat (AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PrisonPlayer prisonPlayer = PrisonMain.getInstance().getStats().get(p.getUniqueId());
        e.setFormat("§b§l" + prisonPlayer.getLevel() + "§bLVL " + prisonPlayer.getPrisonClanName() + " §f§l" + p.getName().toUpperCase() + " §f§l>> §7" + e.getMessage());
    }
}
