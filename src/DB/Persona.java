package DB;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Persona {
    
    private String curp;
    private String nombre;
    private String apellidos;
    private String rango;
    private String permiso;
    private Blob foto;

     public Persona(String curp, String nombre, String apellidos,
            String rango, String permiso, Blob foto){
        this.curp = curp;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.rango = rango;
        this.permiso = permiso;
        this.foto = foto;
    }
    
    public String getCurp() {
        return curp;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getRango() {
        return rango;
    }

    public String getPermiso() {
        return permiso;
    }

    public Blob getFoto() {
        return foto;
    }
    
    private byte[] fromBlobToByteArray(){
        try {
            byte[] inst = foto.getBytes(0, (int)foto.length() );
            return inst;
        } catch (SQLException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
    Blob blob = rs.getBlob("foto");
    if(blob !=null){
        System.out.println(blob);
        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
        System.out.println(i);
        if(i!=null){
            ImageIcon image = new ImageIcon(i);
            if(image.getIconHeight() > 342 || image.getIconWidth() > 230){
               ImageIcon imageScalada = new ImageIcon(image.getImage().getScaledInstance(200, 200, 200));
               imagen.setIcon(imageScalada);
            }
        
           else{
                imagen.setIcon(image);
           }  
        }
    }
     */
    
}
