package Paquete;

public class Excepciones extends RuntimeException{

    public AntiguedadNegativa(){
        super("No se puede tener una antiguedad negativa");
        System.out.println("Se ejecuta print antiguedad negativa");
    }
}
