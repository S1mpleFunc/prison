package menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import prison.PrisonMain;
import prison.PrisonUpgrade;

import java.util.Arrays;

public class UpgradeMenu {

    ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE);
    ItemStack star = new ItemStack(Material.NETHER_STAR);
    ItemMeta starmeta = star.getItemMeta();

    public void openPlayerGUI (Player p) {
        if (PrisonMain.getInstance().getStats().containsKey(p.getUniqueId())) {

            starmeta.setDisplayName("§b§lУлучшить");
            starmeta.setLore(Arrays.asList("§f§lЦена: §6§l" + PrisonUpgrade.getInstance().getCost(p) + "$."));
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
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Упс... Произошла ошибка номер 7, обратитесь к персоналу сообщив номер ошибки.");
    }

    public void menuHandler(Player p) {
        //Обработка возможных предметов
        PrisonUpgrade.getInstance().upgradeItem(p, p.getItemInHand());
        p.closeInventory();
    }
    public static UpgradeMenu getInstance () {
        return new UpgradeMenu();
    }
}
