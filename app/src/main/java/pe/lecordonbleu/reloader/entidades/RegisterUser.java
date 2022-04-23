package pe.lecordonbleu.reloader.entidades;

public class RegisterUser {

    private String usuario;
    private String contrasena;
    private int uneg;
    private String nivel;

    public RegisterUser(){

    }


    public RegisterUser(String usuario, String contrasena, int uneg, String nivel) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.uneg = uneg;
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "RegisterUser{" +
                "usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", uneg=" + uneg +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}
