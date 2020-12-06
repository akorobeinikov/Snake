package client;

import client.mvc.Model.BModelClient;
import client.mvc.Model.ModelClient;
import client.mvc.View.ViewPlayer;

import javax.swing.*;
import java.awt.event.*;

public class Player extends JFrame {
    ModelClient m;
    private JPanel contentPane;
    private ViewPlayer field;
    private JButton connectButton;
    private JPanel field_label;

    public Player() {
        m = BModelClient.model();
        setContentPane(contentPane);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents();

    }

    void initComponents() {

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                m.init();
            }
        });
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Player player = new Player();
                player.setVisible(true);
                player.setSize(800, 600);
            }
        });
    }
}
