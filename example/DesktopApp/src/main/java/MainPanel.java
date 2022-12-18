import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class MainPanel {

    public JPanel mainPanel;

    private JPanel panel1;
    private JPanel panel2;
    private JTextField surname;
    private JTextField name;
    private JTextField patronymic;
    private JButton collapse;
    private JButton expand;
    private JTextField fullName;

    public MainPanel() {

        collapse.addActionListener(new Action() {

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

                if (checkFullName(surname.getText(), name.getText())) {
                    fullName.setText(surname.getText() + " " + name.getText() + " " + patronymic.getText());

                    panel1.setVisible(false);
                    panel2.setVisible(true);
                }
            }
        });

        expand.addActionListener(new Action() {

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

                String[] fullNameToString = fullName.getText().split(" ");

                if (fullNameToString.length == 0) {

                    checkFullName("", "");

                } else if (fullNameToString.length == 1) {

                    checkFullName(fullNameToString[0], "");

                } else {

                    surname.setText(fullNameToString[0]);
                    name.setText(fullNameToString[1]);

                    if (fullNameToString.length > 2) {
                        patronymic.setText(fullNameToString[2]);
                    } else {
                        patronymic.setText("");
                    }

                    panel2.setVisible(false);
                    panel1.setVisible(true);
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public boolean checkFullName(String surnameToString, String nameToString) {

        if (isBlank(surnameToString) && isBlank(nameToString)) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Введите Фамилию и Имя",
                    "Ошибка!",
                    JOptionPane.PLAIN_MESSAGE
            );
            return false;

        } else {
            if (isBlank(surnameToString)) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Введите Фамилию",
                        "Ошибка!",
                        JOptionPane.PLAIN_MESSAGE
                );
                return false;
            }

            if (isBlank(nameToString)) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Введите Имя",
                        "Ошибка!",
                        JOptionPane.PLAIN_MESSAGE
                );
                return false;
            }
        }

        return true;
    }

    private boolean isBlank(String text) {

        if (text.isBlank()) {
            return true;
        }
        return false;
    }
}
