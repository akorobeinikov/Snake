package resources;
import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private ArrayList<Point> body = new ArrayList<Point>();
    private int size;
    private int direction;
    private int old_direction;  // to check that new direction isn't opposite to the old one

    public boolean checkOfDie() {
        Point head = body.get(size - 1);

        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            return true;
        }
        return false;
    }

    public Snake(int x0, int y0) {
        direction = 1;
        old_direction = direction;
        size = 3;
        for(int i = 0; i < size; i++) {
            body.add(new Point(x0+i, y0));
        }
    }

    public ArrayList<Cell> translateSnakeToVectorOfCells() {
        ArrayList<Cell> result = new ArrayList<Cell>();
        for (int i = 0; i < size; ++i) {
            int x = body.get(i).x;
            int y = body.get(i).y;
            result.add(new Cell(x, y, CellState.snake));
        }
        return result;
    }

    public void changeDirection(int direction) {
        if(Math.abs(direction-old_direction) != 2)
            this.direction = direction;
    }

    public void changeOldDirection() {
        old_direction = direction;
    }

    public Point moveHead() {
        changeOldDirection();
        Point head = body.get(size - 1);
        switch(direction) {
            case 0:
                int y = (head.y == 0) ? 19 : head.y - 1;
                body.add(new Point(head.x, y));
                break;
            case 1:
                body.add(new Point((head.x + 1) % 20, head.y));
                break;
            case 2:
                body.add(new Point(head.x, (head.y + 1) % 20));
                break;
            case 3:
                int x = (head.x == 0) ? 19 : head.x - 1;
                body.add(new Point(x, head.y));
                break;
        }
        return body.get(size);
    }

    public Point getTail() {
        return body.get(0);
    }

    public Point moveTail() {
        return body.remove(0);
    }

    public void increaseSnake() {
        size++;
    }
}
