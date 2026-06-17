package dominio;

import dto.Respuesta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Cita;
import utilidades.Constantes;

public class CitaImp {

    private static final int ESTADO_CONFIRMADA = 2;
    private static final int ESTADO_CANCELADA = 3;
    private static final int ESTADO_REAGENDADA = 4;
    private static final int ESTADO_ASISTIDA = 5;
    private static final int ESTADO_AUSENTE = 6;

    private static final LocalTime HORA_INICIO = LocalTime.of(7, 0);
    private static final LocalTime ULTIMA_HORA_REGISTRO = LocalTime.of(20, 30);
    private static final int INTERVALO_MINUTOS = 30;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMATO_HORA_CORTA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATO_FECHA_HORA_CORTA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static synchronized Respuesta registrar(Cita cita) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        String validacion = validarDatosRegistro(cita);
        if (validacion != null) {
            respuesta.setMensaje(validacion);
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            Respuesta respuestaValidacion = validarReglasNegocio(conexionBD, cita, null);
            if (respuestaValidacion.isError()) {
                return respuestaValidacion;
            }

            cita.setIdEstadoCita(ESTADO_CONFIRMADA);
            int filas = conexionBD.insert("cita.registrar", cita);

            if (filas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Cita registrada correctamente.");
            } else {
                conexionBD.rollback();
                respuesta.setMensaje("No se pudo registrar la cita.");
            }
        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error al registrar la cita: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }

    public static synchronized Respuesta reagendar(Integer idCita, String nuevaFechaHora, String comentarios) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (idCita == null || idCita <= 0) {
            respuesta.setMensaje("El id de la cita es obligatorio.");
            return respuesta;
        }
        if (nuevaFechaHora == null || nuevaFechaHora.trim().isEmpty()) {
            respuesta.setMensaje("La nueva fecha y hora de la cita son obligatorias.");
            return respuesta;
        }

        try {
            LocalDateTime fechaHora = parseFechaHora(nuevaFechaHora);
            Cita cita = new Cita();
            cita.setIdCita(idCita);
            cita.setFecha(fechaHora.toLocalDate().format(FORMATO_FECHA));
            cita.setHora(fechaHora.toLocalTime().format(FORMATO_HORA));
            cita.setObservaciones(comentarios);
            return reagendar(cita);
        } catch (DateTimeParseException e) {
            respuesta.setMensaje("El formato de fecha u hora no es valido.");
            return respuesta;
        }
    }

    public static synchronized Respuesta reagendar(Cita cita) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (cita == null || cita.getIdCita() == null || cita.getIdCita() <= 0) {
            respuesta.setMensaje("El id de la cita es obligatorio.");
            return respuesta;
        }

        if (cita.getObservaciones() == null || cita.getObservaciones().trim().isEmpty()) {
            respuesta.setMensaje("El comentario de justificacion es obligatorio para reagendar.");
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            Cita citaActual = conexionBD.selectOne("cita.obtener-por-id", cita.getIdCita());
            if (citaActual == null) {
                conexionBD.rollback();
                respuesta.setMensaje("La cita indicada no existe.");
                return respuesta;
            }
            if (cita.getIdPaciente() == null || cita.getIdPaciente() <= 0) {
                cita.setIdPaciente(citaActual.getIdPaciente());
            }
            if (cita.getIdMedico() == null || cita.getIdMedico() <= 0) {
                cita.setIdMedico(citaActual.getIdMedico());
            }

            String validacion = validarDatosRegistro(cita);
            if (validacion != null) {
                conexionBD.rollback();
                respuesta.setMensaje(validacion);
                return respuesta;
            }

            Respuesta respuestaValidacion = validarReglasNegocio(conexionBD, cita, cita.getIdCita());
            if (respuestaValidacion.isError()) {
                conexionBD.rollback();
                return respuestaValidacion;
            }

            cita.setIdEstadoCita(ESTADO_REAGENDADA);
            int filas = conexionBD.update("cita.reagendar", cita);

            if (filas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Cita reagendada correctamente.");
            } else {
                conexionBD.rollback();
                respuesta.setMensaje("No se pudo reagendar la cita.");
            }
        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error al reagendar la cita: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }

    public static synchronized Respuesta cancelar(Integer idCita, String comentarios) {
        Cita cita = new Cita();
        cita.setIdCita(idCita);
        cita.setMotivoCancelacion(comentarios);
        return cancelar(cita);
    }

    public static synchronized Respuesta cancelar(Cita cita) {
        if (cita != null) {
            cita.setIdEstadoCita(ESTADO_CANCELADA);
        }
        return actualizarEstatus(cita);
    }
    
    public static Respuesta cancelarConMotivo(Cita cita) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                Cita citaBD = conexionBD.selectOne(
                        "cita.obtener-por-id",
                        cita.getIdCita()
                );

                if (citaBD == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("La cita no existe.");
                    return respuesta;
                }

                LocalDate fechaCita = LocalDate.parse(citaBD.getFecha());
                LocalDate manana = LocalDate.now().plusDays(1);

                if (fechaCita.isBefore(manana)) {
                    respuesta.setError(true);
                    respuesta.setMensaje(
                            "Solo puedes cancelar una cita con al menos un día de anticipación."
                    );
                    return respuesta;
                }

                int filasAfectadas = conexionBD.update(
                        "cita.cancelar-con-motivo",
                        cita
                );

                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Cita cancelada correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo cancelar la cita.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje(
                        "Error al cancelar la cita: " + e.getMessage()
                );
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static synchronized Respuesta actualizarEstatus(Cita cita) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (cita == null || cita.getIdCita() == null || cita.getIdCita() <= 0) {
            respuesta.setMensaje("El id de la cita es obligatorio.");
            return respuesta;
        }
        if (cita.getIdEstadoCita() == null || !esEstadoPermitido(cita.getIdEstadoCita())) {
            respuesta.setMensaje("El estado de la cita no es valido.");
            return respuesta;
        }
        if (cita.getIdEstadoCita() == ESTADO_CANCELADA
                && (cita.getMotivoCancelacion() == null || cita.getMotivoCancelacion().trim().isEmpty())) {
            respuesta.setMensaje("El motivo de cancelacion es obligatorio.");
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            Cita citaActual = conexionBD.selectOne("cita.obtener-por-id", cita.getIdCita());
            if (citaActual == null) {
                conexionBD.rollback();
                respuesta.setMensaje("La cita indicada no existe.");
                return respuesta;
            }
            if (cita.getIdEstadoCita() == ESTADO_CANCELADA) {
                LocalDateTime fechaHoraCita = construirFechaHora(citaActual.getFecha(), citaActual.getHora());
                if (!validarMargenCancelacion(fechaHoraCita)) {
                    conexionBD.rollback();
                    respuesta.setMensaje("La cita solo puede cancelarse con al menos una hora de anticipacion.");
                    return respuesta;
                }
            }

            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("idCita", cita.getIdCita());
            parametros.put("idEstadoCita", cita.getIdEstadoCita());
            parametros.put("motivo", cita.getMotivoCancelacion());

            int filas = conexionBD.update("cita.actualizar-estatus", parametros);
            if (filas > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Estatus de la cita actualizado correctamente.");
            } else {
                conexionBD.rollback();
                respuesta.setMensaje("No se pudo actualizar el estatus de la cita.");
            }
        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error al actualizar el estatus de la cita: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }

    public static List<Cita> obtenerPorMedico(Integer idMedico, String fecha) {
        List<Cita> citas = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.update("cita.marcar-ausentes-automatico");
                conexionBD.commit();

                HashMap<String, Object> parametros = new HashMap<>();
                parametros.put("idMedico", idMedico);
                parametros.put("fecha", fecha);

                citas = conexionBD.selectList("cita.obtener-por-medico", parametros);
            } catch (Exception e) {
                conexionBD.rollback();
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return citas;
    }

    public static List<Cita> obtenerTodas() {
        List<Cita> citas = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.update("cita.marcar-ausentes-automatico");
                conexionBD.commit();

                citas = conexionBD.selectList("cita.obtener-todas");
            } catch (Exception e) {
                conexionBD.rollback();
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return citas;
    }

    public static List<Cita> obtenerPorFecha(String fecha) {
        List<Cita> citas = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.update("cita.marcar-ausentes-automatico");
                conexionBD.commit();

                citas = conexionBD.selectList("cita.obtener-por-fecha", fecha);
            } catch (Exception e) {
                conexionBD.rollback();
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return citas;
    }

    public static List<String> obtenerHorasDisponibles(Integer idMedico, String fecha, Integer idCitaExcluir) {
        List<String> horasDisponibles = new ArrayList<>();

        if (idMedico == null || idMedico <= 0 || fecha == null || fecha.trim().isEmpty()) {
            return horasDisponibles;
        }

        try {
            LocalDate.parse(fecha, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            return horasDisponibles;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            return horasDisponibles;
        }

        try {
            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("idMedico", idMedico);
            parametros.put("fecha", fecha);
            parametros.put("idCita", idCitaExcluir);

            List<String> horasOcupadas = conexionBD.selectList("cita.obtener-horas-ocupadas", parametros);
            Set<String> ocupadas = new HashSet<>(horasOcupadas);

            LocalTime hora = HORA_INICIO;
            while (!hora.isAfter(ULTIMA_HORA_REGISTRO)) {
                String horaTexto = hora.format(FORMATO_HORA_CORTA);
                if (!ocupadas.contains(horaTexto)) {
                    horasDisponibles.add(horaTexto);
                }
                hora = hora.plusMinutes(INTERVALO_MINUTOS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }

        return horasDisponibles;
    }

    public static List<Cita> obtenerVigentesPaciente(Integer idPaciente) {
        List<Cita> citas = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.update("cita.marcar-ausentes-automatico");
                conexionBD.commit();
                citas = conexionBD.selectList("cita.obtener-vigentes-paciente", idPaciente);
            } catch (Exception e) {
                conexionBD.rollback();
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return citas;
    }

    public static Respuesta marcarAusentesAutomatico() {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            conexionBD.update("cita.marcar-ausentes-automatico");
            conexionBD.commit();
            respuesta.setError(false);
            respuesta.setMensaje("Citas ausentes actualizadas correctamente.");
        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error al marcar citas ausentes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }

    public static void ejecutarCierreDeAgenda() {
        marcarAusentesAutomatico();
    }

    private static String validarDatosRegistro(Cita cita) {
        if (cita == null) {
            return "Los datos de la cita son obligatorios.";
        }
        if (cita.getIdPaciente() == null || cita.getIdPaciente() <= 0) {
            return "El paciente es obligatorio.";
        }
        if (cita.getIdMedico() == null || cita.getIdMedico() <= 0) {
            return "El medico es obligatorio.";
        }
        if (cita.getFecha() == null || cita.getFecha().trim().isEmpty()) {
            return "La fecha de la cita es obligatoria.";
        }
        if (cita.getHora() == null || cita.getHora().trim().isEmpty()) {
            return "La hora de la cita es obligatoria.";
        }

        try {
            LocalDate fecha = LocalDate.parse(cita.getFecha(), FORMATO_FECHA);
            LocalTime hora = parseHora(cita.getHora());
            LocalDateTime fechaHoraCita = LocalDateTime.of(fecha, hora);

            if (fecha.isBefore(LocalDate.now())) {
                return "No se pueden registrar citas en fechas pasadas.";
            }
            if (!validarAnticipacion(fechaHoraCita)) {
                return "La cita debe registrarse con al menos 1 hora de anticipacion.";
            }
            if (hora.isBefore(HORA_INICIO) || hora.isAfter(ULTIMA_HORA_REGISTRO)) {
                return "La hora de la cita debe estar entre 07:00 y 20:30.";
            }
            if (!validarHorarioPermitido(fechaHoraCita)) {
                return "La cita debe registrarse en bloques de 30 minutos.";
            }
        } catch (DateTimeParseException e) {
            return "El formato de fecha u hora no es valido.";
        }

        return null;
    }
    
    public static List<Cita> obtenerPorPaciente(Integer idPaciente) {
        List<Cita> citas = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                citas = conexionBD.selectList(
                        "cita.obtener-por-paciente",
                        idPaciente
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return citas;
    }

    private static boolean validarAnticipacion(LocalDateTime fechaCita) {
        return !fechaCita.isBefore(LocalDateTime.now().plusHours(1));
    }

    private static boolean validarHorarioPermitido(LocalDateTime fechaCita) {
        LocalTime hora = fechaCita.toLocalTime();
        return !hora.isBefore(HORA_INICIO)
                && !hora.isAfter(ULTIMA_HORA_REGISTRO)
                && (hora.getMinute() == 0 || hora.getMinute() == 30)
                && hora.getSecond() == 0;
    }

    private static boolean validarMargenCancelacion(LocalDateTime fechaCita) {
        return !fechaCita.isBefore(LocalDateTime.now().plusHours(1));
    }

    private static Respuesta validarReglasNegocio(SqlSession conexionBD, Cita cita, Integer idCitaExcluir) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        Integer existePaciente = conexionBD.selectOne("cita.verificar-paciente-existe", cita.getIdPaciente());
        if (existePaciente == null || existePaciente == 0) {
            respuesta.setMensaje("El paciente indicado no existe.");
            return respuesta;
        }

        Integer existeMedico = conexionBD.selectOne("cita.verificar-medico-existe", cita.getIdMedico());
        if (existeMedico == null || existeMedico == 0) {
            respuesta.setMensaje("El medico indicado no existe.");
            return respuesta;
        }

        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idPaciente", cita.getIdPaciente());
        parametros.put("idMedico", cita.getIdMedico());
        parametros.put("fecha", cita.getFecha());
        parametros.put("hora", normalizarHora(cita.getHora()));
        parametros.put("idCita", idCitaExcluir);

        Integer citasPacienteDia = conexionBD.selectOne("cita.verificar-cita-paciente-dia", parametros);
        if (citasPacienteDia != null && citasPacienteDia > 0) {
            respuesta.setMensaje("El paciente ya tiene una cita activa en la fecha seleccionada.");
            return respuesta;
        }

        Integer citasMedicoHorario = conexionBD.selectOne("cita.verificar-disponibilidad-medico", parametros);
        if (citasMedicoHorario != null && citasMedicoHorario > 0) {
            respuesta.setMensaje("El medico no tiene disponible el horario seleccionado.");
            return respuesta;
        }

        cita.setHora(normalizarHora(cita.getHora()));
        respuesta.setError(false);
        respuesta.setMensaje("Validacion correcta.");
        return respuesta;
    }

    private static LocalTime parseHora(String hora) {
        try {
            return LocalTime.parse(hora, FORMATO_HORA);
        } catch (DateTimeParseException e) {
            return LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm"));
        }
    }

    private static LocalDateTime parseFechaHora(String fechaHora) {
        String valor = fechaHora.trim().replace("T", " ");
        try {
            return LocalDateTime.parse(valor, FORMATO_FECHA_HORA);
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(valor, FORMATO_FECHA_HORA_CORTA);
        }
    }

    private static LocalDateTime construirFechaHora(String fecha, String hora) {
        LocalDate fechaCita = LocalDate.parse(fecha, FORMATO_FECHA);
        LocalTime horaCita = parseHora(hora);
        return LocalDateTime.of(fechaCita, horaCita);
    }

    private static String normalizarHora(String hora) {
        return parseHora(hora).format(FORMATO_HORA);
    }

    private static boolean esEstadoPermitido(int estado) {
        return estado == ESTADO_CANCELADA
                || estado == ESTADO_REAGENDADA
                || estado == ESTADO_ASISTIDA
                || estado == ESTADO_AUSENTE;
    }
}
