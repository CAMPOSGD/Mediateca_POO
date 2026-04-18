package gui;

import com.mediateca.bd.*;
import mediateca.modelos.*;
import utilidades.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MenuPrincipal extends JFrame {

    private JTabbedPane pestanasGlobales;
    private JPanel panelAgregarContenedor;
    private CardLayout navegadorAgregar;

    public MenuPrincipal() {
        setTitle("Sistema Mediateca UDB - Gestión Unificada");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pestanasGlobales = new JTabbedPane();

        panelAgregarContenedor = new JPanel();
        navegadorAgregar = new CardLayout();
        panelAgregarContenedor.setLayout(navegadorAgregar);

        panelAgregarContenedor.add(crearMenuSeleccionAgregar(), "MENU_BOTONES");

        pestanasGlobales.addTab("Agregar Material", panelAgregarContenedor);
        pestanasGlobales.addTab("Modificar Material", crearPanelModificarGlobal());
        pestanasGlobales.addTab("Borrar Material", crearPanelBorrarGlobal());
        pestanasGlobales.addTab("Materiales Disponibles", crearPanelDisponiblesGlobal());

        add(pestanasGlobales);
    }

    private JPanel crearMenuSeleccionAgregar() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton btnLibro = new JButton("Agregar Libro");
        JButton btnRevista = new JButton("Agregar Revista");
        JButton btnCD = new JButton("Agregar CD");
        JButton btnDVD = new JButton("Agregar DVD");

        btnLibro.addActionListener(e -> mostrarVistaFormulario("LIBRO"));
        btnRevista.addActionListener(e -> mostrarVistaFormulario("REVISTA"));
        btnCD.addActionListener(e -> mostrarVistaFormulario("CD"));
        btnDVD.addActionListener(e -> mostrarVistaFormulario("DVD"));

        panel.add(btnLibro);
        panel.add(btnRevista);
        panel.add(btnCD);
        panel.add(btnDVD);

        return panel;
    }

    private String generarCodigoAuto(String tipo) {
        String prefijo = tipo.equals("CD") ? "CDA" : tipo.substring(0, 3).toUpperCase();
        String tabla = "";

        if (tipo.equals("LIBRO")) tabla = "Libro";
        else if (tipo.equals("REVISTA")) tabla = "Revista";
        else if (tipo.equals("CD")) tabla = "cd";
        else if (tipo.equals("DVD")) tabla = "DVD";

        int maxNumero = 0;
        String sql = "SELECT codigo_interno FROM " + tabla + " WHERE codigo_interno LIKE '" + prefijo + "-%'";

        Conexion db = new Conexion();
        try (Connection cn = db.getConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String codigo = rs.getString("codigo_interno");
                String[] partes = codigo.split("-");
                if (partes.length == 2) {
                    try {
                        int num = Integer.parseInt(partes[1]);
                        if (num > maxNumero) {
                            maxNumero = num;
                        }
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception e) {}

        return String.format("%s-%04d", prefijo, maxNumero + 1);
    }

    private String adaptarFecha(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) return "2000-01-01";
        entrada = entrada.trim().replaceAll("/", "-");
        if (entrada.matches("\\d{4}")) return entrada + "-01-01";
        if (entrada.matches("\\d{1,2}-\\d{4}")) {
            String[] p = entrada.split("-");
            return p[1] + "-" + String.format("%02d", Integer.parseInt(p[0])) + "-01";
        }
        if (entrada.matches("\\d{4}-\\d{1,2}")) return entrada + "-01";
        if (entrada.matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
            String[] p = entrada.split("-");
            return p[2] + "-" + String.format("%02d", Integer.parseInt(p[1])) + "-" + String.format("%02d", Integer.parseInt(p[0]));
        }
        return entrada;
    }

    private void mostrarVistaFormulario(String tipo) {
        JPanel vistaForm = new JPanel(new BorderLayout());

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVolver = new JButton("<< Volver a selección de material");
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> navegadorAgregar.show(panelAgregarContenedor, "MENU_BOTONES"));
        panelTop.add(btnVolver);

        vistaForm.add(panelTop, BorderLayout.NORTH);

        JPanel cuerpoFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] etiquetas;
        if (tipo.equals("LIBRO")) {
            etiquetas = new String[]{"Código:", "Título:", "Autor:", "Páginas:", "Editorial:", "ISBN:", "Año:", "Unidades:"};
        } else if (tipo.equals("REVISTA")) {
            etiquetas = new String[]{"Código:", "Título:", "Editorial:", "Periodicidad:", "Fecha Pub (a-m-d):", "Unidades:"};
        } else if (tipo.equals("CD")) {
            etiquetas = new String[]{"Código:", "Título:", "Artista:", "Género:", "Duración (m:h:s):", "Canciones:", "Unidades:"};
        } else {
            etiquetas = new String[]{"Código:", "Título:", "Director:", "Duración (m:h:s):", "Género:", "Unidades:"};
        }

        JTextField[] campos = new JTextField[etiquetas.length];
        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            cuerpoFormulario.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);

            if (i == 0) {
                campos[i].setText(generarCodigoAuto(tipo));
                campos[i].setEditable(false);
                campos[i].setBackground(new Color(230, 230, 230));
            }
            cuerpoFormulario.add(campos[i], gbc);
        }

        JButton btnGuardar = new JButton("Guardar " + tipo);
        gbc.gridx = 0; gbc.gridy = etiquetas.length; gbc.gridwidth = 2;
        cuerpoFormulario.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            try {
                boolean exito = false;
                if (tipo.equals("LIBRO")) {
                    Libro l = new Libro(campos[0].getText(), campos[1].getText(), campos[2].getText(), Integer.parseInt(campos[3].getText()), campos[4].getText(), campos[5].getText(), Integer.parseInt(campos[6].getText()), Integer.parseInt(campos[7].getText()));
                    exito = new Crud_Libro().agregarLibro(l);
                } else if (tipo.equals("REVISTA")) {
                    String fechaFiltro = adaptarFecha(campos[4].getText());
                    Revista r = new Revista(campos[0].getText(), campos[1].getText(), campos[2].getText(), campos[3].getText(), fechaFiltro, Integer.parseInt(campos[5].getText()));
                    exito = new Crud_Revista().agregarRevista(r);
                } else if (tipo.equals("CD")) {
                    CDAudio c = new CDAudio(campos[0].getText(), campos[1].getText(), campos[2].getText(), campos[3].getText(), campos[4].getText(), Integer.parseInt(campos[5].getText()), Integer.parseInt(campos[6].getText()));
                    exito = new Crud_CDAudio().agregarCD(c);
                } else if (tipo.equals("DVD")) {
                    DVD d = new DVD(campos[0].getText(), campos[1].getText(), campos[2].getText(), campos[3].getText(), campos[4].getText(), Integer.parseInt(campos[5].getText()));
                    exito = new Crud_DVD().agregarDVD(d);
                }

                if (exito) {
                    JOptionPane.showMessageDialog(this, tipo + " guardado exitosamente.");
                    for (int i = 1; i < campos.length; i++) campos[i].setText("");
                    campos[0].setText(generarCodigoAuto(tipo));
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar. Revise la consola.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos. Verifique formatos numéricos.");
            }
        });

        vistaForm.add(new JScrollPane(cuerpoFormulario), BorderLayout.CENTER);
        panelAgregarContenedor.add(vistaForm, "FORM_" + tipo);
        navegadorAgregar.show(panelAgregarContenedor, "FORM_" + tipo);
    }

    private JPanel crearPanelModificarGlobal() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.add(new JLabel("Código del material (Ej: LIB-0001):"));
        JTextField txtCodigoBusqueda = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar para editar");
        panelBusqueda.add(txtCodigoBusqueda);
        panelBusqueda.add(btnBuscar);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(""));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] etiquetas = {"Título:", "Autor / Director / Artista:", "Editorial / Género:", "Páginas / Duración:", "ISBN / Periodicidad / Canciones:", "Año / Fecha Publicación:", "Unidades Disponibles:"};
        JTextField[] camposMod = new JTextField[7];
        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panelFormulario.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            camposMod[i] = new JTextField(25);
            panelFormulario.add(camposMod[i], gbc);
        }

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        JButton btnActualizar = new JButton("Actualizar Material");
        panelBoton.add(btnActualizar);
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        final Object[] materialEncontrado = {null};
        final String[] tipoActual = {""};

        btnBuscar.addActionListener(e -> {
            String cod = txtCodigoBusqueda.getText().trim().toUpperCase();
            boolean hallado = false;
            for (JTextField t : camposMod) { t.setText(""); t.setEnabled(true); }

            if (cod.startsWith("LIB")) {
                for (Libro l : new Crud_Libro().listarLibros()) {
                    if (l.getCodigoInterno().equals(cod)) {
                        camposMod[0].setText(l.getTitulo());
                        camposMod[1].setText(l.getAutor());
                        camposMod[2].setText(l.getEditorial());
                        camposMod[3].setText(String.valueOf(l.getNumPaginas()));
                        camposMod[4].setText(l.getIsbn());
                        camposMod[5].setText(String.valueOf(l.getAnioPublicacion()));
                        camposMod[6].setText(String.valueOf(l.getUnidadesDisponibles()));
                        materialEncontrado[0] = l; tipoActual[0] = "LIB"; hallado = true; break;
                    }
                }
            } else if (cod.startsWith("REV")) {
                for (Revista r : new Crud_Revista().listarRevistas()) {
                    if (r.getCodigoInterno().equals(cod)) {
                        camposMod[0].setText(r.getTitulo());
                        camposMod[1].setText("N/A"); camposMod[1].setEnabled(false);
                        camposMod[2].setText(r.getEditorial());
                        camposMod[3].setText("N/A"); camposMod[3].setEnabled(false);
                        camposMod[4].setText(r.getPeriodicidad());
                        camposMod[5].setText(r.getFechaPublicacion());
                        camposMod[6].setText(String.valueOf(r.getUnidadesDisponibles()));
                        materialEncontrado[0] = r; tipoActual[0] = "REV"; hallado = true; break;
                    }
                }
            } else if (cod.startsWith("CDA")) {
                for (CDAudio c : new Crud_CDAudio().listarCDs()) {
                    if (c.getCodigoInterno().equals(cod)) {
                        camposMod[0].setText(c.getTitulo());
                        camposMod[1].setText(c.getArtista());
                        camposMod[2].setText(c.getGenero());
                        camposMod[3].setText(c.getDuracion());
                        camposMod[4].setText(String.valueOf(c.getNumCanciones()));
                        camposMod[5].setText("N/A"); camposMod[5].setEnabled(false);
                        camposMod[6].setText(String.valueOf(c.getUnidadesDisponibles()));
                        materialEncontrado[0] = c; tipoActual[0] = "CDA"; hallado = true; break;
                    }
                }
            } else if (cod.startsWith("DVD")) {
                for (DVD d : new Crud_DVD().listarDVDs()) {
                    if (d.getCodigoInterno().equals(cod)) {
                        camposMod[0].setText(d.getTitulo());
                        camposMod[1].setText(d.getDirector());
                        camposMod[2].setText(d.getGenero());
                        camposMod[3].setText(d.getDuracion());
                        camposMod[4].setText("N/A"); camposMod[4].setEnabled(false);
                        camposMod[5].setText("N/A"); camposMod[5].setEnabled(false);
                        camposMod[6].setText(String.valueOf(d.getUnidadesDisponibles()));
                        materialEncontrado[0] = d; tipoActual[0] = "DVD"; hallado = true; break;
                    }
                }
            }

            if (!hallado) JOptionPane.showMessageDialog(this, "Material no encontrado. Revise el código (Ej: DVD-0001)");
        });

        btnActualizar.addActionListener(e -> {
            if (tipoActual[0].isEmpty() || materialEncontrado[0] == null) {
                JOptionPane.showMessageDialog(this, "Primero busque un material válido.");
                return;
            }
            try {
                boolean ok = false;
                if (tipoActual[0].equals("LIB")) {
                    Libro l = (Libro) materialEncontrado[0];
                    l.setTitulo(camposMod[0].getText()); l.setAutor(camposMod[1].getText()); l.setEditorial(camposMod[2].getText()); l.setNumPaginas(Integer.parseInt(camposMod[3].getText())); l.setIsbn(camposMod[4].getText()); l.setAnioPublicacion(Integer.parseInt(camposMod[5].getText())); l.setUnidadesDisponibles(Integer.parseInt(camposMod[6].getText()));
                    ok = new Crud_Libro().actualizarLibro(l);
                } else if (tipoActual[0].equals("REV")) {
                    Revista r = (Revista) materialEncontrado[0];
                    r.setTitulo(camposMod[0].getText()); r.setEditorial(camposMod[2].getText()); r.setPeriodicidad(camposMod[4].getText()); r.setFechaPublicacion(adaptarFecha(camposMod[5].getText())); r.setUnidadesDisponibles(Integer.parseInt(camposMod[6].getText()));
                    ok = new Crud_Revista().actualizarRevista(r);
                } else if (tipoActual[0].equals("CDA")) {
                    CDAudio c = (CDAudio) materialEncontrado[0];
                    c.setTitulo(camposMod[0].getText()); c.setArtista(camposMod[1].getText()); c.setGenero(camposMod[2].getText()); c.setDuracion(camposMod[3].getText()); c.setNumCanciones(Integer.parseInt(camposMod[4].getText())); c.setUnidadesDisponibles(Integer.parseInt(camposMod[6].getText()));
                    ok = new Crud_CDAudio().actualizarCD(c);
                } else if (tipoActual[0].equals("DVD")) {
                    DVD d = (DVD) materialEncontrado[0];
                    d.setTitulo(camposMod[0].getText()); d.setDirector(camposMod[1].getText()); d.setGenero(camposMod[2].getText()); d.setDuracion(camposMod[3].getText()); d.setUnidadesDisponibles(Integer.parseInt(camposMod[6].getText()));
                    ok = new Crud_DVD().actualizarDVD(d);
                }

                if (ok) JOptionPane.showMessageDialog(this, "¡Material actualizado con éxito!");
                else JOptionPane.showMessageDialog(this, "Error en BD al actualizar.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos numéricos. Revise.");
            }
        });

        return panelPrincipal;
    }

    private JPanel crearPanelBorrarGlobal() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));

        JLabel lblTitulo = new JLabel("Código del Material:");
        panel.add(lblTitulo);

        JTextField txtCod = new JTextField(15);
        panel.add(txtCod);

        JButton btnDel = new JButton("Eliminar Material");
        btnDel.setFocusPainted(false);
        btnDel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(btnDel);

        btnDel.addActionListener(e -> {
            String codigo = txtCod.getText().trim();

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un código primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int r = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea borrar permanentemente el material " + codigo + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (r == JOptionPane.YES_OPTION) {
                if (new Crud_Libro().eliminarLibro(codigo)) {
                    JOptionPane.showMessageDialog(this, "Material eliminado con éxito.", "Hecho", JOptionPane.INFORMATION_MESSAGE);
                    txtCod.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar. Verifique que el código exista.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return panel;
    }

    private JPanel crearPanelDisponiblesGlobal() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltro.add(new JLabel("Buscar:"));
        JTextField txtFiltro = new JTextField(30);
        panelFiltro.add(txtFiltro);
        JButton btnCarga = new JButton("Actualizar Inventario");
        panelFiltro.add(btnCarga);
        String[] col = {"Código", "Título", "Autor/Director", "Tipo de Material", "Disponibles"};
        DefaultTableModel modelo = new DefaultTableModel(col, 0);
        JTable tabla = new JTable(modelo);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
            private void filtrar() {
                String texto = txtFiltro.getText();
                if (texto.trim().length() == 0) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0, 1, 2, 3));
            }
        });
        btnCarga.addActionListener(e -> {
            modelo.setRowCount(0);
            new Crud_Libro().listarLibros().forEach(l -> modelo.addRow(new Object[]{l.getCodigoInterno(), l.getTitulo(), l.getAutor(), "Libro", l.getUnidadesDisponibles()}));
            new Crud_Revista().listarRevistas().forEach(r -> modelo.addRow(new Object[]{r.getCodigoInterno(), r.getTitulo(), "N/A", "Revista", r.getUnidadesDisponibles()}));
            new Crud_CDAudio().listarCDs().forEach(c -> modelo.addRow(new Object[]{c.getCodigoInterno(), c.getTitulo(), c.getArtista(), "CD Audio", c.getUnidadesDisponibles()}));
            new Crud_DVD().listarDVDs().forEach(d -> modelo.addRow(new Object[]{d.getCodigoInterno(), d.getTitulo(), d.getDirector(), "DVD", d.getUnidadesDisponibles()}));
        });
        panel.add(panelFiltro, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }
}