package client.mvc.View;

import client.mvc.Model.BModelClient;
import client.mvc.Model.ModelClient;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;


public class ViewClient extends javax.swing.JPanel implements IObserver{

    ModelClient m;

    /**
     * Creates new form View
     */
    public ViewClient() {
        initComponents();
        m = BModelClient.model();
        m.addEvelt(this);
        refresh();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        System.out.println("Hello");
        int height = 20;
        int width = 20;
        viewField = new javax.swing.JLabel[height][width];
        setLayout(new java.awt.GridLayout(height, width));

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                viewField[i][j] = new javax.swing.JLabel();
                ImageIcon icon = new ImageIcon(new ImageIcon("green.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
                viewField[i][j].setIcon(icon);

                add(viewField[i][j]);
            }
        }
        System.out.println("Create field");
    }// </editor-fold>//GEN-END:initComponents

//    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//        m.setText(jTextField1.getText());
//    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel[][] viewField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refresh() {
        int[][] field = m.getField();
        if (field == null)
            return;
        for(int i = 0; i < field.length; ++i) {
            for(int j = 0; j < field[0].length; ++j) {
                viewField[i][j].setText(String.valueOf(field[i][j]));
            }
        }
    }
}
