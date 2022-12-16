package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

public class MainForm2 {
    private JPanel MainPanel;
    private JButton countButton;
    private JPanel drawPanel;

    public MainForm2(){ // чтобы кнопку сделать активной

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
                Graphics2D graphics2D = (Graphics2D) drawPanel.getGraphics();

                graphics2D.setColor(Color.RED);
                graphics2D.fillRect(50, 30, 40 , 50);
            }
        });

    }

    public JPanel getMainPanel(){ // метод возвращения панели
        return  MainPanel;
    }
}
