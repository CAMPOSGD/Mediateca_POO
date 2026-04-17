package com.mediateca.bd;

import mediateca.modelos.DVD;
import utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Crud_DVD {

    public boolean insertarDVD(String codigo, String titulo, String director, String duracion, String genero, int unidades) {
        DVD d = new DVD(codigo, titulo, director, duracion, genero, unidades);
        return agregarDVD(d);
    }

    public boolean agregarDVD(DVD d) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO Material (codigo_interno, titulo) VALUES (?,?)")) {
                ps.setString(1, d.getCodigoInterno());
                ps.setString(2, d.getTitulo());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO MaterialAudiovisual (codigo_interno, duracion, genero) VALUES (?,?,?)")) {
                ps.setString(1, d.getCodigoInterno());
                ps.setString(2, d.getDuracion());
                ps.setString(3, d.getGenero());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO DVD (codigo_interno, director, unidades_disponibles) VALUES (?,?,?)")) {
                ps.setString(1, d.getCodigoInterno());
                ps.setString(2, d.getDirector());
                ps.setInt(3, d.getUnidadesDisponibles());
                ps.executeUpdate();
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            if(cn != null) try { cn.rollback(); } catch(Exception ex) {}
            return false;
        } finally {
            if(cn != null) try { cn.close(); } catch(Exception e) {}
        }
    }

    public boolean actualizarDVD(DVD d) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            try (PreparedStatement ps = cn.prepareStatement("UPDATE Material SET titulo=? WHERE codigo_interno=?")) {
                ps.setString(1, d.getTitulo());
                ps.setString(2, d.getCodigoInterno());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE MaterialAudiovisual SET duracion=?, genero=? WHERE codigo_interno=?")) {
                ps.setString(1, d.getDuracion());
                ps.setString(2, d.getGenero());
                ps.setString(3, d.getCodigoInterno());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE DVD SET director=?, unidades_disponibles=? WHERE codigo_interno=?")) {
                ps.setString(1, d.getDirector());
                ps.setInt(2, d.getUnidadesDisponibles());
                ps.setString(3, d.getCodigoInterno());
                ps.executeUpdate();
            }
            cn.commit();
            return true;
        } catch (SQLException e) {
            if(cn != null) try { cn.rollback(); } catch(Exception ex) {}
            return false;
        } finally {
            if(cn != null) try { cn.close(); } catch(Exception e) {}
        }
    }

    public List<DVD> listarDVDs() {
        List<DVD> lista = new ArrayList<>();
        String sql = "SELECT m.codigo_interno, m.titulo, av.duracion, av.genero, d.director, d.unidades_disponibles " +
                "FROM DVD d JOIN MaterialAudiovisual av ON d.codigo_interno = av.codigo_interno " +
                "JOIN Material m ON av.codigo_interno = m.codigo_interno";
        try (Connection cn = new Conexion().getConexion(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new DVD(rs.getString(1), rs.getString(2), rs.getString(5), rs.getString(3), rs.getString(4), rs.getInt(6)));
            }
        } catch (SQLException e) {}
        return lista;
    }

    public boolean eliminarDVD(String codigo) {
        String sql = "DELETE FROM Material WHERE codigo_interno = ?";
        try (Connection cn = new Conexion().getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}