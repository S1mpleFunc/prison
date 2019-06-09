package listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import prison.PrisonMain;

import java.util.Random;
import java.util.UUID;

public class HandListener implements Listener {

    UUID uuid;
    Random random = new Random();
    ItemStack key = new ItemStack(Material.GHAST_TEAR);
    ItemMeta keymeta = key.getItemMeta();

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {
        keymeta.setDisplayName("§6Ключ");
        key.setItemMeta(keymeta);

        Block block = e.getBlock();
        Player p = e.getPlayer();
        uuid = p.getUniqueId();
        if (PrisonMain.getInstance().getStats().containsKey(uuid))
            PrisonMain.getInstance().getStats().get(uuid).incBlock(block.getType().toString());
        else
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Упс... Произошла ошибка номер 3, обратитесь к персоналу сообщив номер ошибки.");

        if (block.getType().equals(Material.LOG)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(Material.LOG);
                    block.setData((byte) 13);
                }
            }.runTaskLater(PrisonMain.getInstance(), 10 * 20L);
        }
        p.getInventory().addItem(new ItemStack(block.getType()));

        if (random.nextInt(200) == 1) {
            p.getInventory().addItem(key);
            p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "§6Вам выпал ключ.");
        }
        block.setType(Material.AIR);
        e.setCancelled(true);
        p.updateInventory();
    }
    @EventHandler
    public void onBlockPlace (BlockPlaceEvent e) {
        if (e.getPlayer().isOp())
            return;
        e.setCancelled(true);
    }
}
