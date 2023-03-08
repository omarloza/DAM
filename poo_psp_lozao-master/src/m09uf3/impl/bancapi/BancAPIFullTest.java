package m09uf3.impl.bancapi;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lib.LoggingConfigurator;
import lib.Problems;
import lib.Reflections;
import lib.db.ConnectionFactory;
import lib.db.DuplicateException;
import lib.db.NotFoundException;
import lib.http.HTTPProcessor;
import lib.http.SocketHTTPServer;
import m03uf6.prob.banc.BancDAO;
import m03uf6.prob.banc.Moviment;
import m03uf6.prob.banc.SenseFonsException;

public class BancAPIFullTest {

	static final String HOST = "localhost";
	static final int PORT = 5513;
	
	static final String BASEURL = "http://" + HOST + ":" + PORT;

	static final BigDecimal BD100 = BigDecimal.valueOf(100);
	static final BigDecimal BD75 = BigDecimal.valueOf(75);
	static final BigDecimal BD50 = BigDecimal.valueOf(50);
	static final BigDecimal BD25 = BigDecimal.valueOf(25);
	
	private static BancDAO ldao, dao;
	private static SocketHTTPServer server;
	
	@BeforeAll
	static void beforeAll() {
		
		LoggingConfigurator.configure(Level.INFO);

		String packageName = Problems.getImplPackage(BancAPIFullTest.class);

		ConnectionFactory cf = Reflections.newInstanceOfType(
				ConnectionFactory.class, 
				packageName + ".ConnectionFactoryImpl");
			
		ldao = Reflections.newInstanceOfType(
				BancDAO.class, packageName + ".BancDAOImpl", 
				new Class[] {ConnectionFactory.class}, 
				new Object[] {cf});
		
		HTTPProcessor processor = Reflections.newInstanceOfType(
				HTTPProcessor.class, packageName + ".BancAPIProcessorImpl",
				new Class[] {BancDAO.class}, 
				new Object[] {ldao});

		dao = Reflections.newInstanceOfType(
				BancDAO.class, packageName + ".RemoteBancDAOImpl",
				new Class[] {String.class},
				new Object[] {BASEURL});
		
		server = new SocketHTTPServer(processor, HOST, PORT);
		server.start();
	}
	
	@BeforeEach
	void beforeEach() {				
		ldao.init();
	}
	
	@AfterAll
	static void afterAll() {
		server.stop();		
	}
	
	@Test
	void testCompteOk() {
		
		try {
			dao.nouCompte(1, BD100);
			assertEquals(0, BD100.compareTo(dao.getSaldo(1)));
			
		} catch (NotFoundException | SenseFonsException | DuplicateException e) {
			fail("no pot fallar!");
		}		
	}
		
	@Test
	void testMovimentOk() {		
		
		try {
			dao.nouCompte(1, BD100);
			dao.nouCompte(2, BD50);
			dao.nouCompte(3, BD25);
			dao.transferir(1, 2, BD50);
			dao.transferir(2, 3, BD25);
			
			List<Moviment> l1 = dao.getMoviments(1);
			assertEquals(1, l1.size());
			checkMoviment(l1.get(0), 1, 2, BD50);
			
			List<Moviment> l2 = dao.getMoviments(2);
			assertEquals(2, l2.size());
			checkMoviment(l2.get(0), 1, 2, BD50);
			checkMoviment(l2.get(1), 2, 3, BD25);
			
			List<Moviment> l3 = dao.getMoviments(3);
			assertEquals(1, l3.size());
			checkMoviment(l3.get(0), 2, 3, BD25);
			
			assertEquals(0, BD50.compareTo(dao.getSaldo(1)));
			assertEquals(0, BD75.compareTo(dao.getSaldo(2)));
			assertEquals(0, BD50.compareTo(dao.getSaldo(3)));
			
		} catch (NotFoundException | SenseFonsException | DuplicateException e) {
			fail("no pot fallar!");
		}
	}
	
	@Test
	void testSenseFons() {
		
		try {
			dao.nouCompte(1, BD100);
			dao.nouCompte(2, BD50);
			dao.transferir(2, 1, BD100);
			fail("no ha d'arribar aqui!");
	
		} catch (NotFoundException | DuplicateException e) {			
			fail("no pot fallar!");
			
		} catch (SenseFonsException e) {
			
			try { // comptes no modificats
				assertEquals(0, BD100.compareTo(dao.getSaldo(1)));
				assertEquals(0, BD50.compareTo(dao.getSaldo(2)));
				
			} catch (NotFoundException e1) {
				fail("no pot fallar!");
			}
		}		
	}
	
	@Test
	void testDuplicat() {
		
		try {
			dao.nouCompte(1, BD100);
			
		} catch (DuplicateException e) {
			fail("no pot fallar!");
		}
		
		try {
			dao.nouCompte(1, BD50);
			fail("no ha d'arribar aqui!");
			
		} catch (DuplicateException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testCompteDesconegut() {
		
		try {
			dao.getSaldo(1);
			fail("no ha d'arribar aqui!");
			
		} catch (NotFoundException e) {}
	}
	
	private void checkMoviment(Moviment m, int origen, int desti, BigDecimal q) {
		
		assertEquals(origen, m.origen);
		assertEquals(desti, m.desti);
		assertEquals(0, q.compareTo(m.quantitat));
	}

}
