package prison;

import java.util.HashMap;
import java.util.UUID;

public class PrisonPlayer {

    //Создание класса хранящего всю статистику игрока
    private int id;
    private String uuid;
    private int gold;
    private int level;
    private int kills;
    private int deaths;
    private String blocks;

    //                       id	        uuid	  gold	    level	   kills	  deaths	blocks
    public PrisonPlayer (int id, String uuid, int gold, int level, int kills, int deaths,  String blocks) {
        this.id = id;
        this.uuid = uuid;
        this.gold = gold;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.blocks = blocks;

    }

    public int getId() {
        return id;
    }
    public String getUuid() {
        return uuid;
    }
    public int getGold() {
        return gold;
    }
    public int getLevel() {
        return level;
    }
    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public String getBlocks() {
        return blocks;
    }
}
