package menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import prison.PrisonPlayer;
import prison.PrisonUpgrade;
import prison.PrisonVariables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class UpgradeMenu {

    private ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE);
    private ItemStack star = new ItemStack(Material.NETHER_STAR);
    private ItemMeta starmeta = star.getItemMeta();
    private PrisonUpgrade upgrade = new PrisonUpgrade();

    public void openPlayerGUI (Player p) {
        if (((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(p.getUniqueId())) {
            PrisonPlayer prisonPlayer = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());

            starmeta.setDisplayName("§b§lУлучшить");
            starmeta.setLore(Arrays.asList("§f§lЦена: §6§l" + upgrade.getCost(p) + "$.",
                                           "§7У вас денег: " + prisonPlayer.getGold() + "$."));
            star.setItemMeta(starmeta);

            //Инвентарь с живыми игроками
            Inventory i = Bukkit.createInventory(null, 27, "§b§lУлучшение предмета");

            //Заполнение инвентаря стеклом и головами игроков
            for (int u = 0; u < 9; u++)
                i.setItem(u, empty);
            i.setItem(13, star);
            for (int u = 18; u < 27; u++)
                i.setItem(u, empty);
            //Открытие игрока
            p.openInventory(i);
        } else
            p.sendMessage(PrisonVariables.ERROR.getO() + "Упс... Произошла ошибка номер 7, обратитесь к персоналу сообщив номер ошибки.");
    }

    public void menuHandler(Player p) {
        //Обработка возможных предметов
        upgrade.upgradeItem(p, p.getItemInHand());
        p.closeInventory();
    }
}
