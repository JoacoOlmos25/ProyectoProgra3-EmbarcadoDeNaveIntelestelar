package src.main;

import src.AC.Asistente;
import src.Misiones.Mision;
import src.Misiones.Mision_01;
import src.Misiones.Mision_02;
import src.Tripulacion.*;
import src.nave.Nave;
import src.nave.NaveFactory;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Nave nave1 = new NaveFactory().crearNave("carguero");
        Nave nave2 = new NaveFactory().crearNave("ataque");
        Nave nave3 = new NaveFactory().crearNave("explorador");
        try {
            Asistente Ac = new Asistente(nave1);
            Tripulante capitan = new Capitan(10,"Pepe");
            capitan = new Terricola(capitan);
            Tripulante t2 = new Teniente(1,"el Teniente");
            t2 = new Vulcano(t2);
            Tripulante t3 = new Alferez(1,"el Alferez");
            t3 = new Marciano(t3);
            Tripulante t4 = new Consejero(1,"el Consejero 1");
            t4 = new Vulcano(t4);
            Tripulante t5 = new Consejero(1,"el Cosnsejo 2");
            t5 = new Terricola(t5);

            nave1.insertarTripulante(capitan);
            nave1.insertarTripulante(t2);
            nave1.insertarTripulante(t3);
            nave1.insertarTripulante(t4);
            nave1.insertarTripulante(t5);



            Mision m1 = new Mision_01(4,15,4,Ac); // calculo de energia ahora es arbitrario ya que no puede calcularse dinamicamente con el universo
            m1.realizarMision();

            Ac.mostrarBitacora();

            // cambio de Nave al Ac

            Asistente Ac2 = new Asistente(nave2);

            System.out.println(" cambio de nave, nave de Ataque");

            nave2.insertarTripulante(capitan);
            nave2.insertarTripulante(t2);
            nave2.insertarTripulante(t3);
            nave2.insertarTripulante(t4);
            nave2.insertarTripulante(t5);

            Mision m2 = new Mision_02(50000,15,4, Ac2); // calculo de energia ahora es arbitrario ya que no puede calcularse dinamicamente con el universo
            m2.realizarMision();

            Ac2.mostrarBitacora();

        } catch (AntiguedadNegativa e) {
            throw new RuntimeException(e);
        }

    }
}