package pe.lecordonbleu.reloader.entidades;

public class ResponseAuth {

    public String dataCarrera;
    public String dataMenu;
    public String dataPerfil;
    public int flag;
    public String mensaje;


    public String getDataCarrera() {
        return dataCarrera;
    }

    public void setDataCarrera(String dataCarrera) {
        this.dataCarrera = dataCarrera;
    }

    public String getDataMenu() {
        return dataMenu;
    }

    public void setDataMenu(String dataMenu) {
        this.dataMenu = dataMenu;
    }

    public String getDataPerfil() {
        return dataPerfil;
    }

    public void setDataPerfil(String dataPerfil) {
        this.dataPerfil = dataPerfil;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
