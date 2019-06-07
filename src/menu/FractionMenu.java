package menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import prison.PrisonFractions;
import prison.PrisonMain;

public class FractionMenu {

    private ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE);
    private ItemStack item;

    public void openPlayerGUI (Player p) {
        if (PrisonMain.getInstance().getStats().containsKey(p.getUniqueId())) {

            Inventory i = Bukkit.createInventory(null, 27, "§b§lВыбор фракции");

            for (int u = 0; u < 9; u++)
                i.setItem(u, empty);
            int s = 0;
            for (PrisonFractions clan : PrisonFractions.values()) {
                if (clan.getLocation() == null) continue;
                item = new ItemStack(clan.getMaterial());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(clan.getName());
                item.setItemMeta(meta);
                i.setItem(11 + s, item);
                s += 2;
            }
            for (int u = 18; u < 27; u++)
                i.setItem(u, empty);
            //Открытие игрока
            p.openInventory(i);
        } else
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Упс... Произошла ошибка номер 9, обратитесь к персоналу сообщив номер ошибки.");
    }

    public void menuHandler(Player p, ItemStack currentItem) {
        for (PrisonFractions clan : PrisonFractions.values()) {
            if (clan.getMaterial() == null) continue;
            if (clan.getMaterial().equals(currentItem.getType())) {
                if (PrisonMain.getInstance().getStats().containsKey(p.getUniqueId())) {
                    if (PrisonMain.getInstance().getStats().get(p.getUniqueId()).getLevel() > 5)
                        PrisonMain.getInstance().getStats().get(p.getUniqueId()).setClan(clan);

                    else
                        p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Нужен хотя бы 6 уровень.");
                }
            }
        }

        p.closeInventory();
    }
    public static FractionMenu getInstance () {
        return new FractionMenu();
    }
}
