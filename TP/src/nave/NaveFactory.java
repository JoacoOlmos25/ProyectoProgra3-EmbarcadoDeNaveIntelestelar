package src.nave;

import java.util.Locale;

public class NaveFactory{

    /**Contrato
        *Instancia la nave segun cual se requiera
        *Precondicion
            *Tipo debe tener el tipo de nave a crear
        *Postcondicion
            *Devuelve el tipo de nave creada
     */

    public Nave crearNave(String tipo){
        switch (tipo.toLowerCase()){
            case "ataque": return new Ataque();
            case "carguero": return new Carguero();
            case "explorador": return new Explorador();
            default:
                throw new IllegalArgumentException("Tipo de nave desconocido: " + tipo);
        }
    }

}
