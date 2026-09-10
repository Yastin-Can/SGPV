package sgpv.ui;

import java.util.List;
import java.util.Scanner;

/** Presentación y entrada por consola */
public class VistaConsola implements Vista {

    private final Scanner sc = new Scanner(System.in);

    @Override
    public void mostrarTitulo(String titulo) {
        System.out.println();
        System.out.println("== " + titulo + " ==");
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    @Override
    public void mostrarLista(List<String> lineas) {
        if (lineas == null || lineas.isEmpty()) {
            System.out.println("(sin elementos para mostrar)");
            return;
        }
        for (int i = 0; i < lineas.size(); i++) {
            System.out.println((i + 1) + ". " + lineas.get(i));
        }
    }

    @Override
    public int pedirOpcion(List<String> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + ". " + opciones.get(i));
        }
        System.out.println("0. Volver/Salir");
        while (true) {
            System.out.print("Seleccione una opción: ");
            String linea = sc.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor == 0) {
                    return -1;
                }
                if (valor >= 1 && valor <= opciones.size()) {
                    return valor - 1;
                }
            } catch (NumberFormatException ignored) {
                // sigue el bucle y vuelve a pedir
            }
            System.out.println("[ERROR] Opción inválida.");
        }
    }

    @Override
    public String pedirTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return sc.nextLine().trim();
    }

    @Override
    public int pedirEntero(String etiqueta, int min, int max) {
        while (true) {
            System.out.print(etiqueta + " (" + min + " a " + max + "): ");
            try {
                int valor = Integer.parseInt(sc.nextLine().trim());
                if (valor < min || valor > max) {
                    System.out.println("[ERROR] Ingrese un número entre " + min + " y " + max + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Debe ingresar un número válido.");
            }
        }
    }

    @Override
    public boolean pedirBooleano(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + " (s/n): ");
            String linea = sc.nextLine().trim().toLowerCase();
            if (linea.equals("s") || linea.equals("si")) return true;
            if (linea.equals("n") || linea.equals("no")) return false;
            System.out.println("[ERROR] Responda 's' o 'n'.");
        }
    }
}
