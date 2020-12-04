package resources;

import java.awt.*;

public class SnakeChanges {
    public boolean status;
    public Point head;
    public boolean is_grow;
    public boolean moving_to_tail;

    public SnakeChanges(boolean _status, Point _head, boolean _is_grow, boolean _moving_to_tail) {
        status = _status;
        head = _head;
        is_grow = _is_grow;
        moving_to_tail = _moving_to_tail;
    }
}
