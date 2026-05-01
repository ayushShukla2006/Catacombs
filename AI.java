package game;

public class AI {

    static void moveEnemies() {
        if (Game.boss != null) moveBoss();

        for (Enemy e : Game.enemies) {
            int dx = Math.abs(e.getX() - Game.player.getX());
            int dy = Math.abs(e.getY() - Game.player.getY());

            if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
                Game.player.hp -= e.attack;
                Logger.addMessage(e.name + " hits you for " + e.attack
                        + " dmg  [HP " + Game.player.hp + "/" + Game.player.maxHp + "]");
                continue;
            }

            int dir  = (int)(Math.random() * 4);
            int newX = e.getX();
            int newY = e.getY();
            if      (dir == 0) newY--;
            else if (dir == 1) newY++;
            else if (dir == 2) newX--;
            else               newX++;

            if (Game.inBounds(newX, newY)
                    && Game.map[newY][newX] == Room.FLOOR
                    && Game.getEnemyAt(newX, newY) == null
                    && !(Game.boss != null && Game.boss.getX() == newX && Game.boss.getY() == newY)) {
                e.setPosition(newX, newY);
            }
        }
    }

    static void moveBoss() {
        if (Game.boss == null) return;

        int bx = Game.boss.getX(), by = Game.boss.getY();
        int px = Game.player.getX(), py = Game.player.getY();

        int dx = Math.abs(bx - px);
        int dy = Math.abs(by - py);

        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
            Game.player.hp -= Game.boss.attack;
            Logger.addMessage(Boss.BOSS_NAME + " STRIKES you for " + Game.boss.attack
                    + " dmg!  [HP " + Game.player.hp + "/" + Game.player.maxHp + "]");
            return;
        }

        if (Game.boss.canSee(px, py)) {
            int newX = bx, newY = by;

            if (dx >= dy) {
                newX += (px > bx) ? 1 : -1;
            } else {
                newY += (py > by) ? 1 : -1;
            }

            if (Game.inBounds(newX, newY)
                    && Game.map[newY][newX] == Room.FLOOR
                    && Game.getEnemyAt(newX, newY) == null
                    && !(newX == px && newY == py)) {
                Game.boss.setPosition(newX, newY);
            }
            return;
        }

        int dir  = (int)(Math.random() * 4);
        int newX = bx, newY = by;
        if      (dir == 0) newY--;
        else if (dir == 1) newY++;
        else if (dir == 2) newX--;
        else               newX++;

        if (Game.inBounds(newX, newY)
                && Game.map[newY][newX] == Room.FLOOR
                && Game.getEnemyAt(newX, newY) == null) {
            Game.boss.setPosition(newX, newY);
        }
    }
}