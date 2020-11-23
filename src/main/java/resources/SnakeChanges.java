package resources;

import java.awt.*;

public class SnakeChanges {
    public boolean status;
    public Point tail;
    public Point head;

    public SnakeChanges(boolean _status, Point _tail, Point _head) {
        status = _status;
        tail = _tail;
        head = _head;
    }
}
