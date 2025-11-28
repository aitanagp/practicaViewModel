package ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos;

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PedidoDao {
    private DataBaseConnection conn;

    public void setConn(DataBaseConnection db) {
        this.conn = db;
    }

    // INSERTAR PEDIDO COMPLETO (Cabecera + Líneas)
    public void insert(Pedido pedido) {
        String sqlPedido = "INSERT INTO Pedidos (id, cliente, fecha, estado, total) VALUES (?, ?, ?, ?, ?)";
        String sqlLinea = "INSERT INTO LineasPedido (id, pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?)";

        try {
            // 1. Insertar Cabecera
            PreparedStatement pst = conn.getConnection().prepareStatement(sqlPedido);
            pst.setString(1, pedido.getId());
            pst.setString(2, pedido.getCliente());
            pst.setString(3, pedido.getFecha()); // Asumimos formato fecha String correcto o Timestamp
            pst.setString(4, pedido.getEstado());
            pst.setDouble(5, pedido.getTotal());
            pst.executeUpdate();

            // 2. Insertar Líneas
            for (LineaPedido linea : pedido.getLineas()) {
                PreparedStatement pstLinea = conn.getConnection().prepareStatement(sqlLinea);
                pstLinea.setString(1, linea.getId());
                pstLinea.setString(2, pedido.getId()); // FK al pedido padre
                pstLinea.setString(3, linea.getProductoId());
                pstLinea.setInt(4, linea.getCantidad());
                pstLinea.setDouble(5, linea.getPrecioUnitario());
                pstLinea.executeUpdate();
            }

            Logger.getLogger(PedidoDao.class.getName()).info("Pedido insertado: " + pedido.getId());

        } catch (SQLException ex) {
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // OBTENER TODOS LOS PEDIDOS
    public List<Pedido> getAll() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pedidos ORDER BY fecha DESC";

        try (Statement st = conn.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Para el listado general, a veces no necesitamos las línea,
                // pero aquí cargamos todo para simplificaar
                Pedido p = mapRowToPedido(rs);
                // Cargamos sus líneas
                List<LineaPedido> lineas = getLineasByPedidoId(p.getId());
                // Reconstruimos el pedido con las líneas (Kotlin data class es inmutable,
                // pero desde Java podemos usar un constructor o helper si hiciera falta,
                // aquí asumimos que pasamos la lista en el constructor o usamos un setter ficticio)
                // Como Pedido es Data Class Kotlin, mejor crearlo todo junto si es posible,
                // o usar un constructor auxiliar.
                // *Truco*: Creamos el objeto completo aquí:
                Pedido pedidoCompleto = new Pedido(
                        p.getId(), p.getCliente(), p.getFecha(), p.getEstado(), p.getTotal(), lineas
                );
                lista.add(pedidoCompleto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // OBTENER LÍNEAS DE UN PEDIDO
    private List<LineaPedido> getLineasByPedidoId(String pedidoId) {
        List<LineaPedido> lineas = new ArrayList<>();
        String sql = "SELECT * FROM LineasPedido WHERE pedido_id = ?";
        try (PreparedStatement pst = conn.getConnection().prepareStatement(sql)) {
            pst.setString(1, pedidoId);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                lineas.add(new LineaPedido(
                        rs.getString("id"),
                        rs.getString("producto_id"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lineas;
    }

    private Pedido mapRowToPedido(ResultSet rs) throws SQLException {
        // Retorna pedido sin líneas inicialmente
        return new Pedido(
                rs.getString("id"),
                rs.getString("cliente"),
                rs.getString("fecha"), // MySQL devuelve DATETIME, Java lo lee como String
                rs.getString("estado"),
                rs.getDouble("total"),
                new ArrayList<>() // Lista vacía temporal
        );
    }

    public void delete(Pedido item) {
    }
}