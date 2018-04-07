/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mibuscaminas;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author profe
 */
public class MiBuscaMinas extends JFrame {

    private final int FRAME_HEIGHT = 600;

    public MiBuscaMinas() {
        initUI();

    }

    private void initUI() {
        Tablero t = new Tablero();
        add(t);
        setTitle("Buscaminas");
        setSize(t.DIM * t.mshi.getWidth(null) + 1, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                MiBuscaMinas ex = new MiBuscaMinas();
                ex.setVisible(true);
            }
        });
    }
}
