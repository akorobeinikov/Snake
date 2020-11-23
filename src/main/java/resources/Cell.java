package resources;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.swing.*;
import java.awt.*;

@JsonAutoDetect
public class Cell {

    @JsonIgnore
    static ImageIcon[] icons;  // some kind of Array
    static {
        icons = new ImageIcon[3];
        icons[0] = new ImageIcon(new ImageIcon("green.png").getImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT));
        icons[1] = new ImageIcon(new ImageIcon("apple.png").getImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT));
        icons[2] = new ImageIcon(new ImageIcon("snake_body.png").getImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT));
    }
    public int x, y;
    public CellState state;

    public Cell() {

    }
    public Cell (int _x, int _y, CellState _state) {
        x = _x;
        y = _y;

        state = _state;
    }
    @JsonIgnore
    public boolean isEmpty() {
        if (state == CellState.empty) {
            return true;
        }
        return false;
    }
    @JsonIgnore
    public ImageIcon getIcon() {
        return icons[state.ordinal()];
    }
}
