package main2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

public class MainForm {
    private JPanel MainPanel;
    private JTextArea textArea;
    private JButton countButton;
    private JButton clearButton;
    private JPanel drowPanel;

    public MainForm(){ // чтобы кнопку сделать активной
        clearButton.addActionListener(new Action() {
            @Override
            public Object getValue(String key) {
                return null;
            }

            @Override
            public void putValue(String key, Object value) {

            }

            @Override
            public void setEnabled(boolean b) {

            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void actionPerformed(ActionEvent e) { // метод нажатия на кнопку

                textArea.setText(""); // очистим текст
            }
        });
        countButton.addActionListener(new Action() {
            @Override
            public Object getValue(String key) {
                return null;
            }

            @Override
            public void putValue(String key, Object value) {

            }

            @Override
            public void setEnabled(boolean b) {

            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void actionPerformed(ActionEvent e) {

                String text = textArea.getText();
                int length = text.length();
                JOptionPane.showMessageDialog(
                        MainPanel,
                        length + " символов",
                        "Длина тексата ",
                        JOptionPane.PLAIN_MESSAGE
                );// создание всплывающего окна(1 праметр - родительский класс
                // 2 сообщение
                // 3 - заговок
                // 4 - тип окна)
            }
        });

        textArea.addMouseListener(new MouseListener() { // для клика мыши
            @Override
            public void mouseClicked(MouseEvent e) { // действие при клике мышки

                textArea.setBackground(Color.YELLOW);

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        textArea.addKeyListener(new KeyListener() { // для клика клавиатуры
            @Override
            public void keyTyped(KeyEvent e) {// при отпускние клавишы

                if (e.getKeyChar() == '6'){
                    textArea.setText(textArea.getText() + "лошади");
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public JPanel getMainPanel(){ // метод возвращения панели
        return  MainPanel;
    }
}
