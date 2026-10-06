package src.Misiones;

import src.AC.Asistente;

public abstract class Mision {
    protected int combNecesario;
    protected int energiaNecesaria;
    protected int desgaste;
    protected Asistente ac;

    public Mision(int combustible, int energia, int desgaste){
        super();
        this.combNecesario= combustible;
        this.energiaNecesaria = energia;
        this.desgaste = desgaste;
    }

    @override
    public String toString() {
    return "Mision{Combustible necesario=" + combNecesario +", Energia necesaria=" + energiaNecesaria +", Desgaste=" + desgaste + '}';
    }
}
