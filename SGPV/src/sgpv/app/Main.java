package sgpv.app;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import sgpv.modelo.Evento;
import sgpv.excepciones.CupoLlenoException;
import sgpv.excepciones.VoluntarioNoDisponibleException;
import sgpv.modelo.Habilidad;
import sgpv.modelo.GestorVoluntariado;
import sgpv.modelo.Programa;
import sgpv.persistencia.PersistenciaExcel;
import sgpv.ui.Vista;
import sgpv.ui.VistaConsola;
import sgpv.ui.VistaVentana;
import sgpv.utils.StringUtils;
import sgpv.modelo.Voluntario;

/**
 * Base del menu, q da eleccion para consola o ventana, con swing. Para hacer la gestion de programas, eventos y voluntarios.
 */
public class Main {

    private static final String RUTA_EXCEL = "sgpv_datos.xlsx";
    private static final List<String> PRIORIDADES = Arrays.asList("BAJA", "NORMAL", "ALTA", "CATASTROFE");

    public static void main(String[] args) {
        Vista vista = elegirModo();
        GestorVoluntariado gestor = cargarEstadoInicial(vista);

        boolean salir = false;
        while (!salir) {
            vista.mostrarTitulo("Sistema de Gestión de Programas de Voluntariado");
            int opcion = vista.pedirOpcion(Arrays.asList(
                    "Gestión de Programas",
                    "Gestión de Eventos",
                    "Gestión de Voluntarios (registro)",
                    "Asignar voluntarios disponibles a un evento"
            ));

            switch (opcion) {
                case 0:
                    menuProgramas(vista, gestor);
                    break;
                case 1:
                    menuEventos(vista, gestor);
                    break;
                case 2:
                    menuVoluntarios(vista, gestor);
                    break;
                case 3:
                    asignacionEspecial(vista, gestor);
                    break;
                case -1:
                    salir = true;
                    break;
                default:
                    vista.mostrarError("Opción inválida.");
            }
        }
        guardarEstadoFinal(vista, gestor);
        vista.mostrarMensaje("Sistema finalizado. ¡Hasta pronto!");
    }

    // seleccion de modo

    private static Vista elegirModo() {
        Object[] opciones = {"Consola", "Ventana"};
        int seleccion = JOptionPane.showOptionDialog(null, "¿Cómo desea usar el sistema?",
                "Modo de uso", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);
        return seleccion == 1 ? new VistaVentana() : new VistaConsola();
    }

    // Carga del excel.

    private static GestorVoluntariado cargarEstadoInicial(Vista vista) {
        if (PersistenciaExcel.existeArchivo(RUTA_EXCEL)) {
            try {
                GestorVoluntariado gestor = PersistenciaExcel.cargar(RUTA_EXCEL);
                vista.mostrarMensaje("Datos cargados desde " + RUTA_EXCEL + ".");
                return gestor;
            } catch (IOException e) {
                vista.mostrarError("No se pudo leer " + RUTA_EXCEL + ": " + e.getMessage()
                        + ". Se cargarán datos iniciales.");
            }
        }
        GestorVoluntariado gestor = new GestorVoluntariado();
        cargarDatosIniciales(gestor);
        return gestor;
    }

    private static void guardarEstadoFinal(Vista vista, GestorVoluntariado gestor) {
        try {
            PersistenciaExcel.guardar(gestor, RUTA_EXCEL);
            vista.mostrarMensaje("Datos guardados en " + RUTA_EXCEL + ".");
        } catch (IOException e) {
            vista.mostrarError("No se pudieron guardar los datos: " + e.getMessage());
        }
    }

    // Menu de gestion de programas.

    private static void menuProgramas(Vista vista, GestorVoluntariado gestor) {
        boolean volver = false;
        while (!volver) {
            vista.mostrarTitulo("Gestión de Programas");
            int opcion = vista.pedirOpcion(Arrays.asList(
                    "Agregar programa", "Listar programas", "Editar programa",
                    "Eliminar programa", "Buscar programa"
            ));

            switch (opcion) {
                case 0:
                    agregarPrograma(vista, gestor);
                    break;
                case 1:
                    listarProgramas(vista, gestor);
                    break;
                case 2:
                    editarPrograma(vista, gestor);
                    break;
                case 3:
                    eliminarPrograma(vista, gestor);
                    break;
                case 4:
                    buscarPrograma(vista, gestor);
                    break;
                case -1:
                    volver = true;
                    break;
                default:
                    vista.mostrarError("Opción inválida.");
            }
        }
    }

    private static void agregarPrograma(Vista vista, GestorVoluntariado gestor) {
        String id = vista.pedirTexto("ID del programa");
        String titulo = vista.pedirTexto("Título");
        String descripcion = vista.pedirTexto("Descripción");
        String fecha = vista.pedirTexto("Fecha de inicio (DD-MM-AAAA)");

        boolean ok = gestor.agregarPrograma(id, titulo, descripcion, fecha);
        if (ok) {
            vista.mostrarMensaje("Programa agregado correctamente.");
        } else {
            vista.mostrarError("No se pudo agregar el programa (ID repetido o datos inválidos).");
        }
    }

    private static void listarProgramas(Vista vista, GestorVoluntariado gestor) {
        List<String> lineas = new ArrayList<>();
        for (Programa p : gestor.listarProgramas()) {
            lineas.add(p.toString());
        }
        vista.mostrarTitulo("Listado de Programas");
        vista.mostrarLista(lineas);
    }

    private static void editarPrograma(Vista vista, GestorVoluntariado gestor) {
        String id = vista.pedirTexto("ID del programa a editar");
        if (gestor.buscarPrograma(id) == null) {
            vista.mostrarError("No existe un programa con ese ID.");
            return;
        }
        String titulo = vista.pedirTexto("Nuevo título");
        String descripcion = vista.pedirTexto("Nueva descripción");
        String fecha = vista.pedirTexto("Nueva fecha de inicio");

        boolean ok = gestor.editarPrograma(id, titulo, descripcion, fecha);
        vista.mostrarMensaje(ok ? "Programa actualizado." : "No se pudo actualizar el programa.");
    }

    private static void eliminarPrograma(Vista vista, GestorVoluntariado gestor) {
        String id = vista.pedirTexto("ID del programa a eliminar");
        boolean ok = gestor.eliminarPrograma(id);
        vista.mostrarMensaje(ok ? "Programa eliminado." : "No existe un programa con ese ID.");
    }

    private static void buscarPrograma(Vista vista, GestorVoluntariado gestor) {
        String id = vista.pedirTexto("ID del programa a buscar");
        Programa p = gestor.buscarPrograma(id);
        if (p == null) {
            vista.mostrarError("No existe un programa con ese ID.");
        } else {
            vista.mostrarMensaje(p.toString());
        }
    }

    // Menu de eventos

    private static void menuEventos(Vista vista, GestorVoluntariado gestor) {
        boolean volver = false;
        while (!volver) {
            vista.mostrarTitulo("Gestión de Eventos");
            int opcion = vista.pedirOpcion(Arrays.asList(
                    "Agregar evento a un programa", "Listar eventos de un programa",
                    "Editar evento", "Eliminar evento", "Buscar evento"
            ));

            switch (opcion) {
                case 0:
                    agregarEvento(vista, gestor);
                    break;
                case 1:
                    listarEventos(vista, gestor);
                    break;
                case 2:
                    editarEvento(vista, gestor);
                    break;
                case 3:
                    eliminarEvento(vista, gestor);
                    break;
                case 4:
                    buscarEvento(vista, gestor);
                    break;
                case -1:
                    volver = true;
                    break;
                default:
                    vista.mostrarError("Opción inválida.");
            }
        }
    }

    private static void agregarEvento(Vista vista, GestorVoluntariado gestor) {
        String idPrograma = vista.pedirTexto("ID del programa al que pertenece el evento");
        Programa programa = gestor.buscarPrograma(idPrograma);
        if (programa == null) {
            vista.mostrarError("No existe un programa con ese ID.");
            return;
        }

        String idEvento = vista.pedirTexto("ID del evento");
        String nombre = vista.pedirTexto("Nombre");
        String lugar = vista.pedirTexto("Lugar");
        String fecha = vista.pedirTexto("Fecha (DD-MM-AAAA)");
        int cupos = vista.pedirEntero("Cupos", 0, Integer.MAX_VALUE);
        String prioridad = pedirPrioridad(vista);

        boolean ok = programa.agregarEvento(idEvento, nombre, lugar, fecha, cupos, prioridad);
        vista.mostrarMensaje(ok ? "Evento agregado correctamente."
                : "No se pudo agregar el evento (ID repetido o datos inválidos).");
    }

    private static void listarEventos(Vista vista, GestorVoluntariado gestor) {
        String idPrograma = vista.pedirTexto("ID del programa");
        Programa programa = gestor.buscarPrograma(idPrograma);
        if (programa == null) {
            vista.mostrarError("No existe un programa con ese ID.");
            return;
        }
        List<String> lineas = new ArrayList<>();
        for (Evento e : programa.getEventos().values()) {
            lineas.add(e.toString());
        }
        vista.mostrarTitulo("Eventos del programa " + idPrograma);
        vista.mostrarLista(lineas);
    }

    private static void editarEvento(Vista vista, GestorVoluntariado gestor) {
        String idPrograma = vista.pedirTexto("ID del programa");
        Programa programa = gestor.buscarPrograma(idPrograma);
        if (programa == null) {
            vista.mostrarError("No existe un programa con ese ID.");
            return;
        }
        String idEvento = vista.pedirTexto("ID del evento a editar");
        Evento evento = programa.getEvento(idEvento);
        if (evento == null) {
            vista.mostrarError("No existe un evento con ese ID en ese programa.");
            return;
        }

        evento.setNombre(vista.pedirTexto("Nuevo nombre"));
        evento.setLugar(vista.pedirTexto("Nuevo lugar"));
        evento.setFecha(vista.pedirTexto("Nueva fecha"));
        evento.setCupos(vista.pedirEntero("Nuevos cupos", 0, Integer.MAX_VALUE));
        evento.setPrioridad(sgpv.modelo.Prioridad.valueOf(pedirPrioridad(vista)));

        vista.mostrarMensaje("Evento actualizado.");
    }

    private static void eliminarEvento(Vista vista, GestorVoluntariado gestor) {
        String idPrograma = vista.pedirTexto("ID del programa");
        Programa programa = gestor.buscarPrograma(idPrograma);
        if (programa == null) {
            vista.mostrarError("No existe un programa con ese ID.");
            return;
        }
        String idEvento = vista.pedirTexto("ID del evento a eliminar");
        boolean ok = programa.eliminarEvento(idEvento);
        vista.mostrarMensaje(ok ? "Evento eliminado." : "No existe un evento con ese ID en ese programa.");
    }

    private static void buscarEvento(Vista vista, GestorVoluntariado gestor) {
        String idPrograma = vista.pedirTexto("ID del programa");
        Programa programa = gestor.buscarPrograma(idPrograma);
        if (programa == null) {
            vista.mostrarError("No existe un programa con ese ID.");
            return;
        }
        String idEvento = vista.pedirTexto("ID del evento a buscar");
        Evento evento = programa.getEvento(idEvento);
        if (evento == null) {
            vista.mostrarError("No existe un evento con ese ID en ese programa.");
        } else {
            vista.mostrarMensaje(evento.toString());
        }
    }

    // Menu de voluntarios.

    private static void menuVoluntarios(Vista vista, GestorVoluntariado gestor) {
        boolean volver = false;
        while (!volver) {
            vista.mostrarTitulo("Gestión de Voluntarios");
            int opcion = vista.pedirOpcion(Arrays.asList(
                    "Registrar voluntario", "Listar voluntarios", "Editar voluntario",
                    "Eliminar voluntario", "Buscar voluntario"
            ));

            switch (opcion) {
                case 0:
                    registrarVoluntario(vista, gestor);
                    break;
                case 1:
                    listarVoluntarios(vista, gestor);
                    break;
                case 2:
                    editarVoluntario(vista, gestor);
                    break;
                case 3:
                    eliminarVoluntario(vista, gestor);
                    break;
                case 4:
                    buscarVoluntario(vista, gestor);
                    break;
                case -1:
                    volver = true;
                    break;
                default:
                    vista.mostrarError("Opción inválida.");
            }
        }
    }

    private static void registrarVoluntario(Vista vista, GestorVoluntariado gestor) {
        String nombre = vista.pedirTexto("Nombre");
        String rut = vista.pedirTexto("RUT");
        String comuna = vista.pedirTexto("Comuna");
        boolean disponible = vista.pedirBooleano("¿Está disponible?");

        boolean ok = gestor.registrarVoluntario(nombre, rut, comuna, disponible);
        if (!ok) {
            vista.mostrarError("No se pudo registrar (RUT repetido o datos inválidos).");
            return;
        }
        Voluntario v = gestor.buscarVoluntario(rut);
        String habilidad = vista.pedirTexto("Habilidad principal (déjelo vacío si no aplica)");
        if (!StringUtils.isBlank(habilidad)) {
            v.agregarHabilidad(new Habilidad(habilidad));
        }
        vista.mostrarMensaje("Voluntario registrado correctamente.");
    }

    private static void listarVoluntarios(Vista vista, GestorVoluntariado gestor) {
        List<String> lineas = new ArrayList<>();
        for (Voluntario v : gestor.listarVoluntarios()) {
            lineas.add(v.toString());
        }
        vista.mostrarTitulo("Listado de Voluntarios");
        vista.mostrarLista(lineas);
    }

    private static void editarVoluntario(Vista vista, GestorVoluntariado gestor) {
        String rut = vista.pedirTexto("RUT del voluntario a editar");
        if (gestor.buscarVoluntario(rut) == null) {
            vista.mostrarError("No existe un voluntario con ese RUT.");
            return;
        }
        String nombre = vista.pedirTexto("Nuevo nombre");
        String comuna = vista.pedirTexto("Nueva comuna");
        boolean disponible = vista.pedirBooleano("¿Está disponible?");

        boolean ok = gestor.editarVoluntario(rut, nombre, comuna, disponible);
        vista.mostrarMensaje(ok ? "Voluntario actualizado." : "No se pudo actualizar.");
    }

    private static void eliminarVoluntario(Vista vista, GestorVoluntariado gestor) {
        String rut = vista.pedirTexto("RUT del voluntario a eliminar");
        boolean ok = gestor.eliminarVoluntario(rut);
        vista.mostrarMensaje(ok ? "Voluntario eliminado." : "No existe un voluntario con ese RUT.");
    }

    private static void buscarVoluntario(Vista vista, GestorVoluntariado gestor) {
        String rut = vista.pedirTexto("RUT del voluntario a buscar");
        Voluntario v = gestor.buscarVoluntario(rut);
        if (v == null) {
            vista.mostrarError("No existe un voluntario con ese RUT.");
        } else {
            vista.mostrarMensaje(v.toString());
        }
    }

    // Asignacion de voluntariados por habilidad.

    private static void asignacionEspecial(Vista vista, GestorVoluntariado gestor) {
        vista.mostrarTitulo("Asignar voluntarios disponibles a un evento");
        String idPrograma = vista.pedirTexto("ID del programa");
        String idEvento = vista.pedirTexto("ID del evento");
        String habilidad = vista.pedirTexto("Habilidad requerida");

        try {
            List<Voluntario> asignados = gestor.asignarVoluntariosDisponiblesAEvento(idPrograma, idEvento, habilidad);
            if (asignados.isEmpty()) {
                vista.mostrarMensaje("No se asignó ningún voluntario (sin candidatos disponibles o evento inexistente).");
            } else {
                List<String> lineas = new ArrayList<>();
                for (Voluntario v : asignados) {
                    lineas.add(v.getNombre() + " (" + v.getRut() + ")");
                }
                vista.mostrarMensaje("Voluntarios asignados:");
                vista.mostrarLista(lineas);
            }
        } catch (CupoLlenoException | VoluntarioNoDisponibleException e) {
            vista.mostrarError(e.getMessage());
        }
    }


    private static String pedirPrioridad(Vista vista) {
        while (true) {
            int idx = vista.pedirOpcion(PRIORIDADES);
            if (idx != -1) {
                return PRIORIDADES.get(idx);
            }
            vista.mostrarError("Debe elegir una prioridad.");
        }
    }

    // Carga de datos iniciales, para comprobar la persistencia.

    private static void cargarDatosIniciales(GestorVoluntariado gestor) {
        gestor.agregarPrograma("P1", "Reforestación Costera", "Programa de plantación de árboles", "01-03-2026");
        Programa p1 = gestor.buscarPrograma("P1");
        p1.agregarEvento("E1", "Jornada de plantación", "Con Con", "15-03-2026", 5, "NORMAL");
        p1.agregarEvento("E2", "Respuesta a incendio forestal", "Quillota", "20-03-2026", 3, "CATASTROFE");

        gestor.registrarVoluntario("Ana Pérez", "11111111-1", "Valparaíso", true);
        gestor.registrarVoluntario("Juan Soto", "22222222-2", "Quillota", true);
        gestor.registrarVoluntario("María Vidal", "33333333-3", "Con Con", true);

        gestor.buscarVoluntario("11111111-1").agregarHabilidad("Primeros auxilios");
        gestor.buscarVoluntario("22222222-2").agregarHabilidad("Primeros auxilios");
        gestor.buscarVoluntario("33333333-3").agregarHabilidad("Logística");
    }
}
