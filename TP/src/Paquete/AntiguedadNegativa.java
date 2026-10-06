package src.Paquete;

public class AntiguedadNegativa extends Exception{

    public AntiguedadNegativa(){
        super("No se puede tener una antiguedad negativa");
        System.out.println("Se ejecuta print antiguedad negativa");
    }
}
