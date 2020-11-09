package resources;
import javax.swing.*;
import java.awt.*;

public class Cell {

    static ImageIcon[] icons;  // some kind of Array
    static {
        icons = new ImageIcon[3];
        icons[0] = new ImageIcon(new ImageIcon("green.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        icons[1] = new ImageIcon(new ImageIcon("apple2.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        icons[2] = new ImageIcon(new ImageIcon("snake.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
    }
    public int x, y;
    public CellState state;

    public Cell (int _x, int _y, CellState _state) {
        x = _x;
        y = _y;

        state = _state;
    }

    public boolean isEmpty() {
        if (state == CellState.empty) {
            return true;
        }
        return false;
    }

    public ImageIcon getIcon() {
        return icons[state.ordinal()];
    }
}
