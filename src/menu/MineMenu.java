package menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import prison.PrisonMain;
import prison.PrisonMine;
import prison.PrisonPlayer;

import java.util.Arrays;

public class MineMenu {

    ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE);
    ItemStack none = new ItemStack(Material.STAINED_CLAY);
    ItemMeta nonemeta = none.getItemMeta();
    ItemStack infolist = new ItemStack(Material.PAPER);
    ItemMeta infometa = infolist.getItemMeta();

    public void openPlayerGUI (Player p) {
        if (PrisonMain.getInstance().getStats().containsKey(p.getUniqueId())) {
            nonemeta.setDisplayName("НЕДОСТУПНО");
            none.setItemMeta(nonemeta);

            PrisonPlayer prisonPlayer = PrisonMain.getInstance().getStats().get(p.getUniqueId());

            infometa.setDisplayName("Информация " + p.getName());
            infometa.setLore(Arrays.asList("",
                    ChatColor.translateAlternateColorCodes('&', "&f* Уровень: " + prisonPlayer.getLevel()),
                    ChatColor.translateAlternateColorCodes('&', "&f* Денег: " + prisonPlayer.getGold()),
                    ChatColor.translateAlternateColorCodes('&', "&f* Убийств: " + prisonPlayer.getKills()),
                    ChatColor.translateAlternateColorCodes('&', "&f* Блоков: " + prisonPlayer.getBlocksValue()),
                    ChatColor.translateAlternateColorCodes('&', "&f* Индентификатор: " + prisonPlayer.getId())
            ));
            infolist.setItemMeta(infometa);

            //Инвентарь с живыми игроками
            Inventory i = Bukkit.createInventory(null, 54, "§b§lШахты");

            //Заполнение инвентаря стеклом и головами игроков
            for (int u = 0; u < 9; u++)
                i.setItem(u, empty);
            for (int u = 19; u < 26; u++)
                i.setItem(u, none);
            for (int u = 29; u < 34; u++)
                i.setItem(u, none);
            for (int j = 0; j < PrisonMain.getInstance().getMines().size(); j++) {
                PrisonMine prisonMine = PrisonMain.getInstance().getMines().get(j);
                if (prisonPlayer.getLevel() >= prisonMine.getMlevel()) {
                    ItemStack mine = new ItemStack(prisonMine.getMaterial());
                    ItemMeta minemeta = mine.getItemMeta();
                    minemeta.setDisplayName(prisonMine.getName());
                    minemeta.setLore(Arrays.asList("",
                            ChatColor.translateAlternateColorCodes('&', "&f* Минимальный уровень: " + prisonMine.getMlevel()),
                            ChatColor.translateAlternateColorCodes('&', "&f* Наличте ПВП: " + prisonMine.getPvp())));
                    mine.setItemMeta(minemeta);

                    i.setItem(19 + j, mine);
                }
            }
            i.setItem(40, infolist);
            for (int u = 45; u < 54; u++)
                i.setItem(u, empty);
            p.openInventory(i);
        } else
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Упс... Произошла ошибка номер 6, обратитесь к персоналу сообщив номер ошибки.");
    }

    public void menuHandler(Player p, ItemStack item) {
        Material type = item.getType();
        PrisonMain.getInstance().getMines().stream().filter(mine -> mine.getMaterial().equals(type)).forEach(mine -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));
                p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы будете телепортированы через 5 секунд.");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.teleport(mine.getMineLocation());
                        p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы были телепортированы на " + mine.getName());
                    }
                }.runTaskLaterAsynchronously(PrisonMain.getInstance(), 5 * 20L);
            });

        p.closeInventory();
    }
    public static MineMenu getInstance () {
        return new MineMenu();
    }
}
