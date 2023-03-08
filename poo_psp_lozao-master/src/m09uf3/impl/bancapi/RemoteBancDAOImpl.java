package m09uf3.impl.bancapi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import lib.db.DuplicateException;
import lib.db.NotFoundException;
import m03uf6.prob.banc.BancDAO;
import m03uf6.prob.banc.Moviment;
import m03uf6.prob.banc.SenseFonsException;

public class RemoteBancDAOImpl implements BancDAO {

	private String BASEURL;

	public RemoteBancDAOImpl(String BASEURL) {

		this.BASEURL = BASEURL;
	}

	@Override
	public void init() {
		throw new RuntimeException();

	}

	@Override
	public void nouCompte(int numero, BigDecimal saldoInicial) throws DuplicateException {

		JSONObject json;
		json = new JSONObject();
		json.put("numero", numero);
		json.put("saldoInicial", saldoInicial);

		try {
			Response res = Jsoup.connect(BASEURL + "/compte").method(Method.POST).requestBody(json.toString())
					.ignoreContentType(true).ignoreHttpErrors(true).execute();

			if (res.statusCode() != 201) {
				if (res.statusCode() == 409) {
					throw new DuplicateException(" ERROR 409:aquest compte ja existeix ");
				}
				throw new IOException("something went bad! status code is " + res.statusCode());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public int transferir(int origen, int desti, BigDecimal quantitat) throws NotFoundException, SenseFonsException {
		int id = 0;
		JSONObject json;
		json = new JSONObject();
		json.put("origen", origen);
		json.put("desti", desti);
		json.put("quantitat", quantitat);

		try {
			Response res = Jsoup.connect(BASEURL + "/transferir").method(Method.PUT).requestBody(json.toString())
					.ignoreContentType(true).ignoreHttpErrors(true).execute();
			if (res.statusCode() != 200) {
				if (res.statusCode() == 404) {
					throw new NotFoundException("ERROR 404: not found ");
				}
				if (res.statusCode() == 409) {
					throw new SenseFonsException("ERROR 409: sense fons ");
				}
				throw new IOException("something went bad! status code is " + res.statusCode());
			}
			id = new JSONObject(res.body()).getInt("id");
			return id;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public List<Moviment> getMoviments(int origen) {
		ArrayList<Moviment> list = new ArrayList<Moviment>();
	
		int origenRes;
		int destiRes;
		BigDecimal quantitatRes;
		JSONArray jarray ;
		try {
			Response res = Jsoup.connect(BASEURL + "/moviments/" + origen).method(Method.GET).ignoreContentType(true)
					.ignoreHttpErrors(true).execute();

			if (res.statusCode() != 200) {
				throw new IOException("something went bad! status code is " + res.statusCode());
			}
			jarray = new JSONArray(res.body());
			System.out.println(jarray);
			for (int i = 0; i < jarray.length(); i++) {
				Moviment m = new Moviment();
				JSONObject jo = jarray.getJSONObject(i);
				origenRes = jo.getInt("origen");
				destiRes = jo.getInt("desti");
				quantitatRes = jo.getBigDecimal("quantitat");

				m.origen = origenRes;
				m.desti = destiRes;
				m.quantitat = quantitatRes;

				list.add(m);
				
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public BigDecimal getSaldo(int compte) throws NotFoundException {
		BigDecimal resSaldo = null;

		try {
			Response res = Jsoup.connect(BASEURL + "/saldo/" + compte).method(Method.GET).ignoreContentType(true)
					.ignoreHttpErrors(true).execute();

			if (res.statusCode() != 200) {
				if (res.statusCode() == 404) {
					throw new NotFoundException("ERROR 404: Not Found");
				}
				throw new IOException("something went bad! status code is " + res.statusCode());
			}
			resSaldo = new JSONObject(res.body()).getBigDecimal("saldo");
			return resSaldo;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
