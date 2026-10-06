package src.Paquete;

import src.AC.Bitacora;

import java.time.LocalDateTime;

public class Prueba {
    public static void main(String[] args) throws AntiguedadNegativa {
        Tripulante t1=new Capitan(2,"Tomas");
        t1 = new Vulcano(t1);
        System.out.println(t1.toString()+ " Remuneracion total=" + t1.getRemu()+"}");
//       System.out.println(t1.toString());
        Bitacora bitacora = new Bitacora();
        bitacora.mostrarBitacora();
        bitacora.agregaEvento(LocalDateTime.now(),"Mision Marte","Realizada con exito :)");
        bitacora.agregaEvento(LocalDateTime.now(),"Mision Jupiter","Realizada sin exito :(");
        bitacora.agregaEvento(LocalDateTime.now(),"Mision Saturno","Realizada parcialmente :|");
        bitacora.mostrarBitacora();

    }
}
