package resources;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Cell {

    HashMap<String, ImageIcon> icons;
    int x, y;
    String current;

    public Cell (int _x, int _y) {
        x = _x;
        y = _y;
        icons = new HashMap<>();
        icons.put("empty", new ImageIcon(new ImageIcon("green.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        icons.put("eat", new ImageIcon(new ImageIcon("apple.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        icons.put("snake", new ImageIcon(new ImageIcon("snake.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
        current = "empty";
    }
    public ImageIcon getCurrent() {
        return icons.get(current);
    }

    public void setState(String state) {
        current = state;
    }

    public boolean isEmpty() {
        if (current == "empty")
            return true;
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}