package comedor;

import javax.swing.SwingUtilities;

public class Comedor {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                new GUI.MainWindow().setVisible(true);;
            }
            
        });
    }
    
}
