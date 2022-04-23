package pe.lecordonbleu.reloader.entidades;

public class RegisterMenu {
    String icono;
    String textoMenuAbrev;
    String textoMenu;
    int idMenu;
    int orden;
    int nivel;
    int idMenuPadre;

    public RegisterMenu(){

    }

    public RegisterMenu(String icono, String textoMenuAbrev, String textoMenu, int idMenu, int orden, int nivel, int idMenuPadre) {
        this.icono = icono;
        this.textoMenuAbrev = textoMenuAbrev;
        this.textoMenu = textoMenu;
        this.idMenu = idMenu;
        this.orden = orden;
        this.nivel = nivel;
        this.idMenuPadre = idMenuPadre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getTextoMenuAbrev() {
        return textoMenuAbrev;
    }

    public void setTextoMenuAbrev(String textoMenuAbrev) {
        this.textoMenuAbrev = textoMenuAbrev;
    }

    public String getTextoMenu() {
        return textoMenu;
    }

    public void setTextoMenu(String textoMenu) {
        this.textoMenu = textoMenu;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getIdMenuPadre() {
        return idMenuPadre;
    }

    public void setIdMenuPadre(int idMenuPadre) {
        this.idMenuPadre = idMenuPadre;
    }
}
