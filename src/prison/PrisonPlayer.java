package prison;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.UUID;

public class PrisonPlayer {

    String block[];

    //Создание класса хранящего всю статистику игрока
    private int id;
    private int gold;
    private int level;
    private int kills;
    private int deaths;
    private String blocks;

    //                       id	        uuid	  gold	    level	   kills	  deaths	blocks
    public PrisonPlayer (int id, int gold, int level, int kills, int deaths,  String blocks) {
        this.id = id;
        this.gold = gold;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.blocks = blocks;

    }

    public int getId() {
        return id;
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

    public void incrimentBlock(Material material) {
        block = this.getBlocks().split(" ");
        String string = "";
        Integer value;

        for (int i = 0; i < block.length; i++) {
            if (block[i].equalsIgnoreCase(material.toString())) {
                value = Integer.parseInt(block[ i + 1 ]) + 1;
                block[i + 1] = value.toString();
            }
            string += block[i] + " ";
        }

        this.blocks = string;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public void setLevel(int level) {
        this.level = level;
    }
}
