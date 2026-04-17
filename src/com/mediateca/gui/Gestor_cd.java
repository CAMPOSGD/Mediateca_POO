package gui;

import com.mediateca.bd.Crud_CDAudio;
import mediateca.modelos.CDAudio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Gestor_cd extends JFrame {

    private JTabbedPane pestanas;

    public Gestor_cd() {
        setTitle("Gestión de CDs - Mediateca UDB");
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

        String[] etiquetas = {"Código:", "Título:", "Artista:", "Género:", "Duración:", "Canciones:", "Unidades:"};
        JTextField[] campos = new JTextField[7];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btnGuardar = new JButton("Guardar CD");
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            try {
                CDAudio nuevo = new CDAudio(campos[0].getText(), campos[1].getText(), campos[2].getText(),
                        campos[3].getText(), campos[4].getText(), Integer.parseInt(campos[5].getText()),
                        Integer.parseInt(campos[6].getText()));
                if (new Crud_CDAudio().agregarCD(nuevo)) {
                    JOptionPane.showMessageDialog(this, "CD guardado");
                    for (JTextField f : campos) f.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en datos.");
            }
        });
        return panel;
    }

    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] etiquetas = {"Código:", "Título:", "Artista:", "Género:", "Duración:", "Canciones:", "Unidades:"};
        JTextField[] campos = new JTextField[7];

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            panel.add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            panel.add(campos[i], gbc);
        }

        JButton btn = new JButton("Actualizar CD");
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(btn, gbc);

        btn.addActionListener(e -> {
            try {
                CDAudio mod = new CDAudio(campos[0].getText(), campos[1].getText(), campos[2].getText(),
                        campos[3].getText(), campos[4].getText(), Integer.parseInt(campos[5].getText()),
                        Integer.parseInt(campos[6].getText()));
                if (new Crud_CDAudio().actualizarCD(mod)) {
                    JOptionPane.showMessageDialog(this, "CD actualizado");
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
            if (new Crud_CDAudio().eliminarCD(txt.getText())) {
                JOptionPane.showMessageDialog(this, "Eliminado");
            }
        });
        return panel;
    }

    private JPanel crearPanelDisponibles() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] col = {"Código", "Título", "Artista", "Género", "Duración", "Canciones", "Unidades"};
        DefaultTableModel modelo = new DefaultTableModel(col, 0);
        JTable tabla = new JTable(modelo);
        JButton btn = new JButton("Refrescar");
        btn.addActionListener(e -> {
            modelo.setRowCount(0);
            List<CDAudio> lista = new Crud_CDAudio().listarCDs();
            for (CDAudio c : lista) {
                modelo.addRow(new Object[]{c.getCodigoInterno(), c.getTitulo(), c.getArtista(), c.getGenero(), c.getDuracion(), c.getNumCanciones(), c.getUnidadesDisponibles()});
            }
        });
        panel.add(btn, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }
}