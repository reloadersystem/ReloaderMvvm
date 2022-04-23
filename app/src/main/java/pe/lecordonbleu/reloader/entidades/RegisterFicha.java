package pe.lecordonbleu.reloader.entidades;

public class RegisterFicha {

       private String id_estud34;
       private int uneg;


    public RegisterFicha(String id_estud34, int uneg) {
        this.id_estud34 = id_estud34;
        this.uneg = uneg;
    }

    @Override
    public String toString() {
        return "RegisterFicha{" +
                "id_estud34='" + id_estud34 + '\'' +
                ", uneg=" + uneg +
                '}';
    }
}
