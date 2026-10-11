package src.AC;
import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * Clase Bitacora, esta guardara en un array la lista de los eventos creados
 * permitira la agregacion de los eventos a la lista y mostrar el listado en su totalidad por consola.
 * **/
public class Bitacora {

    public static ArrayList<Evento> bitacora = new ArrayList<>();

    public Bitacora(){};

    public static void agregaEvento(LocalDateTime t, String titulo, String msj) {
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
