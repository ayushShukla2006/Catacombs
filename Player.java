package game;

public class Player extends Character {

    public int level = 1;
    public int xp = 0;
    public int xpToNext = 30;

    public int bonusHp = 0;
    public int bonusAttack = 0;

    public Player(int x, int y) {
        super(x, y, 20, 5);
    }

    public boolean gainXp(int amount) {
        xp += amount;
        if (xp >= xpToNext) {
            levelUp();
            return true;
        }
        return false;
    }

    private void levelUp() {
        level++;
        xp = xp - xpToNext;
        xpToNext = 30 + level * 20;

        int hpGain = 5 + level;
        int atkGain = 2;

        maxHp += hpGain;
        hp = maxHp;
        attack += atkGain;
    }

    public String applyChestReward() {
        java.util.Random rand = new java.util.Random();
        boolean boostHp = rand.nextBoolean();

        if (boostHp) {
            int gain = 3 + level * 2 + rand.nextInt(level + 1);
            maxHp += gain;
            hp += gain;
            bonusHp += gain;
            return "+" + gain + " Max HP";
        } else {
            int gain = 1 + rand.nextInt(level + 1);
            attack += gain;
            bonusAttack += gain;
            return "+" + gain + " Attack";
        }
    }

    @Override
    public char getSymbol() {
        return '@';
    }
}