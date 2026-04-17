package com.mediateca.bd;

import mediateca.modelos.Revista;
import utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Crud_Revista {

    public boolean agregarRevista(Revista r) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO Material (codigo_interno, titulo) VALUES (?,?)")) {
                ps.setString(1, r.getCodigoInterno()); ps.setString(2, r.getTitulo()); ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO MaterialEscrito (codigo_interno, editorial) VALUES (?,?)")) {
                ps.setString(1, r.getCodigoInterno()); ps.setString(2, r.getEditorial()); ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO Revista (codigo_interno, periodicidad, fecha_publicacion, unidades_disponibles) VALUES (?,?,?,?)")) {
                ps.setString(1, r.getCodigoInterno()); ps.setString(2, r.getPeriodicidad());
                ps.setString(3, r.getFechaPublicacion()); ps.setInt(4, r.getUnidadesDisponibles()); ps.executeUpdate();
            }
            cn.commit(); return true;
        } catch (SQLException e) { if(cn!=null) try {cn.rollback();}catch(Exception ex){} e.printStackTrace(); return false; }
        finally { if(cn!=null) try {cn.close();} catch(Exception e){} }
    }

    public boolean actualizarRevista(Revista r) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            try (PreparedStatement ps = cn.prepareStatement("UPDATE Material SET titulo=? WHERE codigo_interno=?")) {
                ps.setString(1, r.getTitulo()); ps.setString(2, r.getCodigoInterno()); ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE MaterialEscrito SET editorial=? WHERE codigo_interno=?")) {
                ps.setString(1, r.getEditorial()); ps.setString(2, r.getCodigoInterno()); ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE Revista SET periodicidad=?, fecha_publicacion=?, unidades_disponibles=? WHERE codigo_interno=?")) {
                ps.setString(1, r.getPeriodicidad()); ps.setString(2, r.getFechaPublicacion());
                ps.setInt(3, r.getUnidadesDisponibles()); ps.setString(4, r.getCodigoInterno()); ps.executeUpdate();
            }
            cn.commit(); return true;
        } catch (SQLException e) { if(cn!=null) try {cn.rollback();}catch(Exception ex){} e.printStackTrace(); return false; }
        finally { if(cn!=null) try {cn.close();} catch(Exception e){} }
    }

    public boolean eliminarRevista(String codigo) {
        String sql = "DELETE FROM Material WHERE codigo_interno = ?";
        try (Connection cn = new Conexion().getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Revista> listarRevistas() {
        List<Revista> lista = new ArrayList<>();
        String sql = "SELECT m.codigo_interno, m.titulo, e.editorial, r.periodicidad, r.fecha_publicacion, r.unidades_disponibles " +
                "FROM Revista r JOIN MaterialEscrito e ON r.codigo_interno = e.codigo_interno " +
                "JOIN Material m ON e.codigo_interno = m.codigo_interno";
        try (Connection cn = new Conexion().getConexion(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Revista(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}