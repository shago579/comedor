package GUI;

import DB.DBConnection;
import DB.Persona;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.BufferedImageLuminanceSource;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class WebcamFrame extends JFrame implements Runnable {

    JMenuBar menuBar;
    JMenu archivoMenu;
    JMenu editarMenu;
    Webcam webcam;

    JPanel infoPanel;
    JPanel rightPanel;
    JPanel leftPanel;
    JPanel lastComensalPanel;
    JPanel horarioPanel;
    
    JPanel relojPanel;
    JLabel fechaLabel;
    JLabel relojLabel;
    boolean showDots;

    JLabel desContadorLabel;
    JLabel comContadorLabel;
    JLabel cenContadorLabel;
    JLabel colContadorLabel;

    JLabel comidaLabel;
    JLabel starLabel;
    JLabel finishLabel;
    JLabel logo1Label;
    JLabel logo2Label;
    
    String horaTermina;
    String comidaActual;

    int desContador;
    int comContador;
    int cenContador;
    int colContador;

    JLabel apellidosLabel ;
    //private javax.swing.JLabel jLabel1;
    JPanel jPanel1;
    //JPanel jPanel2;
    JLabel msgLabel;
    JLabel nombreLabel;
    JLabel permisoLabel;
    JLabel rangoLabel;
    JLabel imgLabel;

    public WebcamFrame() {
        super(("Test webcam panel"));
        webcam = Webcam.getDefault();
        webcam.setViewSize(new java.awt.Dimension(320, 240));//[176x144] [320x240] [640x480]
        //webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
        //panel.setFPSDisplayed(true);
        //panel.setDisplayDebugInfo(true);
        //panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        infoPanel = new JPanel();
        rightPanel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout());
        
        /************************Armar Reloj****************************************/
        showDots = true;
        relojPanel = new JPanel(new GridLayout(2, 1));
        
        fechaLabel = new JLabel();
        relojLabel = new JLabel("", SwingConstants.CENTER);
        fechaLabel.setFont(new Font("Sans-Serif", Font.BOLD, 24));
        relojLabel.setFont(new Font("Sans-Serif", Font.BOLD, 22));

        relojPanel.add(fechaLabel);
        relojPanel.add(relojLabel);
        
        armarInfoPanel();

        //rightPanel.setSize(this.getHeight(), this.getWidth() / 3);
        /*rightPanel.setBorder(BorderFactory.createEmptyBorder(
         1, //top
         0,     //left
         1, //bottom
         0));   //right*/
        //rightPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
        rightPanel.add(panel, BorderLayout.NORTH);
        //rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JPanel infoInnerPanel = new JPanel(new BorderLayout());
        infoInnerPanel.add(infoPanel, BorderLayout.NORTH);
        rightPanel.add(infoInnerPanel, BorderLayout.CENTER);
        rightPanel.add(relojPanel, BorderLayout.SOUTH);
        
        

        lastComensalPanel = new JPanel();
        //lastComensalPanel.setBackground(Color.YELLOW);
        //createEmptyBorder(0, 50, 0, 50));
        
        JPanel middlePanel = new JPanel(new GridLayout(2, 1));
        middlePanel.setBackground(Color.red);
        
        JPanel insidePanel = new JPanel();
        //insidePanel.setMinimumSize(new Dimension(650, 250));
        insidePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JPanel logosPanel = new JPanel(new BorderLayout());
        ponerLogos();
        logosPanel.add(logo1Label, BorderLayout.WEST);
        JLabel instructionsLabel = new JLabel("Pasar codigo QR frente a la camara");
        instructionsLabel.setFont(new Font("Sans-Serif", Font.BOLD, 16));
        //Font f = new Font("Sans-Serif", Font.BOLD, 16);
        logosPanel.add(instructionsLabel, BorderLayout.CENTER);
        logosPanel.add(logo2Label, BorderLayout.EAST);
        
        
        apellidosLabel = new JLabel();
        //private javax.swing.JLabel jLabel1;
        jPanel1 = new JPanel(new GridLayout(5, 2, 10, 10));
        //JPanel jPanel2;
        Font f = new Font("Sans-Serif", Font.BOLD, 18);
        JLabel nombreL = new JLabel("Nombre: ");
        msgLabel = new JLabel();
        nombreLabel = new JLabel();
        JLabel rango = new JLabel("Rango: ");
        //msgLabel = new JLabel();
        rangoLabel = new JLabel();
        JLabel permiso = new JLabel("Servicios Autorizados: ");
        permisoLabel = new JLabel();
        
        imgLabel = new JLabel();
        
        nombreL.setFont(f);
        rango.setFont(f);
        permiso.setFont(f);
        msgLabel.setFont(f);
        nombreLabel.setFont(f);
        permisoLabel.setFont(f);
        rangoLabel.setFont(f);
        
        jPanel1.add(nombreL);
        jPanel1.add(nombreLabel);
        jPanel1.add(rango);
        jPanel1.add(rangoLabel);
        jPanel1.add(permiso);
        jPanel1.add(permisoLabel);
        jPanel1.add(new JLabel(" "));
        jPanel1.add(new JLabel(" "));
        jPanel1.add(msgLabel);

        insidePanel.add(jPanel1);
        insidePanel.add(imgLabel);
        
        middlePanel.add(logosPanel);
        middlePanel.add(insidePanel);
        lastComensalPanel.add(middlePanel);
        //lastComensalPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_END);
        //lastComensalPanel.add(new JLabel("huehuehue"));
        armarHorarioPanel();
        
        leftPanel.add(horarioPanel, BorderLayout.PAGE_START);
        leftPanel.add(lastComensalPanel, BorderLayout.CENTER);
        

        this.setLayout(new BorderLayout());
        
        this.add(leftPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.LINE_END);
        
        menuBar = new JMenuBar();

        archivoMenu = new JMenu("Archivo");
        editarMenu = new JMenu("Editar");

        menuBar.add(archivoMenu);

        setJMenuBar(menuBar);

        this.setResizable(true);
        this.setMinimumSize(new java.awt.Dimension(800, 470));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        Thread t = new Thread(this);

        t.start();
    }
    
    private void actualizarReloj(){
        Calendar cal = Calendar.getInstance();
        showDots = !showDots;
        
        fechaLabel.setText(getDateString(cal));
        relojLabel.setText(getRelojString(cal));
    }
    
    private String getRelojString(Calendar cal){
        String format = "";
        int minn = cal.get(Calendar.MINUTE);
        String min = minn < 10 ? "0" + minn: minn+"";
        
        if(showDots)
            format = cal.get(Calendar.HOUR_OF_DAY) +":" + min;
        else {
            
            format = cal.get(Calendar.HOUR_OF_DAY) +" " + min;
        }
        return format;
    }
    
    private String getDateString(Calendar cal){
        //String format = "";
        int m = cal.get(Calendar.MONTH);
        String format = cal.get(Calendar.DAY_OF_MONTH) + " de " + 
                fromInttoMonth(m) + " del " + cal.get(Calendar.YEAR); 
        
        
        return format;
    }
    
    private String fromInttoMonth(int m){
        String mon = "";
        switch(m){
            case 0:
                mon = "Enero";
                break;
            case 1:
                mon = "Febrero";
                break;
            case 2:
                mon = "Marzo";
                break;
            case 3:
                mon = "Abril";
                break;
            case 4:
                mon = "Mayo";
                break;
            case 5:
                mon = "Junio";
                break;
            case 6:
                mon = "Julio";
                break;
            case 7:
                mon = "Agosto";
                break;
            case 8:
                mon = "Septiembre";
                break;
            case 9:
                mon = "Octubre";
                break;
            case 10:
                mon = "Noviembre";
                break;
            case 11:
                mon = "Diciembre";
                break;
        }
        
        return mon;
    }

    private void armarHorarios(String comid[]) {
        if(comid != null){
            horaTermina = comid[3].substring(0, comid[3].length() - 3);
            String horaStart = comid[2].substring(0, comid[2].length() - 3);
            comidaActual = comid[1];
            
            comidaLabel.setText(comidaActual);
            starLabel.setText(horaStart);
            finishLabel.setText(horaTermina);
        } else {
            comidaActual = null;
            comidaLabel.setText("No hay comidas disponibles");
            starLabel.setText("--");
            finishLabel.setText("--");
        }
        
    }

    private void armarHorarioPanel() {
        horarioPanel = new JPanel(new GridLayout(3, 2, 2, 2));
        horarioPanel.setMaximumSize(new Dimension(300, 200));

        DBConnection dbC = new DBConnection();
        String com[] = dbC.queComidaToca();

        comidaLabel = new JLabel();
        starLabel = new JLabel();
        finishLabel = new JLabel();
        
        JLabel seSirve = new JLabel("Se esta sirviendo: ", SwingConstants.RIGHT);
        JLabel empieza = new JLabel("Empieza: ", SwingConstants.RIGHT);
        JLabel termina = new JLabel("Termina: ", SwingConstants.RIGHT);
        
        Font f = new Font("Sans-Serif", Font.BOLD, 25);
        
        comidaLabel.setFont(f);
        starLabel.setFont(f);
        finishLabel.setFont(f);
        seSirve.setFont(f);
        empieza.setFont(f);
        termina.setFont(f);
        
        dbC.closeConnection();

        armarHorarios(com);
        
        horarioPanel.add(seSirve);
        horarioPanel.add(comidaLabel);
        horarioPanel.add(empieza);
        horarioPanel.add(starLabel);
        horarioPanel.add(termina);
        horarioPanel.add(finishLabel);

    }

    private void armarInfoPanel() {

        //infoPanel.setBackground(Color.red);
        desContador = 0;
        comContador = 0;
        cenContador = 0;
        colContador = 0;

        desContadorLabel = new JLabel("Desayunos servidos: " + desContador, SwingConstants.CENTER);
        comContadorLabel = new JLabel("Comidas servidas: " + comContador, SwingConstants.CENTER);
        cenContadorLabel = new JLabel("Cenas servidas: " + cenContador, SwingConstants.CENTER);
        colContadorLabel = new JLabel("Colaciones servidas: " + colContador, SwingConstants.CENTER);

        Font f = new Font("Sans-Serif", Font.BOLD, 18);

        desContadorLabel.setFont(f);
        desContadorLabel.setForeground(Color.BLUE);
        //desContadorLabel.setVerticalAlignment(SwingConstants.CENTER);
        //desContadorLabel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));

        comContadorLabel.setFont(f);
        comContadorLabel.setForeground(Color.BLUE);
        //comContadorLabel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));

        cenContadorLabel.setFont(f);
        cenContadorLabel.setForeground(Color.BLUE);
        //cenContadorLabel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));

        colContadorLabel.setFont(f);
        colContadorLabel.setForeground(Color.BLUE);
        //colContadorLabel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));

        infoPanel.setLayout(new GridLayout(8, 1, 0, 0));

        infoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        infoPanel.add(desContadorLabel);
        infoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        infoPanel.add(comContadorLabel);
        infoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        infoPanel.add(cenContadorLabel);
        infoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        infoPanel.add(colContadorLabel);
    }

    @Override
    public void run() {

        /*Descomentar si deja de funcionar el lector de QR */
        //Webcam webcam = Webcam.getDefault();
        webcam.open();
        while (true) {
            // non-default (e.g. USB) webcam can be used too
            //System.out.println("isFocusOwner");
            //System.out.println( aunEstaCorrectaLaInformacion() );
            actualizarReloj();
            if(!aunEstaCorrectaLaInformacion()){
                DBConnection dbC = new DBConnection();
                armarHorarios(dbC.queComidaToca());
                dbC.closeConnection();
            }
            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    //continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // si no se encuentra un Codigo QR
                    //System.out.println("QR Not Found");
                }
            }

            if (result != null) {
                //Encontro a la persona
                //JOptionPane.showMessageDialog(null, "Luis Santiago Vazquez Mancilla", "Comensal Encontrado", JOptionPane.INFORMATION_MESSAGE);
                //DBConnection dbC = new DBConnection();
                System.out.println(result.getText());
                Persona ps = getPersona(result.getText());
                if (ps != null) {
                    boolean puede = puedeComer(ps);
                    if (puede) {
                        guardarComensal(ps);

                    }
                    displayComensal2(ps, puede);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
        }
    }

    private Persona getPersona(String curp) {
        Persona p;
        DBConnection dbC = new DBConnection();
        p = dbC.getUsuario(curp);
        dbC.closeConnection();
        return p;
    }

    private boolean puedeComer(Persona ps) {
        DBConnection dbC = new DBConnection();
        boolean puede = dbC.puedeComer(ps);
        dbC.closeConnection();
        System.out.println("Puede comer? " + puede);
        return puede;
    }

    private void guardarComensal(Persona ps) {
        //contador++;
        //contadorLabel.setText("Comidas servidas: "+ contador);

        DBConnection dbC = new DBConnection();
        dbC.registrarPersona(ps);
        dbC.closeConnection();
    }

    private void displayComensal2(Persona ps, boolean puedeComer) {
        if (puedeComer) {
            //ComensalDialog cd = new ComensalDialog(ps, puedeComer);
            checarComida();
            displayComensal(ps);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario \"" + ps.getNombre() + " "
                    + ps.getApellidos() + "\" no puede recibir este alimento",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checarComida() {
        DBConnection dbC = new DBConnection();
        String com[] = dbC.queComidaToca();

        switch (com[1]) {
            case "Desayuno":
                desContador++;
                desContadorLabel.setText("Desayunos servidos: " + desContador);
                break;
            case "Comida":
                comContador++;
                comContadorLabel.setText("Comidas servidas: " + comContador);
                break;
            case "Cena":
                cenContador++;
                cenContadorLabel.setText("Cenas servidas: " + cenContador);

                break;
            case "Colacion":
                colContador++;
                colContadorLabel.setText("Colaciones servidas: " + colContador);
                break;
            default:
                break;
        }
        this.revalidate();
        dbC.closeConnection();
    }

    private void displayComensal(Persona ps) {

        try {
            Image img = ImageIO.read(ps.getFoto().getBinaryStream());
            if (img != null) {
                ImageIcon image = new ImageIcon(img);
                if (image.getIconHeight() > 160 || image.getIconWidth() > 160) {
                    ImageIcon imageScalada = new ImageIcon(image.getImage().getScaledInstance(200, 200, 200));
                    imgLabel.setIcon(imageScalada);
                } else {
                    imgLabel.setIcon(image);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(WebcamFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(WebcamFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        nombreLabel.setText(ps.getNombre() + " " + ps.getApellidos());
        apellidosLabel.setText(ps.getApellidos());
        rangoLabel.setText(ps.getRango());
        permisoLabel.setText(ps.getPermiso());

        this.validate();
    }

    private void ponerLogos() {
        logo1Label = new JLabel();
        logo2Label = new JLabel();
        logo1Label.setIcon(new ImageIcon(getClass().getResource("/GUI/img/Untitled-1.png")));
        logo2Label.setIcon(new ImageIcon(getClass().getResource("/GUI/img/Untitled-2.png")));
    }

    private boolean aunEstaCorrectaLaInformacion() {
        DBConnection dbC = new DBConnection();
        String com[] = dbC.queComidaToca();
        //System.out.println(com[0] + "-" + com[1] + "-" + com[2] + ":" + com[3]);
        if(comidaActual == null && com == null){
            //System.out.println("Ambos son nulls");
            return true;
        }else if(comidaActual != null && com == null){
            //System.out.println("La base de datos regreso NULL");
            return false;
        }else if(comidaActual == null && com != null)
            return false;
        else{
            System.out.println(comidaActual +" - " + com[1]);
            return comidaActual.equals(com[1]);
        }
    }

}
