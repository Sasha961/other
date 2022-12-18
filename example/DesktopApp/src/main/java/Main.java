import javax.swing.*;

public class Main {
    public static JFrame jFrame = new JFrame("My program");

    public static void main(String[] args) {

        jFrame.setSize(500, 450);
        jFrame.add(new MainPanel().getMainPanel());
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
