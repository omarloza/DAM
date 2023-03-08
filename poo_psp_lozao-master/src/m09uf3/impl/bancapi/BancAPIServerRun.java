package m09uf3.impl.bancapi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.db.ConnectionFactory;
import lib.http.HTTPProcessor;
import lib.http.SocketHTTPServer;
import m03uf6.prob.banc.BancDAO;

public class BancAPIServerRun {

	static final Logger LOGGER = Logger.getLogger(BancAPIServerRun.class.getName());
	
	static final String HOST = "localhost";
	static final int PORT = 5513;
	
	static final String BASEURL = "http://" + HOST + ":" + PORT;
	
	public static void main(String[] args) throws IOException {
		
		LoggingConfigurator.configure(Level.INFO);

		String packageName = Problems.getImplPackage(BancAPIServerRun.class);

		ConnectionFactory cf = Reflections.newInstanceOfType(
				ConnectionFactory.class, 
				packageName + ".ConnectionFactoryImpl");
			
		BancDAO dao = Reflections.newInstanceOfType(
				BancDAO.class, packageName + ".BancDAOImpl", 
				new Class[] {ConnectionFactory.class}, 
				new Object[] {cf});
		
		dao.init();
		
		HTTPProcessor processor = Reflections.newInstanceOfType(
				HTTPProcessor.class, packageName + ".BancAPIProcessorImpl",
				new Class[] {BancDAO.class}, 
				new Object[] {dao});
		
		SocketHTTPServer server = new SocketHTTPServer(processor, HOST, PORT);
		server.start();
	}
}
