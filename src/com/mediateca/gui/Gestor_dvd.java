package gui;

import com.mediateca.bd.Crud_DVD;
import mediateca.modelos.DVD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Gestor_dvd extends JFrame {

    private JTabbedPane pestanas;

    public Gestor_dvd() {
        setTitle("Gestión de DVDs - Mediateca UDB");
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

        String[] etiquetas = {"Código:", "Título:", "Director:", "Duración:", "Género:", "Unidades:"};
        JTextField[] campos = new JTextField[6];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btnGuardar = new JButton("Guardar DVD");
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            try {
                Crud_DVD crud = new Crud_DVD();
                crud.insertarDVD(
                        campos[0].getText(),
                        campos[1].getText(),
                        campos[2].getText(),
                        campos[3].getText(),
                        campos[4].getText(),
                        Integer.parseInt(campos[5].getText())
                );
                JOptionPane.showMessageDialog(this, "DVD guardado con éxito");
                for (JTextField f : campos) f.setText("");
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

        String[] etiquetas = {"Código:", "Título:", "Director:", "Duración:", "Género:", "Unidades:"};
        JTextField[] campos = new JTextField[6];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btn = new JButton("Actualizar DVD");
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(btn, gbc);

        btn.addActionListener(e -> {
            try {
                DVD mod = new DVD(
                        campos[0].getText(),
                        campos[1].getText(),
                        campos[2].getText(),
                        campos[3].getText(),
                        campos[4].getText(),
                        Integer.parseInt(campos[5].getText())
                );
                if (new Crud_DVD().actualizarDVD(mod)) {
                    JOptionPane.showMessageDialog(this, "DVD actualizado");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error.");
            }
        });
        return panel;
    }

    private JPanel crearPanelBorrar() {
        JPanel panel = new JPanel(new FlowLayout());
        JTextField txt = new JTextField(15);
        JButton btn = new JButton("Eliminar");
        panel.add(new JLabel("Código:"));
        panel.add(txt);
        panel.add(btn);
        btn.addActionListener(e -> {
            if (new Crud_DVD().eliminarDVD(txt.getText())) {
                JOptionPane.showMessageDialog(this, "Eliminado");
            }
        });
        return panel;
    }

    private JPanel crearPanelDisponibles() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] col = {"Código", "Título", "Director", "Duración", "Género", "Unidades"};
        DefaultTableModel modelo = new DefaultTableModel(col, 0);
        JTable tabla = new JTable(modelo);
        JButton btn = new JButton("Refrescar");
        btn.addActionListener(e -> {
            modelo.setRowCount(0);
            List<DVD> lista = new Crud_DVD().listarDVDs();
            for (DVD d : lista) {
                modelo.addRow(new Object[]{d.getCodigoInterno(), d.getTitulo(), d.getDirector(), d.getDuracion(), d.getGenero(), d.getUnidadesDisponibles()});
            }
        });
        panel.add(btn, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }
}