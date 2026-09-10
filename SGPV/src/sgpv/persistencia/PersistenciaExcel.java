package sgpv.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sgpv.modelo.Evento;
import sgpv.excepciones.CupoLlenoException;
import sgpv.excepciones.VoluntarioNoDisponibleException;
import sgpv.modelo.Habilidad;
import sgpv.modelo.GestorVoluntariado;
import sgpv.modelo.Programa;
import sgpv.utils.StringUtils;
import sgpv.modelo.Voluntario;

/**
 * Creacion de una hoja de excel q cumple con la persistencia de datos, con
 * una hoja por tipo de dato: Voluntarios, Programas y Eventos.
 *
 */
public class PersistenciaExcel {

    private static final String HOJA_VOLUNTARIOS = "Voluntarios";
    private static final String HOJA_PROGRAMAS = "Programas";
    private static final String HOJA_EVENTOS = "Eventos";

    private PersistenciaExcel() {
    }

    // Guardar

    public static void guardar(GestorVoluntariado gestor, String rutaArchivo) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            escribirVoluntarios(wb, gestor);
            escribirProgramasYEventos(wb, gestor);

            try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
                wb.write(fos);
            }
        }
    }

    private static void escribirVoluntarios(Workbook wb, GestorVoluntariado gestor) {
        Sheet hoja = wb.createSheet(HOJA_VOLUNTARIOS);
        Row header = hoja.createRow(0);
        String[] columnas = {"nombre", "rut", "comuna", "disponible", "habilidades"};
        for (int i = 0; i < columnas.length; i++) {
            header.createCell(i).setCellValue(columnas[i]);
        }

        int numeroFila = 1;
        for (Voluntario v : gestor.listarVoluntarios()) {
            Row fila = hoja.createRow(numeroFila++);
            fila.createCell(0).setCellValue(v.getNombre());
            fila.createCell(1).setCellValue(v.getRut());
            fila.createCell(2).setCellValue(v.getComuna());
            fila.createCell(3).setCellValue(v.isDisponible());

            StringBuilder habilidades = new StringBuilder();
            for (Habilidad h : v.getHabilidades()) {
                if (habilidades.length() > 0) habilidades.append(",");
                habilidades.append(h.getNombre());
            }
            fila.createCell(4).setCellValue(habilidades.toString());
        }
    }

    private static void escribirProgramasYEventos(Workbook wb, GestorVoluntariado gestor) {
        Sheet hojaProgramas = wb.createSheet(HOJA_PROGRAMAS);
        Row headerProgramas = hojaProgramas.createRow(0);
        String[] colsProgramas = {"idPrograma", "titulo", "descripcion", "fechaInicio"};
        for (int i = 0; i < colsProgramas.length; i++) {
            headerProgramas.createCell(i).setCellValue(colsProgramas[i]);
        }

        Sheet hojaEventos = wb.createSheet(HOJA_EVENTOS);
        Row headerEventos = hojaEventos.createRow(0);
        String[] colsEventos = {"idPrograma", "idEvento", "nombre", "lugar", "comuna",
                "fecha", "cupos", "prioridad", "rutsVoluntariosAsignados"};
        for (int i = 0; i < colsEventos.length; i++) {
            headerEventos.createCell(i).setCellValue(colsEventos[i]);
        }

        int filaPrograma = 1;
        int filaEvento = 1;
        for (Programa p : gestor.listarProgramas()) {
            Row fp = hojaProgramas.createRow(filaPrograma++);
            fp.createCell(0).setCellValue(p.getIdPrograma());
            fp.createCell(1).setCellValue(p.getTitulo());
            fp.createCell(2).setCellValue(p.getDescripcion());
            fp.createCell(3).setCellValue(p.getFechaInicio());

            for (Evento e : p.getEventos().values()) {
                Row fe = hojaEventos.createRow(filaEvento++);
                fe.createCell(0).setCellValue(p.getIdPrograma());
                fe.createCell(1).setCellValue(e.getIdEvento());
                fe.createCell(2).setCellValue(e.getNombre());
                fe.createCell(3).setCellValue(e.getLugar());
                fe.createCell(4).setCellValue(e.getComuna());
                fe.createCell(5).setCellValue(e.getFecha());
                fe.createCell(6).setCellValue(e.getCupos());
                fe.createCell(7).setCellValue(e.getPrioridad().name());

                StringBuilder ruts = new StringBuilder();
                for (Voluntario v : e.getVoluntariosAsignados()) {
                    if (ruts.length() > 0) ruts.append(",");
                    ruts.append(v.getRut());
                }
                fe.createCell(8).setCellValue(ruts.toString());
            }
        }
    }

    // Cargar

    public static GestorVoluntariado cargar(String rutaArchivo) throws IOException {
        GestorVoluntariado gestor = new GestorVoluntariado();

        try (FileInputStream fis = new FileInputStream(rutaArchivo);
                Workbook wb = new XSSFWorkbook(fis)) {

            cargarVoluntarios(wb, gestor);
            cargarProgramas(wb, gestor);
            cargarEventos(wb, gestor);
        }
        return gestor;
    }

    public static boolean existeArchivo(String rutaArchivo) {
        return new File(rutaArchivo).exists();
    }

    private static void cargarVoluntarios(Workbook wb, GestorVoluntariado gestor) {
        Sheet hoja = wb.getSheet(HOJA_VOLUNTARIOS);
        if (hoja == null) return;

        for (int i = 1; i <= hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            if (fila == null) continue;

            String nombre = leerTexto(fila, 0);
            String rut = leerTexto(fila, 1);
            if (StringUtils.isBlank(rut)) continue;
            String comuna = leerTexto(fila, 2);
            boolean disponible = leerBooleano(fila, 3);
            String habilidadesTexto = leerTexto(fila, 4);

            gestor.registrarVoluntario(nombre, rut, comuna, disponible);
            Voluntario v = gestor.buscarVoluntario(rut);
            if (v != null && !StringUtils.isBlank(habilidadesTexto)) {
                for (String nombreHabilidad : habilidadesTexto.split(",")) {
                    if (!StringUtils.isBlank(nombreHabilidad)) {
                        v.agregarHabilidad(new Habilidad(nombreHabilidad.trim()));
                    }
                }
            }
        }
    }

    private static void cargarProgramas(Workbook wb, GestorVoluntariado gestor) {
        Sheet hoja = wb.getSheet(HOJA_PROGRAMAS);
        if (hoja == null) return;

        for (int i = 1; i <= hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            if (fila == null) continue;

            String idPrograma = leerTexto(fila, 0);
            if (StringUtils.isBlank(idPrograma)) continue;
            String titulo = leerTexto(fila, 1);
            String descripcion = leerTexto(fila, 2);
            String fechaInicio = leerTexto(fila, 3);

            gestor.agregarPrograma(idPrograma, titulo, descripcion, fechaInicio);
        }
    }

    private static void cargarEventos(Workbook wb, GestorVoluntariado gestor) {
        Sheet hoja = wb.getSheet(HOJA_EVENTOS);
        if (hoja == null) return;

        for (int i = 1; i <= hoja.getLastRowNum(); i++) {
            Row fila = hoja.getRow(i);
            if (fila == null) continue;

            String idPrograma = leerTexto(fila, 0);
            String idEvento = leerTexto(fila, 1);
            if (StringUtils.isBlank(idPrograma) || StringUtils.isBlank(idEvento)) continue;

            Programa programa = gestor.buscarPrograma(idPrograma);
            if (programa == null) continue;

            String nombre = leerTexto(fila, 2);
            String lugar = leerTexto(fila, 3);
            String comuna = leerTexto(fila, 4);
            String fecha = leerTexto(fila, 5);
            int cupos = (int) leerNumero(fila, 6);
            String prioridad = leerTexto(fila, 7);
            String rutsTexto = leerTexto(fila, 8);

            boolean creado = programa.agregarEvento(idEvento, nombre, lugar, fecha, cupos, prioridad);
            if (!creado) continue;

            Evento evento = programa.getEvento(idEvento);
            evento.setComuna(comuna);

            if (!StringUtils.isBlank(rutsTexto)) {
                for (String rut : rutsTexto.split(",")) {
                    if (StringUtils.isBlank(rut)) continue;
                    Voluntario v = gestor.buscarVoluntario(rut.trim());
                    // Se restaura la asignación tal cual fue guardada; el voluntario
                    // ya quedó marcado como no disponible al cargarlo (SIA-11: batch),
                    // así que se agrega directamente en vez de repetir la validación
                    // de negocio que usa asignarVoluntario() para asignaciones nuevas.
                    if (v != null) {
                        evento.getVoluntariosAsignados().add(v);
                    }
                }
            }
        }
    }

    // Lectura

    private static String leerTexto(Row fila, int indice) {
        Cell celda = fila.getCell(indice);
        if (celda == null) return "";
        if (celda.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
            return String.valueOf((long) celda.getNumericCellValue());
        }
        if (celda.getCellType() == org.apache.poi.ss.usermodel.CellType.BOOLEAN) {
            return String.valueOf(celda.getBooleanCellValue());
        }
        return celda.getStringCellValue() == null ? "" : celda.getStringCellValue().trim();
    }

    private static boolean leerBooleano(Row fila, int indice) {
        Cell celda = fila.getCell(indice);
        if (celda == null) return false;
        if (celda.getCellType() == org.apache.poi.ss.usermodel.CellType.BOOLEAN) {
            return celda.getBooleanCellValue();
        }
        return Boolean.parseBoolean(celda.toString().trim());
    }

    private static double leerNumero(Row fila, int indice) {
        Cell celda = fila.getCell(indice);
        if (celda == null) return 0;
        if (celda.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
            return celda.getNumericCellValue();
        }
        try {
            return Double.parseDouble(celda.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
