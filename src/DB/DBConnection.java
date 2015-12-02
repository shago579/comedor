package DB;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class DBConnection {

    private  String DB_URL = "jdbc:mysql://";

    //  Database credentials
    private static String USER;
    private static String PASS;
    private static String HOST;
    private static String SCHEMA;
    
    Connection conn;

    public DBConnection() {
        
        
        String jsonTxt = readJSON();
        JSONObject json = new JSONObject(jsonTxt);
        
        USER = json.getString("Usuario");
        PASS = json.getString("Password");
        HOST = json.getString("Host");
        SCHEMA = json.getString("Base de Datos");
        
        DB_URL = DB_URL + HOST + "/" + SCHEMA;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
        } catch (ClassNotFoundException ex) {
            System.out.println("Algo espantoso acaba de suceder :'( ");
        } catch (SQLException ex) {
            //System.out.println( ex.getErrorCode() + " creo que estan mal las contraseñas");
            ex.printStackTrace();
        }
    }
    
    private String readJSON(){
        BufferedReader br = null;
        String fullTxt="";
        
        try {
            
            String sCurrentLine;
            
            br = new BufferedReader(new FileReader("C:\\basededatos.json"));
            
            while ((sCurrentLine = br.readLine()) != null) {
		System.out.println(sCurrentLine);
                fullTxt += sCurrentLine;
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                if(br != null) br.close();
            } catch(IOException e){
                
            }
        }
        
        System.out.println("Full text: " + fullTxt);
        return fullTxt;
    }
    
    public int getAuthorizationLevel(String username, String password){
        try {
            System.out.println(username +" : " + password);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM usuarios WHERE username = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt(4);
            }
        } catch (SQLException ex) {
            System.out.println("No existe Usuario");
        }
        return 0;
    }
    
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean agregarUsuario(String nombre, String apellidos, int permisos, 
            int rango, String curp, FileInputStream foto){
        try {
            String sql = "INSERT INTO comensales(curp, nombre, apellidos, permisos, rango, foto) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, curp);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellidos);
            pstmt.setInt(4, permisos);
            pstmt.setInt(5, rango);
            pstmt.setBinaryStream(6, foto);
            
            return pstmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Persona getUsuario(String curp){
        System.out.println("Buscando comensal con CURP " + curp);
        String sql = "SELECT CURP, nombre, apellidos, rango.rango, permiso, foto FROM comensales " +
            "INNER JOIN permisos ON permisos = permisoId " +
            "INNER JOIN rango ON comensales.rango = idRango " +
            "WHERE CURP = ?";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, curp);
            
            ResultSet rs = pstmt.executeQuery();
            
            if( rs.next() ){
                String nombre = rs.getString(2);
                String apellidos = rs.getString(3);
                String rango = rs.getString(4);
                String permiso = rs.getString(5);
                Blob foto = rs.getBlob(6);
                System.out.println();
                Persona ps = new Persona(curp, nombre, apellidos, rango, 
                        permiso, foto);
                System.out.println("Encontrado a " + nombre + " " + apellidos);
                return ps;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void registrarPersona(Persona ps) {
        String sql = "INSERT INTO registro (idComensal, idComida, horaComida) "+
                "VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String comida[] = queComidaToca();
            pstmt.setString(1, ps.getCurp());
            pstmt.setInt(2, Integer.parseInt(comida[0]));
            pstmt.setString(3, getNowDateTime());
            
            pstmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean puedeComer(Persona ps){
        return tienePermiso(ps) && noHaComido(ps);
    }
    
     public Persona getUsuario(String nombre, String apellido){
        //System.out.println("Buscando comensal con CURP " + curp);
        String sql = "SELECT CURP, nombre, apellidos, rango, permisos, foto FROM comensales " +
            "WHERE nombre = ? and apellidos = ?";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            
            ResultSet rs = pstmt.executeQuery();
            
            if( rs.next() ){
                String curp=rs.getString(1);
                String nombres = rs.getString(2);
                String apellidos = rs.getString(3);
                String rango = rs.getString(4);
                String permiso = rs.getString(5);
                Blob foto = rs.getBlob(6);
                System.out.println();
                Persona ps = new Persona(curp, nombre, apellidos, rango, 
                        permiso, foto);
                System.out.println("Encontrado a " + nombre + " " + apellidos);
                return ps;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    

    private boolean tienePermiso(Persona ps){
        String permiso = ps.getPermiso();
        String sql = "SELECT permiso, comida FROM permisoscomidas " +
                "INNER JOIN permisos ON permisoscomidas.idPermiso = permisos.permisoId " +
                "INNER JOIN comidas ON permisoscomidas.idComida = comidas.idComida " +
                "WHERE permiso = ? AND comida = ? ";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, permiso);
            String comida[] = queComidaToca();
            if(comida == null){
                return false;
            }
            
            pstmt.setString(2, comida[1]);
            
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();
        
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String[] queComidaToca(){
        String comida[] = new String[4];
        String sql = "SELECT * FROM comidas " +
                "WHERE TIME(horaInicio) <= ? " + 
                "AND TIME(horaFinal) >= ? ";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String hora = getNowHourMinSeg();
            pstmt.setString(1, hora);
            pstmt.setString(2, hora);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
                comida[0] = rs.getInt(1) + "";
                comida[1] = rs.getString(2);
                comida[2] = rs.getTime(3).toString();
                comida[3] = rs.getTime(4).toString();
                System.out.print("Returning " + comida[0] + ":" + comida[1]);
                System.out.println(":" + comida[2] + ":"+comida[3]);
                return comida;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private boolean noHaComido(Persona ps){
        String sql = "SELECT * FROM registro " +
                "WHERE idComensal = ? " +
                "AND DATE(horaComida) = ? " +
                "AND HOUR(horaComida) >= ? " +
                "AND HOUR(horaComida) <= ? ";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            String comida[] = queComidaToca();
            pstmt.setString(1, ps.getCurp());
            pstmt.setString(2, getNowDate());
            pstmt.setString(3, comida[2]);
            pstmt.setString(4, comida[3]);
            
            ResultSet rs = pstmt.executeQuery();
            
            return !rs.next();
        
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    private String getNowDateTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private String getNowDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    private String getNowHourMinSeg(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }
    
    public String getReporte(String where){
        String sql = "SELECT idComensal, nombre, apellidos, comida, horaComida "+
                "FROM registro INNER JOIN comidas "+
                "ON comidas.idComida = registro.idComida " +
                "INNER JOIN comensales ON registro.idComensal = comensales.CURP "
                + where;
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            String iniFin = "+-------+--------------------+---------------------------------+"+
                    "---------------------------------+----------------+------------------------+"+System.lineSeparator();
            String top = "+  No.  +        CURP        +            Nombre(s)            +"+
                    "            Apellidos            +     Comida     +    Hora del Consumo    +"+System.lineSeparator();
            String format = "|%-6d | %-18s | %-31s | %-31s | %-14s | %-22s | "+System.lineSeparator();
            
            StringBuilder sBu = new StringBuilder();
            Formatter formatter = new Formatter(sBu);
            
            sBu.append(iniFin);
            sBu.append(top);
            sBu.append(iniFin);
            
            int x = 0;
            while(rs.next()){
                String curp = rs.getString(1);
                String nombre = rs.getString(2);
                String apellidos = rs.getString(3);
                String comida = rs.getString(4);
                String date = rs.getString(5);
                
                formatter.format(format, ++x, curp, nombre, 
                        apellidos, comida, date);
            }
            sBu.append(iniFin);
            return sBu.toString();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public void deleteUsuario(String curp){
        String sql = "DELETE FROM comensales WHERE curp = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, curp);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void modificarUsuario(String curp, String nombre, String apellido, int rango, int permiso){
        String sql = "Update comensales set  nombre=?,apellidos=?,permisos=?,rango=? where CURP=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            //pstmt.setString(3,curp);
            pstmt.setInt(3, permiso);
            pstmt.setInt(4, rango);
            pstmt.setString(5, curp);
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
