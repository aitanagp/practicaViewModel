package ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
// Si usas IDao genérico, descomenta la siguiente línea:
// import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DependienteDao {

    private DataBaseConnection conn;
    private final String table_name = "Dependientes";

    private final String insert = "INSERT INTO " + table_name + " (id, name, email, password, image_path, enabled, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String update = "UPDATE " + table_name + " SET name=?, email=?, password=?, image_path=?, enabled=?, is_admin=? WHERE id=?";
    private final String delete = "DELETE FROM " + table_name + " WHERE id=?";
    private final String selectAll = "SELECT * FROM " + table_name;
    private final String selectById = "SELECT * FROM " + table_name + " WHERE id=?";

    public void setConn(DataBaseConnection db) {
        this.conn = db;
    }

    public void insert(Dependiente item) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(insert)) {
            pst.setString(1, item.getId());
            pst.setString(2, item.getName());
            pst.setString(3, item.getEmail());
            pst.setString(4, item.getPassword());
            pst.setString(5, item.getImagePath());
            pst.setBoolean(6, item.getEnabled());
            pst.setBoolean(7, item.isAdmin());

            pst.executeUpdate();
            Logger.getLogger(DependienteDao.class.getName()).info("Insertado: " + item.getName());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Dependiente item) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(update)) {
            pst.setString(1, item.getName());
            pst.setString(2, item.getEmail());
            pst.setString(3, item.getPassword());
            pst.setString(4, item.getImagePath());
            pst.setBoolean(5, item.getEnabled());
            pst.setBoolean(6, item.isAdmin());
            pst.setString(7, item.getId()); // WHERE id = ?

            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Dependiente item) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(delete)) {
            pst.setString(1, item.getId());
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Sobrecarga para borrar por ID
    public void delete(String id) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(delete)) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Dependiente> getAll() {
        List<Dependiente> lista = new ArrayList<>();
        try (Statement st = conn.getConnection().createStatement();
             ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                lista.add(mapRowToDependiente(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public Dependiente getById(String id) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(selectById)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapRowToDependiente(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // Ojo: Kotlin espera Dependiente?, así que null es válido si se maneja
    }

    // Mapeo manual de columnas SQL -> Objeto Kotlin
    private Dependiente mapRowToDependiente(ResultSet rs) throws SQLException {
        // El orden de los argumentos debe coincidir con el constructor de Data Class Kotlin
        return new Dependiente(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("image_path"),
                rs.getBoolean("enabled"),
                rs.getBoolean("is_admin")
        );
    }
}