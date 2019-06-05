package listeners;

import menu.FractionMenu;
import menu.LevelMenu;
import menu.MineMenu;
import menu.UpgradeMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import prison.PrisonMain;
import prison.PrisonPlayer;
import prison.PrisonScoreboard;

import java.util.Random;

public class InteractListener implements Listener {


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
            MineMenu.getInstance().openPlayerGUI(e.getPlayer());
            return;
        }
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getState() instanceof Sign) {
            Player p = e.getPlayer();
            if (!PrisonMain.getInstance().getStats().containsKey(p.getUniqueId())) return;
            PrisonPlayer prisonPlayer = PrisonMain.getInstance().getStats().get(p.getUniqueId());
            ItemStack item;
            ItemStack air = new ItemStack(Material.AIR);
            double summary = 0;
            for (int i = 0; i < 36; i++) {
                item = p.getInventory().getItem(i);
                if (p.getInventory().getItem(i) != null && PrisonMain.getInstance().getConfig().getString("prices.list").contains(item.getType().toString())) {
                    p.getInventory().setItem(i, air);
                    float price = (float) PrisonMain.getInstance().getConfig().getDouble("prices." + item.getType());
                    summary += price * item.getAmount() * PrisonMain.getInstance().getGLOBAL_MONEY_BOOSTER();
                    prisonPlayer.setGold(prisonPlayer.getGold() + (price * item.getAmount() * PrisonMain.getInstance().getGLOBAL_MONEY_BOOSTER()));
                    p.updateInventory();
                }
            }
            p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы продали на §6" + summary + "§7$.");
        } else if (e.getClickedBlock().getType().equals(Material.ENDER_CHEST) && e.getPlayer().getItemInHand().getType().equals(Material.GHAST_TEAR)) {
            Player p = e.getPlayer();
            PrisonMain.getInstance().deleteOneItem(p);
            Inventory i = Bukkit.createInventory(null, 45, "§fСундук");
            Random random = new Random();
            int variants = PrisonMain.getInstance().getConfig().getInt("drop.items");
            int choosen = random.nextInt(variants) + 1;
            ItemStack item = new ItemStack(Material.getMaterial(PrisonMain.getInstance().getConfig().getString("drop." + choosen + ".material")));
            item.setAmount(random.nextInt(PrisonMain.getInstance().getConfig().getInt("drop." + choosen + ".max_amount")) + 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(PrisonMain.getInstance().getConfig().getString("drop." + choosen + ".name"));
            item.setItemMeta(meta);
            i.addItem(item);
            p.openInventory(i);
            e.setCancelled(true);
        } else if (e.getClickedBlock().getType().equals(Material.LEVER)) {
            e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            e.getPlayer().sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы справили нужду!");
        } else if (e.getClickedBlock().getType().equals(Material.BED_BLOCK)) {
            e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
            e.getPlayer().sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы хорошо выспались!");
        } else if (e.getPlayer().getItemInHand() != null) {
            Player p = e.getPlayer();
            ItemStack itemStack = p.getItemInHand();
            try {
                if (itemStack.getType().equals(Material.GOLD_BLOCK) && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).toLowerCase().contains("бустер блоков")) {
                    PrisonMain.getInstance().deleteOneItem(p);
                    PrisonMain.getInstance().setGLOBAL_BLOCK_BOOSTER(2);
                    PrisonMain.getInstance().setBLOCK_BOOSTER_END(60 * 30);
                    Bukkit.broadcastMessage(PrisonMain.getInstance().getInfoPrefix() + p.getName() + " активировал бустер блоков на 30 минут.");
                } else if (itemStack.getType().equals(Material.GOLD_BLOCK) && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).toLowerCase().contains("бустер денег")) {
                    PrisonMain.getInstance().deleteOneItem(p);
                    PrisonMain.getInstance().setGLOBAL_MONEY_BOOSTER(2);
                    PrisonMain.getInstance().setMONEY_BOOSTER_END(60 * 30);
                    Bukkit.broadcastMessage(PrisonMain.getInstance().getInfoPrefix() + p.getName() + " активировал бустер денег на 30 минут.");
                }
            } catch (Exception ex) {}
        }
    }
    @EventHandler
    public void onSleep (PlayerBedEnterEvent e) {
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getPlayer().getLocation().getBlock().getType().equals(Material.STATIONARY_WATER))
            e.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
    }
    @EventHandler
    public void onClick (InventoryClickEvent e)
    {
        Player p = (Player) e.getWhoClicked();

        Inventory open = e.getInventory();
        ItemStack item = e.getCurrentItem();
        if (open == null || item == null)
            return;
        if (item.getType().equals(Material.STAINED_GLASS_PANE)) {
            e.setCancelled(true);
            return;
        }
        switch (ChatColor.stripColor(open.getName()).toLowerCase()) {
            case "шахты":
                MineMenu.getInstance().menuHandler(p, item);
                e.setCancelled(true);
                break;
            case "улучшение предмета":
                UpgradeMenu.getInstance().menuHandler(p);
                e.setCancelled(true);
                break;
            case "улучшение уровня":
                LevelMenu.getInstance().menuHandler(p);
                e.setCancelled(true);
                break;
            case  "выбор фракции":
                FractionMenu.getInstance().menuHandler(p, item);
                e.setCancelled(true);
                break;
        }
    }
}
