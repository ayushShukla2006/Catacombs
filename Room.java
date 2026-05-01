package game;
import java.util.ArrayList;
import java.util.Random;

public class Room {

    static final int WIDTH = 70;
    static final int HEIGHT = 30;
    static final int MIN_SIZE = 6;

    static char[][] map;
    static Random rand = new Random();

    public static final char WALL   = '#';
    public static final char FLOOR  = ' ';
    public static final char STAIRS = '>';
    public static final char KEY    = 'K';
    public static final char CHEST  = 'C';

    static class Node {
        int x, y, w, h;
        Node left, right;
        int roomX, roomY, roomW, roomH;

        Node(int x, int y, int w, int h) {
            this.x = x; this.y = y; this.w = w; this.h = h;
        }

        boolean isLeaf() { return left == null && right == null; }
    }

    public static char[][] generateMap() { return generateMap(false, false); }

    public static char[][] generateMap(boolean placeStairs, boolean placeKey) {
        map = new char[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++)
                map[i][j] = WALL;

        Node root = new Node(1, 1, WIDTH - 2, HEIGHT - 2);
        split(root);
        createRooms(root);
        connectRooms(root);

        for (int i = 0; i < HEIGHT; i++) { map[i][0] = WALL; map[i][WIDTH-1] = WALL; }
        for (int j = 0; j < WIDTH;  j++) { map[0][j] = WALL; map[HEIGHT-1][j] = WALL; }

        ArrayList<int[]> cells = getFloorCells();

        if (placeStairs && !cells.isEmpty()) {
            int[] c = cells.remove(rand.nextInt(cells.size()));
            map[c[1]][c[0]] = STAIRS;
        }
        if (placeKey && !cells.isEmpty()) {
            int[] c = cells.remove(rand.nextInt(cells.size()));
            map[c[1]][c[0]] = KEY;
        }

        return map;
    }

    public static void placeChests(int count) {
        ArrayList<int[]> cells = getFloorCells();
        for (int i = 0; i < count && !cells.isEmpty(); i++) {
            int[] c = cells.remove(rand.nextInt(cells.size()));
            map[c[1]][c[0]] = CHEST;
        }
    }

    private static ArrayList<int[]> getFloorCells() {
        ArrayList<int[]> list = new ArrayList<>();
        for (int i = 1; i < HEIGHT-1; i++)
            for (int j = 1; j < WIDTH-1; j++)
                if (map[i][j] == FLOOR)
                    list.add(new int[]{j, i});
        return list;
    }

    static void split(Node node) {
        boolean cv = node.w >= MIN_SIZE * 2;
        boolean ch = node.h >= MIN_SIZE * 2;
        if (!cv && !ch) return;

        boolean sv = (cv && ch) ? rand.nextBoolean() : cv;

        if (sv) {
            int max = node.w - MIN_SIZE * 2;
            int s = rand.nextInt(max + 1) + MIN_SIZE;
            node.left  = new Node(node.x, node.y, s, node.h);
            node.right = new Node(node.x + s, node.y, node.w - s, node.h);
        } else {
            int max = node.h - MIN_SIZE * 2;
            int s = rand.nextInt(max + 1) + MIN_SIZE;
            node.left  = new Node(node.x, node.y, node.w, s);
            node.right = new Node(node.x, node.y + s, node.w, node.h - s);
        }
        split(node.left);
        split(node.right);
    }

    static void createRooms(Node node) {
        if (node.isLeaf()) {
            int rw = Math.max(3, rand.nextInt(Math.max(1, node.w - 2)) + 3);
            int rh = Math.max(3, rand.nextInt(Math.max(1, node.h - 2)) + 3);
            int rx = node.x + (node.w - rw > 0 ? rand.nextInt(node.w - rw) : 0);
            int ry = node.y + (node.h - rh > 0 ? rand.nextInt(node.h - rh) : 0);
            node.roomX = rx; node.roomY = ry; node.roomW = rw; node.roomH = rh;
            for (int i = ry; i < ry+rh; i++)
                for (int j = rx; j < rx+rw; j++)
                    if (i>0 && i<HEIGHT-1 && j>0 && j<WIDTH-1)
                        map[i][j] = FLOOR;
        } else {
            createRooms(node.left);
            createRooms(node.right);
        }
    }

    static void connectRooms(Node node) {
        if (node.left != null && node.right != null) {
            int[] p1 = getRoomCenter(node.left);
            int[] p2 = getRoomCenter(node.right);
            carveCorridor(p1[0], p1[1], p2[0], p2[1]);
            connectRooms(node.left);
            connectRooms(node.right);
        }
    }

    static int[] getRoomCenter(Node node) {
        if (node.isLeaf())
            return new int[]{ node.roomX + node.roomW/2, node.roomY + node.roomH/2 };
        return rand.nextBoolean() ? getRoomCenter(node.left) : getRoomCenter(node.right);
    }

    static void carveCorridor(int x1, int y1, int x2, int y2) {
        if (rand.nextBoolean()) {
            for (int x = Math.min(x1,x2); x <= Math.max(x1,x2); x++)
                if (y1>0 && y1<HEIGHT-1 && x>0 && x<WIDTH-1) map[y1][x] = FLOOR;
            for (int y = Math.min(y1,y2); y <= Math.max(y1,y2); y++)
                if (y>0 && y<HEIGHT-1 && x2>0 && x2<WIDTH-1) map[y][x2] = FLOOR;
        } else {
            for (int y = Math.min(y1,y2); y <= Math.max(y1,y2); y++)
                if (y>0 && y<HEIGHT-1 && x1>0 && x1<WIDTH-1) map[y][x1] = FLOOR;
            for (int x = Math.min(x1,x2); x <= Math.max(x1,x2); x++)
                if (y2>0 && y2<HEIGHT-1 && x>0 && x<WIDTH-1) map[y2][x] = FLOOR;
        }
    }
}