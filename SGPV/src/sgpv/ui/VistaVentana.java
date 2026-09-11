package sgpv.ui;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * Presentacion y entrada mediante ventanas reales de swing.
 * Cada mensaje, lista, o dato pedido al usuario aparece en una ventana emergente
 * 
 */
public class VistaVentana implements Vista {

    private String tituloActual = "Sistema de Gestión de Programas de Voluntariado";

    @Override
    public void mostrarTitulo(String titulo) {
        this.tituloActual = titulo;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, tituloActual, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, tituloActual, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void mostrarLista(List<String> lineas) {
        StringBuilder sb = new StringBuilder();
        if (lineas == null || lineas.isEmpty()) {
            sb.append("(sin elementos para mostrar)");
        } else {
            for (int i = 0; i < lineas.size(); i++) {
                sb.append(i + 1).append(". ").append(lineas.get(i)).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), tituloActual, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int pedirOpcion(List<String> opciones) {
        Object[] valores = opciones.toArray();
        Object seleccion = JOptionPane.showInputDialog(null, "Seleccione una opción:", tituloActual,
                JOptionPane.QUESTION_MESSAGE, null, valores, valores.length > 0 ? valores[0] : null);
        if (seleccion == null) {
            return -1;
        }
        return opciones.indexOf(seleccion.toString());
    }

    @Override
    public String pedirTexto(String etiqueta) {
        String respuesta = JOptionPane.showInputDialog(null, etiqueta, tituloActual, JOptionPane.QUESTION_MESSAGE);
        return respuesta == null ? "" : respuesta.trim();
    }

    @Override
    public int pedirEntero(String etiqueta, int min, int max) {
        while (true) {
            String respuesta = JOptionPane.showInputDialog(null,
                    etiqueta + " (" + min + " a " + max + ")", tituloActual, JOptionPane.QUESTION_MESSAGE);
            if (respuesta == null) {
                continue; // el usuario canceló; para un dato obligatorio, se vuelve a pedir
            }
            try {
                int valor = Integer.parseInt(respuesta.trim());
                if (valor < min || valor > max) {
                    JOptionPane.showMessageDialog(null, "Debe estar entre " + min + " y " + max + ".",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un número válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public boolean pedirBooleano(String etiqueta) {
        while (true) {
            int respuesta = JOptionPane.showConfirmDialog(null, etiqueta, tituloActual, JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) return true;
            if (respuesta == JOptionPane.NO_OPTION) return false;
            // si cierra la ventana sin elegir, se vuelve a preguntar
        }
    }
}
