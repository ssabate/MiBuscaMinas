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


    public MiBuscaMinas() {
        initUI();

    }

    private void initUI() {
        Tablero t = new Tablero();
        add(t);
        setTitle("Buscaminas");
        if(t.test) setSize(t.DIM * t.mshi.getWidth(null) + 1, ((t.DIM * 2)+6) * t.mshi.getHeight(null));
        else  setSize(t.DIM * t.mshi.getWidth(null) + 1, (t.DIM+6) * t.mshi.getHeight(null));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
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
