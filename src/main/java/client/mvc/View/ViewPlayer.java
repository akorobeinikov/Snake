package client.mvc.View;

import client.mvc.Model.BModelClient;
import client.mvc.Model.ModelClient;
import resources.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ViewPlayer extends JPanel implements IObserver {
    ModelClient m;
    private JLabel[][] viewField;
    int height = 20;
    int width = 20;

    public ViewPlayer() {
        initComponents();
        m = BModelClient.model();
        m.addObserver(this);
        refresh();
    }

    private void initComponents() {
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                int code = event.getKeyCode();

                if (code == KeyEvent.VK_W) {
                    m.setDirection(3);
                }

                if (code == KeyEvent.VK_A) {
                    m.setDirection(0);
                }

                if (code == KeyEvent.VK_S) {
                    m.setDirection(1);
                }

                if (code == KeyEvent.VK_D) {
                    m.setDirection(2);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        int cell_height = 15;
        int cell_width = 15;
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

    private void resetComponents() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                ImageIcon icon = new ImageIcon(new ImageIcon("green.png").getImage().getScaledInstance(21, 21, Image.SCALE_DEFAULT));
                viewField[i][j].setIcon(icon);
            }
        }
    }

    @Override
    public void refresh() {
        this.requestFocus();
        Cell point = m.getPoint();
        if (point == null || point.x < 0)
            return;
        if (point.x == 100) {
            resetComponents();
            return;
        }
        viewField[point.x][point.y].setIcon(new ImageIcon(point.getIcon().getImage().getScaledInstance(-1, -1, Image.SCALE_DEFAULT)));
    }

}
