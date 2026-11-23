package src.nave;

/**
 * Fábrica responsable de instanciar las subclases concretas de Nave (Patrón Factory).
 */
public class NaveFactory {

    /**
     * Contrato:
     * Instancia la nave según el tipo requerido.
     * Precondicion:
     * - tipo != null && !tipo.isBlank()
     * Postcondicion:
     * - Devuelve una instancia no nula de Nave correspondiente al tipo solicitado.
     * @throws IllegalArgumentException si el tipo de nave es desconocido
     */
    public Nave crearNave(String tipo) {
        assert tipo != null && !tipo.isBlank() : "El tipo de nave no puede ser nulo ni vacío";

        Nave creada;
        switch (tipo.trim().toLowerCase()) {
            case "ataque":
                creada = new Ataque();
                break;
            case "carguero":
                creada = new Carguero();
                break;
            case "explorador":
                creada = new Explorador();
                break;
            default:
                throw new IllegalArgumentException("Tipo de nave desconocido: " + tipo);
        }

        assert creada != null : "Postcondición violada: la fábrica devolvió una nave nula";
        return creada;
    }
}