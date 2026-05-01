package game;

public abstract class Character {
    protected int x, y;
    protected int hp;
    protected int maxHp;
    protected int attack;

    public Character(int x, int y, int hp, int attack) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract char getSymbol();
}