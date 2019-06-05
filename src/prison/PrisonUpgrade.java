package prison;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrisonUpgrade {
    ItemMeta meta;
    String type;
    Integer nextlevel;

    public void upgradeItem (Player p, ItemStack item) {
        init(p);
        try {
            PrisonPlayer prisonPlayer = PrisonMain.getInstance().getStats().get(p.getUniqueId());
            int cost = PrisonMain.getInstance().getConfig().getInt("items." + type + "." + nextlevel + ".cost");
            if (prisonPlayer.getGold() < cost) {
                p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вам не хватает " + (cost - prisonPlayer.getGold()) + "$.");
                return;
            }

            String[] blocks = prisonPlayer.getBlocks().split(" ");
            boolean flag = false;
            for (int i = 0; i < blocks.length; i++) {
                if (i % 2 == 0) {
                    int need_blocks = PrisonMain.getInstance().getConfig().getInt("items." + type + "." + nextlevel + ".blocks." + blocks[i]);
                    int have_blocks = Integer.parseInt(blocks[i + 1]);
                    if (need_blocks > have_blocks) {
                        p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Вам не хватает еще " + (need_blocks - have_blocks) + " блоков " + blocks[i] + ".");
                        flag = true;
                    }
                }
            }
            if (flag) return;
            prisonPlayer.setGold(prisonPlayer.getGold() - cost);
            p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Потрачено " + cost + "$.");
            if (PrisonMain.getInstance().getConfig().getString("items." + type + "." + nextlevel + ".effect").equalsIgnoreCase("null")) {
                meta.removeEnchant(Enchantment.getByName(PrisonMain.getInstance().getConfig().getString("items." + type + "." + (nextlevel-1) + ".effect.enchantment")));
            } else
                meta.addEnchant(Enchantment.getByName(PrisonMain.getInstance().getConfig().getString("items." + type + "." + nextlevel + ".effect.enchantment")),
                        PrisonMain.getInstance().getConfig().getInt("items." + type + "." + nextlevel + ".effect.level"), true);
            //meta.spigot().setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setDisplayName(PrisonMain.getInstance().getConfig().getString("items." + type + "." + nextlevel + ".name"));
            item.setItemMeta(meta);
            item.setType(Material.getMaterial(PrisonMain.getInstance().getConfig().getString("items." + type + "." + nextlevel + ".material")));
            PrisonScoreboard.getInstance().updateScoreboard(p);
        } catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Этот предмет нельзя улучшить.");
        }
    }
    public static PrisonUpgrade getInstance () {
        return new PrisonUpgrade();
    }

    public int getCost (Player p) {
        init(p);
        return PrisonMain.getInstance().getConfig().getInt("items." + type + "." + nextlevel + ".cost");
    }
    private void init (Player p) {
        try {
            meta = p.getItemInHand().getItemMeta();
            type = meta.getDisplayName().split(" ")[1];
            nextlevel = Integer.parseInt(meta.getDisplayName().split(" ")[2]) + 1;
        } catch (Exception e) {
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Этот предмет нельзя улучшить.");
        }
    }
}
