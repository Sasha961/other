import javax.swing.*;

public class GUI {
    public static void main(String[] args) {

        JFrame frame = new JFrame("File and Encrypter");
        frame.setSize(600, 400); // размеры
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // закрытие при нажатии крестика
        frame.setLocationRelativeTo(null);
        GUIform guIform = new GUIform();
        frame.add(guIform.getPanel1()); // добавление панели
        frame.setVisible(true);
    }
}
