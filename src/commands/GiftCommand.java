package commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import prison.PrisonMain;

public class GiftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) return false;
        if (strings.length != 1) return false;

        Player sender = (Player) commandSender;
        Player getter = Bukkit.getPlayer(strings[0]);
        if (!Bukkit.getOnlinePlayers().contains(getter)) {
            sender.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Игрок оффлайн, либо не существует.");
            return false;
        }
        ItemStack item = sender.getItemInHand();
        if (getter.getLocation().distance(sender.getLocation()) > 6) {
            sender.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Игрок слишком далеко.");
            return false;
        }
        sender.setItemInHand(new ItemStack(Material.AIR));

        getter.getInventory().addItem(item);
        getter.updateInventory();
        getter.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы получили предмет от " + sender.getName());

        sender.updateInventory();
        sender.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Предмет успешно передан.");

        return false;
    }
}
