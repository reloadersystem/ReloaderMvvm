package pe.lecordonbleu.reloader.entidades;

public class RegisterUserCorreo {

    private String usuario;
    private int uneg;
    private String nivel;

    public RegisterUserCorreo(String usuario, int uneg, String nivel) {
        this.usuario = usuario;
        this.uneg = uneg;
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "RegisterUserCorreo{" +
                "usuario='" + usuario + '\'' +
                ", uneg=" + uneg +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}
