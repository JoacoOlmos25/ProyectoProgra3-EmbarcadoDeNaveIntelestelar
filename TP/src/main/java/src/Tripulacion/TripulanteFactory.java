package src.Tripulacion;

/**
 * Fábrica responsable de instanciar tripulantes según su cargo
 * y aplicar el decorador correspondiente a su origen.
 */
public class TripulanteFactory {

    /**
     * Contrato:
     * Instancia un tripulante decorado según el cargo y origen solicitados.
     * Precondicion:
     * - cargo, origen e ident no son nulos ni vacíos.
     * Postcondicion:
     * - Devuelve una instancia no nula de Tripulante lista para incorporarse a una Nave.
     * @throws AntiguedadNegativa si antiguedad < 0
     * @throws IllegalArgumentException si el cargo o el origen son desconocidos
     */
    public Tripulante crearTripulante(String cargo, String origen, int antiguedad, String ident)
            throws AntiguedadNegativa {
        assert cargo != null && !cargo.isBlank() : "El cargo no puede ser nulo o vacío";
        assert origen != null && !origen.isBlank() : "El origen no puede ser nulo o vacío";
        assert ident != null && !ident.isBlank() : "La identidad no puede ser nula o vacía";

        Tripulante base = crearPorCargo(cargo, antiguedad, ident);
        Tripulante resultado = aplicarOrigen(base, origen);

        assert resultado != null : "Postcondición violada: la fábrica devolvió un tripulante nulo";
        return resultado;
    }

    private Tripulante crearPorCargo(String cargo, int antiguedad, String ident)
            throws AntiguedadNegativa {
        switch (cargo.trim().toLowerCase()) {
            case "capitan":
                return new Capitan(antiguedad, ident);
            case "teniente":
                return new Teniente(antiguedad, ident);
            case "alferez":
                return new Alferez(antiguedad, ident);
            case "consejero":
                return new Consejero(antiguedad, ident);
            default:
                throw new IllegalArgumentException("Cargo de tripulante desconocido: " + cargo);
        }
    }

    private Tripulante aplicarOrigen(Tripulante base, String origen) {
        switch (origen.trim().toLowerCase()) {
            case "terricola":
                return new Terricola(base);
            case "vulcano":
                return new Vulcano(base);
            case "marciano":
                return new Marciano(base);
            default:
                throw new IllegalArgumentException("Origen de tripulante desconocido: " + origen);
        }
    }
}