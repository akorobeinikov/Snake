package resources;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Cell {

    static HashMap<String, ImageIcon> icons;
    static {
        icons = new HashMap<>();
        icons.put("empty", new ImageIcon(new ImageIcon("green.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        icons.put("eat", new ImageIcon(new ImageIcon("apple2.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        icons.put("snake", new ImageIcon(new ImageIcon("snake.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
    }
    public int x, y;
    public String state;

    public Cell (int _x, int _y, String _state) {
        x = _x;
        y = _y;

        state = _state;
    }

    public boolean isEmpty() {
        if (state == "empty") {
            return true;
        }
        return false;
    }

    public ImageIcon getIcon() {
        return icons.get(state);
    }
}