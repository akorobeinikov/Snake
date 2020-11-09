package resources;
import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private ArrayList<Point> body = new ArrayList<Point>();
    private int size;
    private int direction;

    private void checkOfDie() {

    }

    public Snake(int x0, int y0) {
        direction = 0;
        size = 1;
        body.add(new Point(x0, y0));
    }

    public void changeDirection(int direction) {
        this.direction = direction;
    }

    public void move() {
        Point head = body.get(size - 1);
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
        checkOfDie();
    }
}
