package m09uf3.impl.bancapi;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import m03uf6.prob.banc.BancDAO;
import m03uf6.prob.banc.Moviment;

public class BancAPIClientRun {
	
	static final Logger LOGGER = Logger.getLogger(BancAPIClientRun.class.getName());
	
//	static final String HOST = "localhost";
//	static final int PORT = 5513;
	static final String HOST = "146.255.96.104";
	static final int PORT = 9001;
//	
	static final String BASEURL = "http://" + HOST + ":" + PORT;
	
	static final BigDecimal BD100 = BigDecimal.valueOf(100);
	static final BigDecimal BD75 = BigDecimal.valueOf(75);
	static final BigDecimal BD50 = BigDecimal.valueOf(50);
	static final BigDecimal BD25 = BigDecimal.valueOf(25);
	
	public static void main(String[] args) {
		
		LoggingConfigurator.configure(Level.INFO);

		String packageName = Problems.getImplPackage(BancAPIClientRun.class);
		
		BancDAO dao = Reflections.newInstanceOfType(
				BancDAO.class, packageName + ".RemoteBancDAOImpl",
				new Class[] {String.class},
				new Object[] {BASEURL});
		
		try {
			dao.nouCompte(100, BD100);
			dao.nouCompte(101, BD50);
			dao.nouCompte(102, BD25);
			dao.transferir(100,101, BD50);
			dao.transferir(101, 102, BD25);
		
			List<Moviment> l1 = dao.getMoviments(100);
			LOGGER.info("moviments 100: " + l1.toString());
			
			List<Moviment> l2 = dao.getMoviments(101);
			LOGGER.info("moviments 101: " + l2.toString());
			
			List<Moviment> l3 = dao.getMoviments(102);
			LOGGER.info("moviments 102: " + l3.toString());
			
			LOGGER.info("saldo 100: " + dao.getSaldo(100));
			LOGGER.info("saldo 101: " + dao.getSaldo(101));
			LOGGER.info("saldo 102: " + dao.getSaldo(102));
		
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "testing", e);
		}
	}
}
