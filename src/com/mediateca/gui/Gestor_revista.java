package gui;

import com.mediateca.bd.Crud_Revista;
import mediateca.modelos.Revista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Gestor_revista extends JFrame {

    private JTabbedPane pestanas;

    public Gestor_revista() {
        setTitle("Gestión de Revistas - Mediateca UDB");
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

        String[] etiquetas = {"Código:", "Título:", "Editorial:", "Periodicidad:", "Fecha Pub:", "Unidades:"};
        JTextField[] campos = new JTextField[6];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btnGuardar = new JButton("Guardar Revista");
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            try {
                Revista nueva = new Revista(campos[0].getText(), campos[1].getText(), campos[2].getText(),
                        campos[3].getText(), campos[4].getText(), Integer.parseInt(campos[5].getText()));
                Crud_Revista crud = new Crud_Revista();
                if (crud.agregarRevista(nueva)) {
                    JOptionPane.showMessageDialog(this, "Revista guardada con éxito");
                    for (JTextField f : campos) f.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos.");
            }
        });
        return panel;
    }

    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] etiquetas = {"Código a Modificar:", "Nuevo Título:", "Nueva Editorial:", "Nueva Periodicidad:", "Nueva Fecha:", "Nuevas Unidades:"};
        JTextField[] campos = new JTextField[6];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btnActualizar = new JButton("Actualizar Revista");
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(btnActualizar, gbc);

        btnActualizar.addActionListener(e -> {
            try {
                Revista mod = new Revista(campos[0].getText(), campos[1].getText(), campos[2].getText(),
                        campos[3].getText(), campos[4].getText(), Integer.parseInt(campos[5].getText()));
                Crud_Revista crud = new Crud_Revista();
                if (crud.actualizarRevista(mod)) {
                    JOptionPane.showMessageDialog(this, "Revista actualizada.");
                    for (JTextField f : campos) f.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        });
        return panel;
    }

    private JPanel crearPanelBorrar() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Código:"));
        JTextField txtCodigo = new JTextField(15);
        panel.add(txtCodigo);
        JButton btn = new JButton("Eliminar");
        panel.add(btn);

        btn.addActionListener(e -> {
            Crud_Revista crud = new Crud_Revista();
            if (crud.eliminarRevista(txtCodigo.getText())) {
                JOptionPane.showMessageDialog(this, "Eliminado.");
                txtCodigo.setText("");
            }
        });
        return panel;
    }

    private JPanel crearPanelDisponibles() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] col = {"Código", "Título", "Editorial", "Periodicidad", "Fecha", "Unidades"};
        DefaultTableModel modelo = new DefaultTableModel(col, 0);
        JTable tabla = new JTable(modelo);
        JButton btn = new JButton("Refrescar");
        btn.addActionListener(e -> {
            modelo.setRowCount(0);
            List<Revista> lista = new Crud_Revista().listarRevistas();
            for (Revista r : lista) {
                modelo.addRow(new Object[]{r.getCodigoInterno(), r.getTitulo(), r.getEditorial(), r.getPeriodicidad(), r.getFechaPublicacion(), r.getUnidadesDisponibles()});
            }
        });
        panel.add(btn, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }
}