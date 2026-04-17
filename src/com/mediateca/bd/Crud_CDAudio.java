package com.mediateca.bd;

import mediateca.modelos.CDAudio;
import utilidades.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Crud_CDAudio {

    public boolean agregarCD(CDAudio c) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO Material (codigo_interno, titulo) VALUES (?,?)")) {
                ps.setString(1, c.getCodigoInterno());
                ps.setString(2, c.getTitulo());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO MaterialAudiovisual (codigo_interno, duracion, genero) VALUES (?,?,?)")) {
                ps.setString(1, c.getCodigoInterno());
                ps.setString(2, c.getDuracion());
                ps.setString(3, c.getGenero());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO cd (codigo_interno, artista, num_canciones, unidades_disponibles) VALUES (?,?,?,?)")) {
                ps.setString(1, c.getCodigoInterno());
                ps.setString(2, c.getArtista());
                ps.setInt(3, c.getNumCanciones());
                ps.setInt(4, c.getUnidadesDisponibles());
                ps.executeUpdate();
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            if(cn != null) try { cn.rollback(); } catch(Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            if(cn != null) try { cn.close(); } catch(Exception e) {}
        }
    }

    public boolean actualizarCD(CDAudio c) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            try (PreparedStatement ps = cn.prepareStatement("UPDATE Material SET titulo=? WHERE codigo_interno=?")) {
                ps.setString(1, c.getTitulo());
                ps.setString(2, c.getCodigoInterno());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE MaterialAudiovisual SET duracion=?, genero=? WHERE codigo_interno=?")) {
                ps.setString(1, c.getDuracion());
                ps.setString(2, c.getGenero());
                ps.setString(3, c.getCodigoInterno());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE cd SET artista=?, num_canciones=?, unidades_disponibles=? WHERE codigo_interno=?")) {
                ps.setString(1, c.getArtista());
                ps.setInt(2, c.getNumCanciones());
                ps.setInt(3, c.getUnidadesDisponibles());
                ps.setString(4, c.getCodigoInterno());
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

    public List<CDAudio> listarCDs() {
        List<CDAudio> lista = new ArrayList<>();
        String sql = "SELECT m.codigo_interno, m.titulo, av.duracion, av.genero, c.artista, c.num_canciones, c.unidades_disponibles " +
                "FROM cd c JOIN MaterialAudiovisual av ON c.codigo_interno = av.codigo_interno " +
                "JOIN Material m ON av.codigo_interno = m.codigo_interno";
        try (Connection cn = new Conexion().getConexion(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new CDAudio(rs.getString(1), rs.getString(2), rs.getString(5), rs.getString(4), rs.getString(3), rs.getInt(6), rs.getInt(7)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean eliminarCD(String codigo) {
        String sql = "DELETE FROM Material WHERE codigo_interno = ?";
        try (Connection cn = new Conexion().getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}