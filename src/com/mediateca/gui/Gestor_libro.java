package gui;

import com.mediateca.bd.Crud_Libro;
import mediateca.modelos.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Gestor_libro extends JFrame {

    private JTabbedPane pestanas;

    public Gestor_libro() {
        setTitle("Gestión de Libros - Mediateca UDB");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        pestanas = new JTabbedPane();

        pestanas.addTab("Agregar Material", crearPanelAgregar());
        pestanas.addTab("Modificar Material", crearPanelModificar());
        pestanas.addTab("Borrar Material", crearPanelBorrar());
        pestanas.addTab("Materiales Disponibles", crearPanelDisponibles());

        add(pestanas);
    }

    private JPanel crearPanelAgregar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] etiquetas = {"Código:", "Título:", "Autor:", "Páginas:", "Editorial:", "ISBN:", "Año:", "Unidades:"};
        JTextField[] campos = new JTextField[8];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btnGuardar = new JButton("Guardar Libro");
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            try {
                Libro nuevo = new Libro(campos[0].getText(), campos[1].getText(), campos[2].getText(),
                        Integer.parseInt(campos[3].getText()), campos[4].getText(), campos[5].getText(),
                        Integer.parseInt(campos[6].getText()), Integer.parseInt(campos[7].getText()));
                Crud_Libro crud = new Crud_Libro();
                if (crud.agregarLibro(nuevo)) {
                    JOptionPane.showMessageDialog(this, "Libro guardado con éxito");
                    for (JTextField f : campos) f.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar en la Base de Datos");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Revisa que los números sean correctos.");
            }
        });
        return panel;
    }

    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] etiquetas = {"Código a Modificar:", "Nuevo Título:", "Nuevo Autor:", "Nuevas Páginas:", "Nueva Editorial:", "Nuevo ISBN:", "Nuevo Año:", "Nuevas Unidades:"};
        JTextField[] campos = new JTextField[8];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btnActualizar = new JButton("Actualizar Libro");
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(btnActualizar, gbc);

        btnActualizar.addActionListener(e -> {
            try {
                Libro modificado = new Libro(campos[0].getText(), campos[1].getText(), campos[2].getText(),
                        Integer.parseInt(campos[3].getText()), campos[4].getText(), campos[5].getText(),
                        Integer.parseInt(campos[6].getText()), Integer.parseInt(campos[7].getText()));
                Crud_Libro crud = new Crud_Libro();
                if (crud.actualizarLibro(modificado)) {
                    JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.");
                    for (JTextField f : campos) f.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún libro con ese código.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Revisa los datos ingresados.");
            }
        });
        return panel;
    }

    private JPanel crearPanelBorrar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.add(new JLabel("Ingrese el Código del Libro a eliminar:"));
        JTextField txtCodigo = new JTextField(15);
        panel.add(txtCodigo);

        JButton btnBorrar = new JButton("Eliminar Libro");
        btnBorrar.setBackground(new Color(231, 76, 60));
        btnBorrar.setForeground(Color.WHITE);
        panel.add(btnBorrar);

        btnBorrar.addActionListener(e -> {
            String codigo = txtCodigo.getText();
            if(codigo.isEmpty()){
                JOptionPane.showMessageDialog(this, "Debe ingresar un código.");
                return;
            }
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el libro " + codigo + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                Crud_Libro crud = new Crud_Libro();
                if (crud.eliminarLibro(codigo)) {
                    JOptionPane.showMessageDialog(this, "Libro eliminado.");
                    txtCodigo.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar. Verifique el código.");
                }
            }
        });
        return panel;
    }

    private JPanel crearPanelDisponibles() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = {"Código", "Título", "Autor", "Páginas", "Editorial", "ISBN", "Año", "Unidades"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JButton btnRefrescar = new JButton("Cargar / Actualizar Lista de Libros");
        btnRefrescar.setBackground(new Color(46, 204, 113));
        btnRefrescar.setForeground(Color.WHITE);

        btnRefrescar.addActionListener(e -> {
            modelo.setRowCount(0);
            Crud_Libro crud = new Crud_Libro();
            List<Libro> libros = crud.listarLibros();
            for (Libro l : libros) {
                modelo.addRow(new Object[]{
                        l.getCodigoInterno(), l.getTitulo(), l.getAutor(), l.getNumPaginas(),
                        l.getEditorial(), l.getIsbn(), l.getAnioPublicacion(), l.getUnidadesDisponibles()
                });
            }
        });

        panel.add(btnRefrescar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}