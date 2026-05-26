package com.jerme.sis;

import javax.swing.*;

public class MainFrame {
    private JPanel JermeFrame;

    protected void showFrame() {
        JermeFrame.setSize(500, 500);
        JermeFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame().showFrame();
    }

}
