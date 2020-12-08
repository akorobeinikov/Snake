package resources;

import java.awt.*;

public class SnakeChanges {
    public boolean status;
    public Point head;
    public boolean is_grow;
    public boolean moving_to_tail;
    public boolean snake_crash;

    public SnakeChanges(Point _head, boolean _is_grow, boolean _moving_to_tail, boolean _snake_crash) {
        head = _head;
        is_grow = _is_grow;
        moving_to_tail = _moving_to_tail;
        snake_crash = _snake_crash;
        setStatus();
    }

    private void setStatus() {
        if (moving_to_tail || snake_crash) {
            status = true;
        }
    }
}
