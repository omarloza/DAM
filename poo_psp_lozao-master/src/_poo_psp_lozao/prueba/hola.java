package _poo_psp_lozao.prueba;


import java.util.logging.Logger;
import org.json.JSONObject;
import lib.LoggingConfigurator;

public class hola {
    
    static final Logger LOGGER = Logger.getLogger(hola.class.getName());

    public static void main(String[] args) {
   	 LoggingConfigurator.configure();
   	 LOGGER.info(new JSONObject().put("hola", "mon").toString(4));
    }
}
