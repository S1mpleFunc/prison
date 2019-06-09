package listener;

import menu.*;
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
import prison.PrisonVariables;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class InteractListener implements Listener {


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
            new MineMenu().openPlayerGUI(e.getPlayer());
            return;
        }
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getState() instanceof Sign) {
            Player p = e.getPlayer();
            if (!((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(p.getUniqueId())) return;
            PrisonPlayer prisonPlayer = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
            ItemStack item;
            ItemStack air = new ItemStack(Material.AIR);
            double summary = 0;
            for (int i = 0; i < 36; i++) {
                item = p.getInventory().getItem(i);
                if (p.getInventory().getItem(i) != null && PrisonMain.getInstance().getConfig().getString("prices.list").contains(item.getType().toString())) {
                    p.getInventory().setItem(i, air);
                    float price = (float) PrisonMain.getInstance().getConfig().getDouble("prices." + item.getType());
                    summary += price * item.getAmount() * (int)PrisonVariables.GLOBAL_MONEY_BOOSTER.getO();
                    prisonPlayer.setGold(prisonPlayer.getGold() + (price * item.getAmount() * (int)PrisonVariables.GLOBAL_MONEY_BOOSTER.getO()));
                    p.updateInventory();
                }
            }
            p.sendMessage(PrisonVariables.INFO.getO() + "Вы продали на §6" + summary + "§7$.");
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
            e.getPlayer().sendMessage(PrisonVariables.INFO.getO() + "Вы справили нужду!");
        } else if (e.getClickedBlock().getType().equals(Material.BED_BLOCK)) {
            e.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
            e.getPlayer().sendMessage(PrisonVariables.INFO.getO() + "Вы хорошо выспались!");
        } else if (e.getPlayer().getItemInHand() != null) {
            Player p = e.getPlayer();
            ItemStack itemStack = p.getItemInHand();
            try {
                if (itemStack.getType().equals(Material.GOLD_BLOCK) && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).toLowerCase().contains("бустер блоков")) {
                    PrisonMain.getInstance().deleteOneItem(p);
                    PrisonVariables.GLOBAL_BLOCK_BOOSTER.setO(2);
                    PrisonVariables.BLOCK_BOOSTER_END.setO(60 * 30);
                    Bukkit.broadcastMessage(PrisonVariables.INFO.getO() + p.getName() + " активировал бустер блоков на 30 минут.");
                } else if (itemStack.getType().equals(Material.GOLD_BLOCK) && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).toLowerCase().contains("бустер денег")) {
                    PrisonMain.getInstance().deleteOneItem(p);
                    PrisonVariables.GLOBAL_MONEY_BOOSTER.setO(2);
                    PrisonVariables.MONEY_BOOSTER_END.setO(60 * 30);
                    Bukkit.broadcastMessage(PrisonVariables.INFO.getO() + p.getName() + " активировал бустер денег на 30 минут.");
                } else if (itemStack.getType().equals(Material.BOOK) && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).toLowerCase().contains("подвал")) {
                    PrisonMain.getInstance().deleteOneItem(p);
                    ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId()).setCanEnter(1);
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
        if (item.getType().equals(Material.STAINED_GLASS_PANE) || item.getType().equals(Material.PAPER)) {
            e.setCancelled(true);
            return;
        }
        switch (ChatColor.stripColor(open.getName()).toLowerCase()) {
            case "шахты":
                new MineMenu().menuHandler(p, item);
                e.setCancelled(true);
                break;
            case "улучшение предмета":
                new UpgradeMenu().menuHandler(p);
                e.setCancelled(true);
                break;
            case "улучшение уровня":
                new LevelMenu().menuHandler(p);
                e.setCancelled(true);
                break;
            case  "выбор фракции":
                new FractionMenu().menuHandler(p, item);
                e.setCancelled(true);
                break;
            case "магазин":
                new ShopMenu().menuHandler(p, item);
                e.setCancelled(true);
                break;
        }
    }
}
