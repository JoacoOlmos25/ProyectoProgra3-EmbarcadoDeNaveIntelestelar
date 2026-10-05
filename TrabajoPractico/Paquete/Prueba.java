package Paquete;

public class Prueba {
    public static void main(String[] args) throws AntiguedadNegativa {
        Tripulante t1=new Capitan(2,"Tomas");
        t1 = new Vulcano(t1);
        System.out.println(t1.toString()+ " Remuneracion total=" + t1.getRemu()+"}");
//        System.out.println(t1.toString());
    }
}
