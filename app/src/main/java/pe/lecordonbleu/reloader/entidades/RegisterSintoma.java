package pe.lecordonbleu.reloader.entidades;

public class RegisterSintoma {

    String descripcion;
    int imgstado;


    public RegisterSintoma(String descripcion, int imgstado) {
        this.descripcion = descripcion;
        this.imgstado = imgstado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImgstado() {
        return imgstado;
    }

    public void setImgstado(int imgstado) {
        this.imgstado = imgstado;
    }
}
