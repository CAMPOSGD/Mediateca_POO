import gui.MenuPrincipal;

public class Principal {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            MenuPrincipal ventana = new MenuPrincipal();
            ventana.setVisible(true);
        });
    }
}