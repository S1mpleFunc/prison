package listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import prison.PrisonMain;
import prison.PrisonPlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

    HashMap<UUID, ItemStack[]> returnDrop = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        if (e.getEntityType() == EntityType.PLAYER) {
            Player player = e.getEntity();
            try {
                ItemStack[] items = new ItemStack[36];
                int i = 0;
                for (ItemStack item : e.getDrops()) {
                    if (item == null || item.getItemMeta() == null) continue;
                    if (PrisonMain.getInstance().getConfig().getString("no_drop").contains(item.getType().toString())) {
                        items[i] = item;
                        i++;
                    }
                }
                for (ItemStack itemStack : items)
                    e.getDrops().remove(itemStack);

                returnDrop.put(player.getUniqueId(), items);
                PrisonPlayer prisonPlayer = PrisonMain.getInstance().getStats().get(player.getUniqueId());
                if (prisonPlayer.getGold() >= prisonPlayer.getLevel())
                    prisonPlayer.setGold(prisonPlayer.getGold() - prisonPlayer.getLevel());
                player.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы были убиты " + player.getKiller().getName() + " и потеряли " + prisonPlayer.getLevel() + "$.");
                PrisonPlayer prisonKiller = PrisonMain.getInstance().getStats().get(player.getKiller().getUniqueId());
                prisonKiller.setGold(prisonKiller.getGold() + prisonPlayer.getLevel());
                player.getKiller().sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы убили " + player.getName() + " и получили " + prisonPlayer.getLevel() + "$.");
                prisonKiller.setKills(prisonKiller.getKills() + 1);
            } catch (Exception ex) {
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (!returnDrop.containsKey(e.getPlayer().getUniqueId())) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack item : returnDrop.get(e.getPlayer().getUniqueId())) {
                    if (item == null) continue;
                    e.getPlayer().getInventory().addItem(item);
                }
                returnDrop.remove(e.getPlayer().getUniqueId());
            }
        }.runTaskLaterAsynchronously(PrisonMain.getInstance(), 5);
    }
}
