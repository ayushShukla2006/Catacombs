package game;

public class Spawner {

    static void loadFloor() {
        Game.enemies.clear();
        Game.boss = null;

        boolean isBossFloor = (Game.floor == Game.BOSS_FLOOR);
        boolean isKeyFloor  = (Game.floor == Game.KEY_FLOOR);
        boolean hasStairs   = (Game.floor < Game.KEY_FLOOR);

        Game.map = Room.generateMap(hasStairs, isKeyFloor);
        Game.HEIGHT = Game.map.length;
        Game.WIDTH  = Game.map[0].length;

        Room.placeChests(2 + Game.floor / 2);

        spawnPlayer();

        if (isBossFloor) {
            spawnBoss();
            spawnEnemies(2);
            Logger.addMessage("--- Floor " + Game.floor + " : BOSS FLOOR ---");
            Logger.addMessage(Boss.BOSS_NAME + " awaits...");
        } else {
            spawnEnemies(4 + Game.floor);
            Logger.addMessage("--- Floor " + Game.floor + " ---");
            if (isKeyFloor) Logger.addMessage("The legendary KEY is here!");
        }
    }

    static void spawnPlayer() {
        if (Game.player == null) Game.player = new Player(0, 0);
        while (true) {
            int x = (int)(Math.random() * Game.WIDTH);
            int y = (int)(Math.random() * Game.HEIGHT);
            if (Game.map[y][x] == Room.FLOOR) {
                Game.player.setPosition(x, y);
                break;
            }
        }
    }

    static void spawnBoss() {
        int attempts = 0;
        while (true) {
            int x = (int)(Math.random() * Game.WIDTH);
            int y = (int)(Math.random() * Game.HEIGHT);
            int dist = Math.abs(x - Game.player.getX()) + Math.abs(y - Game.player.getY());
            if (Game.map[y][x] == Room.FLOOR && dist >= 15) {
                Game.boss = new Boss(x, y, Game.floor);
                break;
            }
            if (++attempts > 2000) {
                if (Game.map[y][x] == Room.FLOOR) { Game.boss = new Boss(x, y, Game.floor); break; }
            }
        }
    }

    static void spawnEnemies(int count) {
        int spawned = 0;
        int attempts = 0;
        while (spawned < count && attempts < 5000) {
            attempts++;
            int x = (int)(Math.random() * Game.WIDTH);
            int y = (int)(Math.random() * Game.HEIGHT);
            boolean bossHere = (Game.boss != null && Game.boss.getX() == x && Game.boss.getY() == y);
            if (Game.map[y][x] == Room.FLOOR
                    && Game.getEnemyAt(x, y) == null
                    && !bossHere
                    && !(Game.player.getX() == x && Game.player.getY() == y)) {
                Game.enemies.add(new Enemy(x, y, Game.floor));
                spawned++;
            }
        }
    }
}