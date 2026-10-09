package src.AC;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Bitacora {

    public ArrayList<Evento> bitacora = new ArrayList<>();

    public Bitacora(){};

    public void agregaEvento(LocalDateTime t, String titulo,String msj ) {
       Evento evento = new Evento(t,titulo,msj);
       bitacora.add(evento);
    }

    public void mostrarBitacora(){
        int i,n = bitacora.size();

        if (n == 0){
            System.out.println("No han ocurrido eventos");
        }else{
            for (i=0 ;i<n;i++){
              System.out.println( bitacora.get(i).toString());
            }
        }
    };
}
