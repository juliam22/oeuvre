package byow.Core;

import java.util.Random;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class MapGenerator {

    private static final int WIDTH = 70;
    private static final int HEIGHT = 45;
//    private static final int AREA = WIDTH * HEIGHT;
//    private int SPACE_USED = 0;


    public static TETile[][] clearWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }


   //Builds room on point (x,y) of width WIDTH and height HEIGHT on world in a NorthEast direction
    public static void makeRoomNE(int x, int y, int width, int height, TETile[][] world) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i + x][j + y] = Tileset.FLOOR;
            }
        }
    }

    //Builds room on point (x,y) of width WIDTH and height HEIGHT on world in a SouthEast direction
    public static void makeRoomSE(int x, int y, int width, int height, TETile[][] world) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i + x][-j + y] = Tileset.FLOOR;
            }
        }
    }


    // Builds hallway on point (x,y) of length LENGTH in either a horizontal
    // or vertical direction depending on the vertical boolean. Width is always 3 for
    // hallways. BUilds in a NorthEast direction.
    public static void makeHallwayNE(int x, int y, int length, TETile[][] world, boolean vertical) {
        if (vertical) {
            makeRoomNE(x, y, 3, length, world);
        } else {
            makeRoomNE(x, y, length, 3, world);
        }
    }

    // Builds hallway on point (x,y) of length LENGTH in either a horizontal
    // or vertical direction depending on the vertical boolean. Width is always 3 for
    // hallways. Builds in a SouthEast direction.
    public static void makeHallwaySE(int x, int y, int length, TETile[][] world, boolean vertical) {
        if (vertical) {
            makeRoomSE(x, y, 3, length, world);
        } else {
            makeRoomSE(x, y, length, 3, world);
        }
    }


    // Repeatedly builds rooms and hallways in a NorthEast direction until the
    // space taken up has reached the desired amount. Builds walls and hallways
    // with a 1/3 chance of rooms vs 2/3 chance of hallways.
    public static void generateFloorsNE(Random r, TETile[][] world, int startX, int startY) {
        int X = startX;
        int Y = startY;
        int i = 50;
        boolean vertical = false;
        boolean roomPrev = false;
        while (i > 0) {
            boolean roomOrHall = (r.nextInt() % 2 == 0);
            if (!roomPrev && roomOrHall) {
                int rand1 = r.nextInt(6) + 5;
                int rand2 = r.nextInt(6) + 5;
                if (!isValid(X + rand1, Y + rand2)) {
                    return;
                }
                makeRoomNE(X, Y, rand1, rand2, world);
                X = X + r.nextInt(rand1 - 3) + 1;
                Y = Y + r.nextInt(rand2 - 3) + 1;
                if (!isValid(X, Y)) {
                    return;
                }
                i--;
                roomPrev = true;
            } else {
                int randLength = r.nextInt(9) + 5;
                if (!isValid(X + randLength, Y + randLength)) {
                    return;
                }
                makeHallwayNE(X, Y, randLength, world, vertical);
                vertical = !vertical;
                roomPrev = false;
                if (!vertical) {
                    X = X + 1;
                    Y = Y + r.nextInt(randLength - 3) + 1;
                    if (!isValid(X, Y)) {
                        return;
                    }
                } else {
                    X = X + r.nextInt(randLength - 3) + 1;
                    Y = Y + 1;
                    if (!isValid(X, Y)) {
                        return;
                    }
                }
                i--;
            }
        }
    }

    // generates floors and hallways in a SouthEast direction
    public static void generateFloorsSE(Random r, TETile[][] world, int startX, int startY) {
        int X = startX;
        int Y = startY;
        int i = 50;
        boolean vertical = false;
        boolean roomPrev = false;
        while (i > 0) {
            boolean roomOrHall = (r.nextInt() % 3 == 0);
            if (!roomPrev && roomOrHall) {
                int rand1 = r.nextInt(6) + 5;
                int rand2 = r.nextInt(6) + 5;
                if (!isValid(X + rand1, Y - rand2)) {
                    return;
                }
                makeRoomSE(X, Y, rand1, rand2, world);
                X = X + r.nextInt(rand1 - 3) + 1;
                Y = Y - r.nextInt(rand2 - 3) - 1;
                if (!isValid(X, Y)) {
                    return;
                }
                roomPrev = true;
                i--;
            } else {
                int randLength = r.nextInt(9) + 5;
                if (!isValid(X + randLength, Y - randLength)) {
                    return;
                }
                makeHallwaySE(X, Y, randLength, world, vertical);
                vertical = !vertical;
                roomPrev = false;
                if (!vertical) {
                    X = X + 1;
                    Y = Y - r.nextInt(randLength - 3) - 1;
                    if (!isValid(X, Y)) {
                        return;
                    }
                } else {
                    X = X + r.nextInt(randLength - 3) + 1;
                    Y = Y - 1;
                    if (!isValid(X, Y)) {
                        return;
                    }
                }
                i--;
            }
        }
    }


    //Makes sure the points cannot build within a 1 pixel bound of the world
    public static boolean isValid(int x, int y) {
        if (x > WIDTH - 1 || x < 1 || y > HEIGHT - 1 || y < 1) {
            return false;
        }
        return true;
    }

    // Constructs walls around map
    public static void buildWalls(TETile[][] world) {
        for (int r = 0; r < WIDTH; r++) {
            for (int c = 0; c < HEIGHT; c++) {
                if (emptyAdjacent(world, r, c) && world[r][c] == Tileset.FLOOR) {
                    world[r][c] = Tileset.WALL;
                }
            }
        }
    }

    // Returns true if empty space in adjacent point to row, col of world
    public static boolean emptyAdjacent(TETile[][] world, int row, int col) {
        for (int r = 0; r < WIDTH; r++) {
            for (int c = 0; c < HEIGHT; c++) {
                if (r == row && c == col) {
                    continue;
                }
                if (c < col - 1 || c > col + 1) {
                    continue;
                }
                if (r < row - 1 || r > row + 1) {
                    continue;
                }
                if (world[r][c] == Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }


    // Uses generateFloors to create a random map using the seed parameter
    public static void createMap(int seed, TETile[][] world) {
        Random rand = new Random(seed);
        Random rand2 = new Random(seed + 1);
        Random rand3 = new Random(seed + 2);

        generateFloorsNE(rand, world, 5, 2);
        generateFloorsSE(rand2, world, 5, 43);
        generateFloorsNE(rand3, world, 25, 2);
        buildWalls(world);
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize clear tiles
        TETile[][] world = clearWorld();

        //Testing Zone:

        //ADD starting room to beginning of each floor generation
        // draws the world to the screen
        ter.renderFrame(world);
    }
}

