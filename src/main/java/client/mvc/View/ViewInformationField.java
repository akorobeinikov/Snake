package client.mvc.View;

import client.mvc.Model.BModelClient;
import client.mvc.Model.ModelClient;
import resources.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ViewInformationField extends JLabel implements IObserver {
    ModelClient m;

    public ViewInformationField() {
        initComponents();
        m = BModelClient.model();
        m.addObserver(this);
    }

    private void initComponents() {
        setSize(100, 200);
        setText("<html>Information about players: <br>");
    }

    @Override
    public void refresh() {
        Cell point = m.getPoint();
        if (point == null || point.x != -2)
            return;
        Integer player = point.y;
        if (player == 1) {
            setText(getText() + "You are 1 player <br>" + "Waiting for second player to connect <br>");
        } else if (player == 2){
            setText(getText() + "You are 2 player <br>");
        }
        else {
            setText(getText() + " All players are ready !!! <br> The game starts in 3 seconds <br>");
        }
    }

}