package diana.org.proyectoandroid;

/**
 * Created by YOO on 04/01/2018.
 */

public class Estatus {
    boolean success;
    String mensaje;

    public Estatus() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
