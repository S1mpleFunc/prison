package prison;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PrisonUpgrade {

    private ItemMeta meta;
    private String type;
    private Integer nextlevel;

    private JavaPlugin plugin;

    public PrisonUpgrade (PrisonMain prison) {
        this.plugin = prison;
    }
    public PrisonUpgrade () {
    }
    public void upgradeItem (Player p, ItemStack item) {
        init(p);
        try {
            PrisonPlayer prisonPlayer = ((HashMap<UUID, PrisonPlayer>)PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
            int cost = plugin.getConfig().getInt("items." + type + "." + nextlevel + ".cost");
            if (prisonPlayer.getGold() < cost) {
                p.sendMessage(PrisonVariables.ERROR.getO() + "Вам не хватает " + (cost - prisonPlayer.getGold()) + "$.");
                return;
            }

            String[] blocks = prisonPlayer.getBlocks().split(" ");
            boolean flag = false;
            for (int i = 0; i < blocks.length; i++) {
                if (i % 2 == 0) {
                    int need_blocks = plugin.getConfig().getInt("items." + type + "." + nextlevel + ".blocks." + blocks[i]);
                    int have_blocks = Integer.parseInt(blocks[i + 1]);
                    if (need_blocks > have_blocks) {
                        p.sendMessage(PrisonVariables.ERROR.getO() + "Вам не хватает еще " + (need_blocks - have_blocks) + " блоков " + blocks[i] + ".");
                        flag = true;
                    }
                }
            }
            if (flag) return;
            prisonPlayer.setGold(prisonPlayer.getGold() - cost);
            p.sendMessage(PrisonVariables.INFO.getO() + "Потрачено " + cost + "$.");
            if (plugin.getConfig().getString("items." + type + "." + nextlevel + ".effect").equalsIgnoreCase("null")) {
                meta.removeEnchant(Enchantment.getByName(plugin.getConfig().getString("items." + type + "." + (nextlevel-1) + ".effect.enchantment")));
            } else
                meta.addEnchant(Enchantment.getByName(plugin.getConfig().getString("items." + type + "." + nextlevel + ".effect.enchantment")),
                        plugin.getConfig().getInt("items." + type + "." + nextlevel + ".effect.level"), true);
            //meta.spigot().setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setDisplayName(plugin.getConfig().getString("items." + type + "." + nextlevel + ".name"));
            item.setItemMeta(meta);
            item.setType(Material.getMaterial(plugin.getConfig().getString("items." + type + "." + nextlevel + ".material")));
            PrisonScoreboard.getInstance().updateScoreboard(p);
        } catch (Exception e) {
            p.sendMessage(PrisonVariables.ERROR.getO() + "Этот предмет нельзя улучшить.");
        }
    }

    public int getCost (Player p) {
        init(p);
        return plugin.getConfig().getInt("items." + type + "." + nextlevel + ".cost");
    }
    private void init (Player p) {
        try {
            meta = p.getItemInHand().getItemMeta();
            type = meta.getDisplayName().split(" ")[1];
            nextlevel = Integer.parseInt(meta.getDisplayName().split(" ")[2]) + 1;
        } catch (Exception e) {
            p.sendMessage(PrisonVariables.INFO.getO() + "Этот предмет нельзя улучшить.");
        }
    }
}
