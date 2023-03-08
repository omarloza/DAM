package m09uf3.impl.bancapi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lib.db.ConnectionFactory;
import lib.db.DAOException;
import lib.db.DuplicateException;
import lib.db.NotFoundException;
import m03uf6.prob.banc.BancDAO;
import m03uf6.prob.banc.BancDB;
import m03uf6.prob.banc.Moviment;
import m03uf6.prob.banc.SenseFonsException;

public class BancDAOImpl implements BancDAO {

	private ConnectionFactory cf;
	
	public BancDAOImpl(ConnectionFactory cf) {
		this.cf = cf;
	}
	
	@Override
	public void init() {
		BancDB.init(cf);
	}
	
	@Override
	public void nouCompte(int numero, BigDecimal saldoInicial) throws DuplicateException {

		String sql = "INSERT INTO compte (id_compte, saldo) VALUES (?,?)";

		try (Connection conn = cf.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, numero);
			ps.setBigDecimal(2, saldoInicial);
			ps.executeUpdate();

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DuplicateException(e);

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public int transferir(int origen, int desti, BigDecimal quantitat) 
			throws NotFoundException, SenseFonsException {
		
		try (Connection conn = cf.getConnection()) {
			
			conn.setAutoCommit(false);
			
		    try {		        		        
		        BigDecimal so = getSaldo(conn, origen);
		        BigDecimal sd = getSaldo(conn, desti);
		        
		        if (so.subtract(quantitat).compareTo(BigDecimal.ZERO) < 0) {
		        	throw new SenseFonsException();
		        }

		        setSaldo(conn, origen, so.subtract(quantitat));
		        setSaldo(conn, desti, sd.add(quantitat));
		        int id = addMoviment(conn, origen, desti, quantitat);		        
		        
		        conn.commit();
		        return id;
		        
		    } catch (Exception e) {
		    	conn.rollback();
		    	throw e;
		    }
		    
		} catch (SQLException e) {
		    throw new DAOException(e);
		}
	}
	
	private int addMoviment(Connection conn, int origen, int desti, BigDecimal quantitat) {

		String sql = "INSERT INTO moviment (id_compte_origen, id_compte_desti, quantitat) VALUES (?,?,?)";

		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, origen);
			ps.setInt(2, desti);
			ps.setBigDecimal(3, quantitat);
			ps.executeUpdate();
			
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					throw new DAOException("falta clau generada a tasques");
				}
			}
			
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	private void setSaldo(Connection conn, int compte, BigDecimal saldo) throws SQLException {
		
		String sql = "UPDATE compte SET saldo = ? WHERE id_compte = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setBigDecimal(1, saldo);
			ps.setInt(2, compte);
			ps.executeUpdate();
		}
	}
	
	private BigDecimal getSaldo(Connection conn, int compte) throws SQLException, NotFoundException {
		
		String sql = "SELECT saldo FROM compte WHERE id_compte = ?";
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, compte);			
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					return rs.getBigDecimal(1);

				} else {
					throw new NotFoundException("compte " + compte);
				}
			}
		}
	}
	
	@Override
	public BigDecimal getSaldo(int compte) throws NotFoundException {
		
		try (Connection conn = cf.getConnection()) {
			return getSaldo(conn, compte);
			
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public List<Moviment> getMoviments(int compte) {
		
		List<Moviment> result = new ArrayList<>();
		
		String sql = "SELECT * FROM `moviment` WHERE id_compte_origen = ? OR id_compte_desti = ? ORDER BY id_moviment ASC";
		
		try (	Connection conn = cf.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, compte);
			ps.setInt(2, compte);
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {				
					Moviment m = new Moviment();
					m.origen = rs.getInt("id_compte_origen");
					m.desti = rs.getInt("id_compte_desti");
					m.quantitat = rs.getBigDecimal("quantitat");				
					result.add(m);
				}	
			}
						
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		
		return result;
	}
}
