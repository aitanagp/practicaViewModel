package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao {
    private DataBaseConnection conn;
    private final String table_name = "Categorias";

    // Consultas SQL
    private final String selectAll = "SELECT * FROM " + table_name;
    private final String selectById = "SELECT * FROM " + table_name + " WHERE id=?";
    // Insertamos el ID manualmente porque ahora es un UUID (String)
    private final String insert = "INSERT INTO " + table_name + " (id, nombre, imgPath, activa) VALUES (?, ?, ?, ?)";
    private final String update = "UPDATE " + table_name + " SET nombre=?, imgPath=?, activa=? WHERE id=?";
    private final String delete = "DELETE FROM " + table_name + " WHERE id=?";

    public void setConn(DataBaseConnection db) {
        this.conn = db;
    }

    public List<Categoria> getAll() {
        List<Categoria> lista = new ArrayList<>();
        try (Statement st = conn.getConnection().createStatement();
             ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                lista.add(mapRowToCategoria(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public Categoria getById(String id) { // ID es String
        try (PreparedStatement pst = conn.getConnection().prepareStatement(selectById)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapRowToCategoria(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void insert(Categoria item) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(insert)) {
            pst.setString(1, item.getId()); // UUID
            pst.setString(2, item.getNombre());
            pst.setString(3, item.getImgpath()); // Cuidado con la mayúscula/minúscula de tu modelo
            pst.setBoolean(4, item.getActiva());
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Categoria item) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(update)) {
            pst.setString(1, item.getNombre());
            pst.setString(2, item.getImgpath());
            pst.setBoolean(3, item.getActiva());
            pst.setString(4, item.getId()); // WHERE id
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String id) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(delete)) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Categoria mapRowToCategoria(ResultSet rs) throws SQLException {
        // El orden en el constructor debe coincidir con tu clase Kotlin Categoria
        return new Categoria(
                rs.getString("id"),
                rs.getString("nombre"),
                rs.getString("imgPath"),
                rs.getBoolean("activa")
        );
    }
}