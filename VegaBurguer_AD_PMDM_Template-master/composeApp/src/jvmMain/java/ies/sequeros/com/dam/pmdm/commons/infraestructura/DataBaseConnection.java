package ies.sequeros.com.dam.pmdm.commons.infraestructura;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {
    private String configPath;
    private Connection conexion;

    public DataBaseConnection() {
    }

    // Este método lo llama tu repositorio para pasarle la ruta "/app.properties"
    public void setConfig_path(String path) {
        this.configPath = path;
    }
    /*
    public void open() throws Exception {
        FileReader fr = null;
        File f =new File(System.getProperty("user.dir")+
                this.getConfig_path());
        fr = new FileReader(f);
        Properties props = new Properties();
        try {
            props.load(fr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String user = props.getProperty("database.user");
        String password = props.getProperty("database.password");
        this.connection_string = props.getProperty("database.path")
                + ";user=" + user + ";password=" + password;
        this.conexion =
                DriverManager.getConnection(this.connection_string);
    }
     */
    public void open() throws Exception {
        Properties props = new Properties();
        // Quitamos la barra inicial si existe para que funcione getResourceAsStream
        String resourceName = (configPath != null && configPath.startsWith("/")) ? configPath.substring(1) : configPath;
        if (resourceName == null) resourceName = "app.properties";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IOException("No se encuentra el archivo de propiedades: " + resourceName);
            }
            props.load(input);

            // Cargamos los datos del fichero
            String url = props.getProperty("database.path");
            String user = props.getProperty("database.user");
            String pass = props.getProperty("database.password");
            String driver = props.getProperty("database.driver");

            // Cargar driver
            if (driver != null) Class.forName(driver);

            // Conectar
            this.conexion = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión a base de datos abierta: " + url);

        } catch (Exception e) {
            System.err.println("Error al conectar: " + e.getMessage());
            throw e;
        }
    }

    public Connection getConnection() {
        return this.conexion;
    }

    public void close() throws SQLException {
        if (this.conexion != null && !this.conexion.isClosed()) {
            this.conexion.close();
        }
    }
}