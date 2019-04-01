package listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import prison.PrisonMain;

import java.util.UUID;

public class HandListener implements Listener {

    PrisonMain plugin = PrisonMain.getPlugin(PrisonMain.class);
    UUID uuid;

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {
        Block block = e.getBlock();
        uuid = e.getPlayer().getUniqueId();
        if (plugin.stats.containsKey(uuid))
            plugin.stats.get(uuid).incrimentBlock(block.getType().toString());
        else
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error")) + "Упс... Произошла ошибка номер 3, обратитесь к персоналу сообщив номер ошибки.");

        if (block.getType().equals(Material.LOG)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(Material.LOG);
                }
            }.runTaskLaterAsynchronously(plugin, 10 * 20L);
        }
        e.getPlayer().getInventory().addItem(new ItemStack(block.getType()));

        block.setType(Material.AIR);
        e.setCancelled(true);
    }
    @EventHandler
    public void onBlockOlace (BlockPlaceEvent e) {
        if (e.getPlayer().isOp())
            return;
        e.setCancelled(true);
    }
}
