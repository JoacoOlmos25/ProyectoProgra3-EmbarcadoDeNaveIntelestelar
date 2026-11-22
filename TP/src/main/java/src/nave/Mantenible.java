package src.nave;

public interface Mantenible {

    int getDesgaste();

    void incrementaDesgaste(int cant) throws MantenimientoException;

    boolean requiereMantenimiento();

    void realizarMantenimiento() throws MantenimientoException;
}
