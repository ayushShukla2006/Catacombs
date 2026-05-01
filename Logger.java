package game;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

public class Logger {

    static LinkedList<String> messages = new LinkedList<>();
    static final int MAX_MESSAGES = 6;

    static ArrayList<String> fullLog = new ArrayList<>();

    static void addMessage(String msg) {
        messages.addFirst(msg);
        if (messages.size() > MAX_MESSAGES) messages.removeLast();
        fullLog.add(msg);
    }

    static void saveLog(String result) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = Game.playerName + "_" + timestamp + ".txt";

        try (FileWriter fw = new FileWriter(filename)) {
            fw.write("=== CATACOMBS - RUN LOG ===\n");
            fw.write("Player : " + Game.playerName + "\n");
            fw.write("Result : " + result + "\n");
            fw.write("Floor  : " + Game.floor + "\n");
            fw.write("Level  : " + Game.player.level + "\n");
            fw.write("Attack : " + Game.player.attack + "\n");
            fw.write("Date   : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            fw.write("===========================\n\n");

            for (int i = fullLog.size() - 1; i >= 0; i--) {
                fw.write(fullLog.get(i) + "\n");
            }

            System.out.println("\n  Run log saved to: " + filename);
        } catch (IOException e) {
            System.out.println("\n  Could not save log: " + e.getMessage());
        }
    }
}