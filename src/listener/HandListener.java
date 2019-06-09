package listener;

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
import prison.PrisonPlayer;
import prison.PrisonVariables;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class HandListener implements Listener {

    private UUID uuid;
    private Random random = new Random();
    private ItemStack key = new ItemStack(Material.GHAST_TEAR);
    private ItemMeta keymeta = key.getItemMeta();

    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {
        keymeta.setDisplayName("§6Ключ");
        key.setItemMeta(keymeta);

        Block block = e.getBlock();
        Player p = e.getPlayer();
        uuid = p.getUniqueId();
        if (((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(uuid))
            ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(uuid).incBlock(block.getType().toString());
        else
            p.sendMessage(PrisonVariables.INFO.getO() + "Упс... Произошла ошибка номер 3, обратитесь к персоналу сообщив номер ошибки.");

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
            p.sendMessage(PrisonVariables.INFO.getO() + "§6Вам выпал ключ.");
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
