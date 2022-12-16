package main;

import main2.MainForm;

import javax.swing.*;

public class Main2 {
    public static void main(String[] args) {
        JFrame frame = new JFrame(); // окно приложения
        frame.setSize(600, 400); // размеры окна

        frame.add(new MainForm2().getMainPanel());

        // так делать не правильно
//        frame.setLayout(new FlowLayout());// создание кнопки
//        frame.add(new JButton("Click")); // кнопка

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // закрывает приложение при нажатии крестика
        frame.setLocationRelativeTo(null);// чтобы отображалось в центре
        frame.setVisible(true);// делает окно видимым

    }
}
