package client.mvc.View;

import client.mvc.Model.BModelClient;
import client.mvc.Model.ModelClient;
import resources.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ViewResultField extends JLabel implements IObserver {
    ModelClient m;

    public ViewResultField() {
        initComponents();
        m = BModelClient.model();
        m.addObserver(this);
    }

    private void initComponents() {
        setSize(100, 200);
        setText("Result of game: ");
    }

    @Override
    public void refresh() {
        Cell point = m.getPoint();
        if (point == null || point.x != -1)
            return;
        Integer winner = point.y + 1;
        setText("Result of game: " + winner.toString() + " player win");
    }

}