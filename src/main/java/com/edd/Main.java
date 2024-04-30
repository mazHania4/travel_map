package com.edd;

import com.edd.view.WindowPnl;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        JFrame window = new JFrame();
        window.setBounds(200,200,1000,800);
        window.setResizable(false);
        window.setTitle("WEB XML");
        window.add(new WindowPnl());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        System.out.println("Bye!");
    }
}