package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import java.sql.SQLException;
import java.util.List;

public class BBDDRepositorioCategoriasJava {
    private final DataBaseConnection db;
    private CategoriaDao dao;

    public BBDDRepositorioCategoriasJava(String path) throws Exception {
        this.db = new DataBaseConnection();
        this.db.setConfig_path(path);
        this.db.open();
        this.dao = new CategoriaDao();
        this.dao.setConn(this.db);
    }

    public void add(Categoria item) {
        this.dao.insert(item);
    }

    public void update(Categoria item) {
        this.dao.update(item);
    }

    public void remove(String id) { // ID es String
        this.dao.delete(id);
    }

    public Categoria getById(String id) { // ID es String
        return this.dao.getById(id);
    }

    public List<Categoria> getAll() {
        return this.dao.getAll();
    }

    public void close() {
        try {
            this.db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}