package ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import java.sql.SQLException;
import java.util.List;

public class BBDDRepositorioProductosJava {
    private final DataBaseConnection db;
    private ProductoDao dao;

    public BBDDRepositorioProductosJava(String path) throws Exception {
        this.db = new DataBaseConnection();
        this.db.setConfig_path(path);
        this.db.open();
        this.dao = new ProductoDao();
        this.dao.setConn(this.db);
    }

    public void add(Producto item) {
        this.dao.insert(item);
    }

    public void update(Producto item) {
        this.dao.update(item);
    }

    public void remove(String id) {
        this.dao.delete(id);
    }

    public Producto getById(String id) {
        return this.dao.getById(id);
    }

    public List<Producto> getAll() {
        return this.dao.getAll();
    }

    public List<Producto> getByCategoria(String categoriaId) {
        return this.dao.getByCategoria(categoriaId);
    }

    public void close() {
        try {
            this.db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}