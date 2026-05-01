package game;

import java.util.Random;

public class Enemy extends Character {

    private static final String[] NAME_POOL = {
        "Goblin", "Skeleton", "Orc", "Shade", "Troll",
        "Wraith", "Bat", "Kobold", "Ghoul", "Imp",
        "Bandit", "Slime", "Spider", "Wight", "Cultist"
    };

    public String name;
    public int xpReward;

    public Enemy(int x, int y, int floor) {
        super(x, y, baseHp(floor), baseAtk(floor));
        Random rand = new Random();
        this.name = NAME_POOL[rand.nextInt(NAME_POOL.length)];
        this.xpReward = 10 + (floor - 1) * 5;
    }

    private static int baseHp(int floor) {
        return 10 + (floor - 1) * 4;
    }

    private static int baseAtk(int floor) {
        return 3 + (floor - 1);
    }

    @Override
    public char getSymbol() {
        return 'E';
    }
}