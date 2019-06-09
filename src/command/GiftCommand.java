package command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import prison.PrisonMain;
import prison.PrisonVariables;

public class GiftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) return false;
        if (strings.length != 1) return false;

        Player sender = (Player) commandSender;
        Player getter = Bukkit.getPlayer(strings[0]);
        if (!Bukkit.getOnlinePlayers().contains(getter)) {
            sender.sendMessage(PrisonVariables.ERROR.getO() + "Игрок оффлайн, либо не существует.");
            return false;
        }
        ItemStack item = sender.getItemInHand();
        if (getter.getLocation().distance(sender.getLocation()) > 6) {
            sender.sendMessage(PrisonVariables.ERROR.getO() + "Игрок слишком далеко.");
            return false;
        }
        sender.setItemInHand(new ItemStack(Material.AIR));

        getter.getInventory().addItem(item);
        getter.updateInventory();
        getter.sendMessage(PrisonVariables.INFO.getO() + "Вы получили предмет от " + sender.getName());

        sender.updateInventory();
        sender.sendMessage(PrisonVariables.INFO.getO() + "Предмет успешно передан.");

        return false;
    }
}
