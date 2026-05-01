package game;

import java.util.Scanner;

public class Renderer {

    static void showTitleScreen(Scanner sc) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println();
        System.out.println("   ######     ###    ########    ###     ######    #######  ##     ## ########   ######  ");
        System.out.println("  ##    ##   ## ##      ##      ## ##   ##    ##  ##     ## ###   ### ##     ## ##    ## ");
        System.out.println("  ##        ##   ##     ##     ##   ##  ##        ##     ## #### #### ##     ## ##       ");
        System.out.println("  ##       ##     ##    ##    ##     ## ##        ##     ## ## ### ## ########   ######  ");
        System.out.println("  ##       #########    ##    ######### ##        ##     ## ##     ## ##     ##       ## ");
        System.out.println("  ##    ## ##     ##    ##    ##     ## ##    ##  ##     ## ##     ## ##     ## ##    ## ");
        System.out.println("   ######  ##     ##    ##    ##     ##  ######    #######  ##     ## ########   ######  ");
        System.out.println();
        System.out.println("                        Descend. Fight. Survive.");
        System.out.println();
        System.out.print("  Enter your name: ");
        Game.playerName = sc.next().trim();
        if (Game.playerName.isEmpty()) Game.playerName = "Unknown";
        System.out.println();
        System.out.println("  Welcome, " + Game.playerName + ". The catacombs await...");
        System.out.println();
        System.out.print("  Press ENTER to begin.");
        try { System.in.read(); System.in.read(); } catch (Exception ignored) {}
    }

    static void showTombstone() {
        System.out.println();
        System.out.println("          .-------.");
        System.out.println("         /  R.I.P  \\");
        System.out.println("        |           |");

        String name = Game.playerName.length() > 9 ? Game.playerName.substring(0, 9) : Game.playerName;
        int pad = (9 - name.length()) / 2;
        String left  = " ".repeat(pad);
        String right = " ".repeat(9 - name.length() - pad);
        System.out.println("        |  " + left + name + right + "  |");

        System.out.println("        |           |");
        System.out.println("        | Floor " + String.format("%-4s", Game.floor) + "  |");
        System.out.println("        | Level " + String.format("%-4s", Game.player.level) + "  |");
        System.out.println("        |           |");
        System.out.println("        |___________|");
        System.out.println("       /|           |\\");
        System.out.println("      / |           | \\");
        System.out.println();
        System.out.println("        " + Game.playerName + " was slain in the Catacombs.");
        System.out.println();
    }

    static void render() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(" " + Game.playerName + "  |  Floor: " + Game.floor + "/" + Game.KEY_FLOOR
                + "   Lv." + Game.player.level
                + "  XP: " + Game.player.xp + "/" + Game.player.xpToNext
                + "  HP: " + Game.player.hp + "/" + Game.player.maxHp
                + "  ATK: " + Game.player.attack);

        if (Game.boss != null) {
            int barLen = 30;
            int filled = (int)((double) Game.boss.hp / Game.boss.maxHp * barLen);
            filled = Math.max(0, Math.min(barLen, filled));
            StringBuilder bar = new StringBuilder("[");
            for (int i = 0; i < barLen; i++) bar.append(i < filled ? '=' : ' ');
            bar.append("]");
            System.out.println(" BOSS " + Boss.BOSS_NAME
                    + "  " + bar + " " + Game.boss.hp + "/" + Game.boss.maxHp);
        }

        System.out.println(" Legend:  @ You   E Enemy   B Boss   > Stairs   K Key   C Chest");
        System.out.println();

        for (int i = 0; i < Game.HEIGHT; i++) {
            for (int j = 0; j < Game.WIDTH; j++) {

                if (i == Game.player.getY() && j == Game.player.getX()) {
                    System.out.print('@');
                    continue;
                }

                if (Game.boss != null && Game.boss.getY() == i && Game.boss.getX() == j) {
                    System.out.print('B');
                    continue;
                }

                boolean printed = false;
                for (Enemy e : Game.enemies) {
                    if (e.getY() == i && e.getX() == j) {
                        System.out.print('E');
                        printed = true;
                        break;
                    }
                }

                if (!printed) System.out.print(Game.map[i][j]);
            }
            System.out.println();
        }

        System.out.println("\n--- Log ---");
        for (String msg : Logger.messages) System.out.println("  " + msg);
    }
}