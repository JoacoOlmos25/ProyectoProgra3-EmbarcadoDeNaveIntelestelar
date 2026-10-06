package src.nave;

import java.util.Locale;

public class NaveFactory{

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
