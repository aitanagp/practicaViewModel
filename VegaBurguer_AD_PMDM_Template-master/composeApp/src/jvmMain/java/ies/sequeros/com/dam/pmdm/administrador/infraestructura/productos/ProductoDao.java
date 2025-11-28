package ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDao {
    private DataBaseConnection conn;
    private final String table_name = "Productos";

    private final String selectAll = "SELECT * FROM " + table_name;
    private final String selectById = "SELECT * FROM " + table_name + " WHERE id=?";
    private final String selectByCategoria = "SELECT * FROM " + table_name + " WHERE categoria_id=?";
    private final String insert = "INSERT INTO " + table_name + " (id, nombre, descripcion, precio, imgPath, categoria_id, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String update = "UPDATE " + table_name + " SET nombre=?, descripcion=?, precio=?, imgPath=?, categoria_id=?, activo=? WHERE id=?";
    private final String delete = "DELETE FROM " + table_name + " WHERE id=?";

    public void setConn(DataBaseConnection db) {
        this.conn = db;
    }

    public List<Producto> getAll() {
        List<Producto> lista = new ArrayList<>();
        try (Statement st = conn.getConnection().createStatement();
             ResultSet rs = st.executeQuery(selectAll)) {
            while (rs.next()) {
                lista.add(mapRowToProducto(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public Producto getById(String id) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(selectById)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapRowToProducto(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Producto> getByCategoria(String categoriaId) {
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement pst = conn.getConnection().prepareStatement(selectByCategoria)) {
            pst.setString(1, categoriaId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(mapRowToProducto(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public void insert(Producto item) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(insert)) {
            pst.setString(1, item.getId());
            pst.setString(2, item.getNombre());
            pst.setString(3, item.getDescripcion());
            pst.setDouble(4, item.getPrecio());
            pst.setString(5, item.getImgPath());
            pst.setString(6, item.getCategoriaId());
            pst.setBoolean(7, item.getActivo());
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Producto item) {
        try (PreparedStatement pst = conn.getConnection().prepareStatement(update)) {
            pst.setString(1, item.getNombre());
            pst.setString(2, item.getDescripcion());
            pst.setDouble(3, item.getPrecio());
            pst.setString(4, item.getImgPath());
            pst.setString(5, item.getCategoriaId());
            pst.setBoolean(6, item.getActivo());
            pst.setString(7, item.getId());
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

    private Producto mapRowToProducto(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getString("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getString("imgPath"),
                rs.getString("categoria_id"),
                rs.getBoolean("activo")
        );
    }
}