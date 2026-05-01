package game;

public class Boss extends Enemy {

    public static final String BOSS_NAME = "The Dungeon Lord";
    public static final int VIEW_RANGE = 12;

    public Boss(int x, int y, int floor) {
        super(x, y, floor);
        this.hp     = (10 + (floor - 1) * 4) * 5;
        this.maxHp  = this.hp;
        this.attack = (3 + (floor - 1)) * 2;
        this.name   = BOSS_NAME;
        this.xpReward = 80 + floor * 20;
    }

    @Override
    public char getSymbol() {
        return 'B';
    }

    public boolean canSee(int px, int py) {
        int dx = Math.abs(getX() - px);
        int dy = Math.abs(getY() - py);
        return dx + dy <= VIEW_RANGE;
    }
}