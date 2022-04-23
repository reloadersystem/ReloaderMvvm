package pe.lecordonbleu.reloader.entidades;

public class RegisterQR {

    int  id_estud;

    public RegisterQR(int id_estud) {
        this.id_estud = id_estud;
    }

    @Override
    public String toString() {
        return "RegisterQR{" +
                "id_estud=" + id_estud +
                '}';
    }
}
