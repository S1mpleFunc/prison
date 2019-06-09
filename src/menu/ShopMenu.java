package menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import prison.PrisonMain;
import prison.PrisonPlayer;
import prison.PrisonVariables;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ShopMenu {

    private ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE);
    private ItemStack infolist = new ItemStack(Material.PAPER);
    private ItemMeta infometa = infolist.getItemMeta();

    public void openPlayerGUI (Player p) {
        if (((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(p.getUniqueId())) {

            PrisonPlayer prisonPlayer = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
            infometa.setDisplayName("§lИнформация §e§l" + p.getName());
            infometa.setLore(Arrays.asList("",
                    "§f* Уровень: §b§l" + prisonPlayer.getLevel(),
                    "§f* Денег: §a§l" + prisonPlayer.getGold() + "$",
                    "§f* Убийств: §c§l" + prisonPlayer.getKills(),
                    "§f* Блоков: §6§l" + prisonPlayer.getBlocksValue(),
                    "§f* Индентификатор: §l" + prisonPlayer.getId())
            );
            infolist.setItemMeta(infometa);
            //Инвентарь с живыми игроками
            Inventory i = Bukkit.createInventory(null, 54, "§b§lМагазин");

            //Заполнение инвентаря стеклом и головами игроков
            for (int u = 0; u < 9; u++)
                i.setItem(u, empty);
            for (int u = 1; u < PrisonMain.getInstance().getConfig().getInt("shop.amount") + 1; u++) {
                ItemStack item = new ItemStack(Material.getMaterial(PrisonMain.getInstance().getConfig().getString("shop." + u +".material")),
                                               PrisonMain.getInstance().getConfig().getInt("shop." + u +".amount"));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(PrisonMain.getInstance().getConfig().getString("shop." + u +".name"));
                meta.setLore(Arrays.asList("", "§f§lЦена: §a" + PrisonMain.getInstance().getConfig().getDouble("shop." + u +".cost") + "$", "§7У вас денег: " + prisonPlayer.getGold() + "$"));
                meta.spigot().setUnbreakable(true);
                item.setItemMeta(meta);
                i.setItem(18 + u, item);
            }
            i.setItem(40, infolist);
            for (int u = 45; u < 54; u++)
                i.setItem(u, empty);
            //Открытие игрока
            p.openInventory(i);
        } else
            p.sendMessage(PrisonVariables.ERROR.getO() + "Упс... Произошла ошибка номер 7, обратитесь к персоналу сообщив номер ошибки.");
    }

    public void menuHandler(Player p, ItemStack itemStack) {
        PrisonPlayer prisonPlayer = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
        float cost = Float.parseFloat(ChatColor.stripColor(itemStack.getItemMeta().getLore().get(1).split(" ")[1]).replace("$", ""));

        if (prisonPlayer.getGold() < cost) {
            p.sendMessage(PrisonVariables.ERROR.getO() + "Вам не хватает " + (cost - prisonPlayer.getGold()) + "$.");
            return;
        }
        p.sendMessage(PrisonVariables.INFO.getO() + "Вы потратили " + cost + "$.");
        prisonPlayer.setGold(prisonPlayer.getGold() - cost);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList("", "§7§lПриобретено игроком " + p.getName(), "§7§l" + new Date()));
        itemStack.setItemMeta(meta);
        p.getInventory().addItem(itemStack);
        p.updateInventory();
        p.closeInventory();
    }
}
