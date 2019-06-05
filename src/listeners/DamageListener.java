package listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import prison.PrisonMain;
import prison.PrisonPlayer;

public class DamageListener implements Listener {

    @EventHandler
    public void playerDamagePlayer (EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (PrisonMain.getInstance().getStats().containsKey(e.getEntity().getUniqueId()) && PrisonMain.getInstance().getStats().containsKey(e.getDamager().getUniqueId())) {
                PrisonPlayer player = PrisonMain.getInstance().getStats().get(e.getEntity().getUniqueId());
                PrisonPlayer damager = PrisonMain.getInstance().getStats().get(e.getDamager().getUniqueId());
                if (player.getPrisonClansLocation() != null && damager.getPrisonClansLocation() != null && player.getPrisonClans().equals(damager.getPrisonClans()))
                    e.setCancelled(true);
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            if (PrisonMain.getInstance().getStats().containsKey(e.getEntity().getUniqueId()) && PrisonMain.getInstance().getStats().containsKey(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId())) {
                PrisonPlayer player = PrisonMain.getInstance().getStats().get(e.getEntity().getUniqueId());
                PrisonPlayer damager = PrisonMain.getInstance().getStats().get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
                if (player.getPrisonClansLocation() != null && damager.getPrisonClansLocation() != null && player.getPrisonClans().equals(damager.getPrisonClans()))
                    e.setCancelled(true);
            }
        }
    }
}
