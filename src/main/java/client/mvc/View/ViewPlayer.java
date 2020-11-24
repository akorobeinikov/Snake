package client.mvc.View;

import client.mvc.Model.BModelClient;
import client.mvc.Model.ModelClient;
import resources.Cell;

import javax.swing.*;
import java.awt.*;

public class ViewPlayer extends JPanel implements IObserver {
    ModelClient m;
    private JLabel[][] viewField;

    public ViewPlayer() {
        initComponents();
        m = BModelClient.model();
        m.addObserver(this);
        refresh();
    }

    private void initComponents() {
        int cell_height = 15;
        int cell_width = 15;
        int height = 20;
        int width = 20;
        viewField = new JLabel[height][width];
        setLayout(new GridLayout(height, width, 1, 1));

        setSize(width * cell_width, height * cell_height);
        setMaximumSize(new Dimension(width * cell_width, height * cell_height));
        setMinimumSize(new Dimension(width * cell_width, height * cell_height));

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                viewField[i][j] = new JLabel();
                ImageIcon icon = new ImageIcon(new ImageIcon("green.png").getImage().getScaledInstance(21, 21, Image.SCALE_DEFAULT));
                viewField[i][j].setIcon(icon);

                add(viewField[i][j]);
            }
        }
        System.out.println("Create field");
    }

    @Override
    public void refresh() {
        Cell point = m.getPoint();
        if (point == null)
            return;
//        System.out.printf("new point = %s", point.state);
        viewField[point.x][point.y].setIcon(new ImageIcon(point.getIcon().getImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT)));
    }

}
