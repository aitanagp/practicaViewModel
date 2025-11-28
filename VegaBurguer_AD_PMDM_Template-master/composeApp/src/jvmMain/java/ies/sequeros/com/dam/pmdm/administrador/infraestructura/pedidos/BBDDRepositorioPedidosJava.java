package ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos;

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes.DependienteDao;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import java.util.List;

public class BBDDRepositorioPedidosJava {
    private final DataBaseConnection db;
    private PedidoDao dao;

    public BBDDRepositorioPedidosJava(String path) throws Exception {
        super();
        this.db = new DataBaseConnection();
        this.db.setConfig_path(path);
        this.db.open();
        dao= new PedidoDao();
        dao.setConn(this.db);
    }

    public void add(Pedido pedido) {
        this.dao.insert(pedido);
    }
    public boolean remove(Pedido item){
        this.dao.delete(item);
        return true;
    }


    public List<Pedido> getAll() {
        return this.dao.getAll();
    }
    public void close() {
        try {
            this.db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}