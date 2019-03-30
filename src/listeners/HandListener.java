package listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import prison.PrisonMain;

import java.util.UUID;

public class HandListener implements Listener {

    PrisonMain plugin = PrisonMain.getPlugin(PrisonMain.class);
    UUID uuid;

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {
        uuid = e.getPlayer().getUniqueId();
        if (plugin.stats.containsKey(uuid))
            plugin.stats.get(uuid).incrimentBlock(e.getBlock().getType());
        else
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error")) + "Упс... Произошла ошибка номер 3, обратитесь к персоналу сообщив номер ошибки.");

        e.getPlayer().sendMessage("ГГ");
    }
}
