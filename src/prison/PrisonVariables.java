package prison;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public enum PrisonVariables {

    GLOBAL_TIME(0),

    PLAYER_STATS(new HashMap<UUID, PrisonPlayer>()),
    MINES(new LinkedList<PrisonMine>()),
    SCORES(new HashMap<UUID, Objective>()),
    BOSSES(new LinkedList<LivingEntity>()),

    GLOBAL_MONEY_BOOSTER(-1),
    GLOBAL_BLOCK_BOOSTER(-1),
    BLOCK_BOOSTER_END(-1),
    MONEY_BOOSTER_END(-1),

    INFO("§7[§ai§7] "),
    ERROR("§7[§c!§l§6*§7] "),
    FATAL_ERROR("§7[§c!§l§6**§7] "),

    STATEMENT(null),

    WORLD(Bukkit.getWorld("world")),
    ;

    private Object o;

    PrisonVariables(Object o) {
        this.o = o;
    }

    public Object getO() {
        return o;
    }
    public void setO(Object o) {
        this.o = o;
    }
}
