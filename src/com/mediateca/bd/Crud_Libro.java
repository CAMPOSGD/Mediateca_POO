package com.mediateca.bd;

import mediateca.modelos.Libro;
import utilidades.Conexion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Crud_Libro {
    private static final Logger log = LogManager.getLogger(Crud_Libro.class);

    public boolean agregarLibro(Libro libro) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            String sqlMaterial = "INSERT INTO Material (codigo_interno, titulo) VALUES (?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlMaterial)) {
                ps.setString(1, libro.getCodigoInterno());
                ps.setString(2, libro.getTitulo());
                ps.executeUpdate();
            }

            String sqlEscrito = "INSERT INTO MaterialEscrito (codigo_interno, editorial) VALUES (?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlEscrito)) {
                ps.setString(1, libro.getCodigoInterno());
                ps.setString(2, libro.getEditorial());
                ps.executeUpdate();
            }

            String sqlLibro = "INSERT INTO Libro (codigo_interno, autor, numero_paginas, isbn, anio_publicacion, unidades_disponibles) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlLibro)) {
                ps.setString(1, libro.getCodigoInterno());
                ps.setString(2, libro.getAutor());
                ps.setInt(3, libro.getNumPaginas());
                ps.setString(4, libro.getIsbn());
                ps.setInt(5, libro.getAnioPublicacion());
                ps.setInt(6, libro.getUnidadesDisponibles());
                ps.executeUpdate();
            }

            cn.commit();
            log.info("Libro guardado en los 3 niveles: " + libro.getCodigoInterno());
            return true;
        } catch (SQLException e) {
            if (cn != null) try { cn.rollback(); } catch (SQLException ex) { }
            log.error("Error en transacción: " + e.getMessage());
            return false;
        } finally {
            if (cn != null) try { cn.close(); } catch (SQLException e) { }
        }
    }

    public List<Libro> listarLibros() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT m.codigo_interno, m.titulo, e.editorial, l.autor, l.numero_paginas, l.isbn, l.anio_publicacion, l.unidades_disponibles " +
                "FROM Libro l " +
                "JOIN MaterialEscrito e ON l.codigo_interno = e.codigo_interno " +
                "JOIN Material m ON e.codigo_interno = m.codigo_interno";
        Conexion db = new Conexion();
        try (Connection cn = db.getConexion(); Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Libro(
                        rs.getString("codigo_interno"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("numero_paginas"),
                        rs.getString("editorial"),
                        rs.getString("isbn"),
                        rs.getInt("anio_publicacion"),
                        rs.getInt("unidades_disponibles")
                ));
            }
        } catch (SQLException e) { log.error("Error al listar: " + e.getMessage()); }
        return lista;
    }

    public boolean actualizarLibro(Libro libro) {
        Conexion db = new Conexion();
        Connection cn = null;
        try {
            cn = db.getConexion();
            cn.setAutoCommit(false);

            String sqlMat = "UPDATE Material SET titulo=? WHERE codigo_interno=?";
            try (PreparedStatement ps = cn.prepareStatement(sqlMat)) {
                ps.setString(1, libro.getTitulo());
                ps.setString(2, libro.getCodigoInterno());
                ps.executeUpdate();
            }

            String sqlEsc = "UPDATE MaterialEscrito SET editorial=? WHERE codigo_interno=?";
            try (PreparedStatement ps = cn.prepareStatement(sqlEsc)) {
                ps.setString(1, libro.getEditorial());
                ps.setString(2, libro.getCodigoInterno());
                ps.executeUpdate();
            }

            String sqlLib = "UPDATE Libro SET autor=?, numero_paginas=?, isbn=?, anio_publicacion=?, unidades_disponibles=? WHERE codigo_interno=?";
            try (PreparedStatement ps = cn.prepareStatement(sqlLib)) {
                ps.setString(1, libro.getAutor());
                ps.setInt(2, libro.getNumPaginas());
                ps.setString(3, libro.getIsbn());
                ps.setInt(4, libro.getAnioPublicacion());
                ps.setInt(5, libro.getUnidadesDisponibles());
                ps.setString(6, libro.getCodigoInterno());
                ps.executeUpdate();
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            if (cn != null) try { cn.rollback(); } catch (SQLException ex) { }
            return false;
        } finally {
            if (cn != null) try { cn.close(); } catch (SQLException e) { }
        }
    }

    public boolean eliminarLibro(String codigo) {
        String sql = "DELETE FROM Material WHERE codigo_interno = ?";
        Conexion db = new Conexion();
        try (Connection cn = db.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}