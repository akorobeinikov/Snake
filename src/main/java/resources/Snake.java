package resources;
import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private ArrayList<Point> body = new ArrayList<Point>();
    private int size;
    private int direction;


    private boolean checkOfDie() {
        Point head = body.get(size - 1);

        if (head.x < 0 || head.x >= 20 || head.y < 0 || head.y >= 20) {
            return true;
        }
        return false;
    }

    public Snake(int x0, int y0) {
        direction = 1;
        size = 1;
        body.add(new Point(x0, y0));
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
        this.direction = direction;
    }

    public SnakeChanges move() {
        Point head = body.get(size - 1);
        Point tail = body.get(0);
        body.remove(0);
        switch(direction) {
            case 0:
                body.add(new Point(head.x, head.y - 1));
                break;
            case 1:
                body.add(new Point(head.x + 1, head.y));
                break;
            case 2:
                body.add(new Point(head.x , head.y + 1));
                break;
            case 3:
                body.add(new Point(head.x - 1 , head.y));
                break;
        }
        return new SnakeChanges(checkOfDie(), tail, body.get(size - 1));
    }
}
