package game;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    static int WIDTH;
    static int HEIGHT;
    static char[][] map;

    static Player player;
    static ArrayList<Enemy> enemies = new ArrayList<>();
    static Boss boss = null;

    static int floor = 1;
    static final int BOSS_FLOOR = 5;
    static final int KEY_FLOOR  = 6;

    static String playerName = "Unknown";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Renderer.showTitleScreen(sc);
        Spawner.loadFloor();

        while (true) {
            Renderer.render();

            System.out.print("Move (WASD): ");
            String line = sc.next();
            if (line.isEmpty()) continue;
            char input = line.toLowerCase().charAt(0);

            boolean descended = handleInput(input);
            if (descended) continue;

            AI.moveEnemies();

            if (player.hp <= 0) {
                Renderer.render();
                Renderer.showTombstone();
                Logger.saveLog("GAME OVER");
                break;
            }
        }
        sc.close();
    }

    static boolean handleInput(char input) {
        int newX = player.getX();
        int newY = player.getY();

        switch (input) {
            case 'w': newY--; break;
            case 's': newY++; break;
            case 'a': newX--; break;
            case 'd': newX++; break;
            default:  return false;
        }

        if (!inBounds(newX, newY)) return false;

        if (boss != null && boss.getX() == newX && boss.getY() == newY) {
            boss.hp -= player.attack;
            Logger.addMessage("You strike " + Boss.BOSS_NAME + " for " + player.attack
                    + " dmg!  [" + Math.max(boss.hp, 0) + "/" + boss.maxHp + "]");

            if (boss.hp <= 0) {
                boss = null;
                Logger.addMessage(Boss.BOSS_NAME + " has been SLAIN!");
                boolean leveledUp = player.gainXp(80 + floor * 20);
                if (leveledUp)
                    Logger.addMessage("*** LEVEL UP! Now Lv." + player.level
                            + "  ATK:" + player.attack + "  MaxHP:" + player.maxHp + " ***");
                Logger.addMessage("The stairs to the final floor appear...");
            }
            return false;
        }

        Enemy target = getEnemyAt(newX, newY);
        if (target != null) {
            target.hp -= player.attack;
            Logger.addMessage("You hit " + target.name + " for " + player.attack
                    + " dmg  [" + Math.max(target.hp, 0) + "/" + target.maxHp + "]");

            if (target.hp <= 0) {
                enemies.remove(target);
                Logger.addMessage(target.name + " defeated!");
                boolean leveledUp = player.gainXp(target.xpReward);
                Logger.addMessage("+" + target.xpReward + " XP  ["
                        + player.xp + "/" + player.xpToNext + "]");
                if (leveledUp)
                    Logger.addMessage("*** LEVEL UP! Now Lv." + player.level
                            + "  ATK:" + player.attack + "  MaxHP:" + player.maxHp + " ***");
            }
            return false;
        }

        char tile = map[newY][newX];

        if (tile == Room.CHEST) {
            map[newY][newX] = Room.FLOOR;
            player.setPosition(newX, newY);
            String reward = player.applyChestReward();
            Logger.addMessage("Chest opened!  " + reward);
            return false;
        }

        if (tile == Room.STAIRS) {
            if (floor == BOSS_FLOOR && boss != null) {
                Logger.addMessage(Boss.BOSS_NAME + " blocks your path! Defeat the boss first.");
                return false;
            }
            floor++;
            Logger.addMessage("You descend to floor " + floor + "...");
            Spawner.loadFloor();
            return true;
        }

        if (tile == Room.KEY) {
            Renderer.render();
            System.out.println("\n+======================================+");
            System.out.println("|   YOU FOUND THE LEGENDARY KEY!       |");
            System.out.println("|   YOU WIN!  " + playerName + "  Floors: " + floor
                    + "  Lv." + player.level + "|");
            System.out.println("+======================================+");
            Logger.saveLog("VICTORY");
            System.exit(0);
        }

        if (tile == Room.FLOOR) {
            player.setPosition(newX, newY);
        }

        return false;
    }

    static boolean inBounds(int x, int y) {
        return y >= 0 && y < HEIGHT && x >= 0 && x < WIDTH;
    }

    static Enemy getEnemyAt(int x, int y) {
        for (Enemy e : enemies)
            if (e.getX() == x && e.getY() == y) return e;
        return null;
    }
}