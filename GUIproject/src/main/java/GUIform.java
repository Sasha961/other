import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class GUIform {
    private JPanel panel1;
    private JButton button1;
    private JTextField textField1;
    private JButton button2;
    private File selectedFile;

    public GUIform (){
        button1.addActionListener(new Action() { // кнопка
            @Override
            public void actionPerformed(ActionEvent e) { // при нажатии на кнопку
                JFileChooser chooser = new JFileChooser(); // позволяет выбирать файлы
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // какой путь выбирать
                chooser.showOpenDialog(panel1); // относительного чего размещен
                selectedFile = chooser.getSelectedFile();
                textField1.setText(selectedFile.getAbsolutePath()); // путь к файлу
                try {
                    ZipFile zipFile = new ZipFile(selectedFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                button2.setVisible(true);            }

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
        });
    }
    public JPanel getPanel1(){
       return panel1;
   }
}
