package m09uf3.impl.bancapi;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import lib.db.DuplicateException;
import lib.db.NotFoundException;
import lib.http.HTTPProcessor;
import lib.http.HTTPRequest;
import lib.http.HTTPResponse;
import lib.http.HTTPUtils;
import lib.http.SocketHTTPServer;
import m03uf6.prob.banc.BancDAO;
import m03uf6.prob.banc.Moviment;
import m03uf6.prob.banc.SenseFonsException;

public class BancAPIProcessorImpl implements HTTPProcessor {
	static final Logger LOGGER = Logger.getLogger(BancAPIProcessorImpl.class.getName());
	private BancDAO banc;


	public BancAPIProcessorImpl(BancDAO banc) {
		this.banc = banc;
	}

	@Override
	public void process(HTTPRequest req, HTTPResponse res) {

		switch (req.getMethod()) {
		case "POST":
			if ("/compte".equals(req.getPath())) {
				String body = HTTPUtils.readToString(req.getReader());
				JSONObject jin = new JSONObject(body);
				int num = jin.getInt("numero");
				BigDecimal saldo = jin.getBigDecimal("saldoInicial");
				try {

					banc.nouCompte(num, saldo);
					processf(res, "201 Created", null);
				} catch (DuplicateException e) {
					processf(res, "409 Conflict", null);
				}

			} else {
				processf(res, "400 Bad Request", null);
			}
			break;

		case "PUT":
			if ("/transferir".equals(req.getPath())) {
				String body = HTTPUtils.readToString(req.getReader());
				JSONObject jin = new JSONObject(body);

				int origen = jin.getInt("origen");
				int desti = jin.getInt("desti");
				BigDecimal quantitat = jin.getBigDecimal("quantitat");

				try {
					int id = banc.transferir(origen, desti, quantitat);
					JSONObject jout = new JSONObject().put("id", id);
					processf(res, "200 Ok", jout.toString());
				} catch (SenseFonsException e) {

					processf(res, "409 Conflict", null);

				} catch (NotFoundException e) {

					processf(res, "404 Not Found", null);
				}

			} else {
				processf(res, "400 Bad Request", null);
			}
			break;
		case "GET":
			String[] params = req.getPath().split("/");
			if (params.length < 3 || 3 > params.length) {
				processf(res, "400 Bad Request", null);
			}

			String metodo = params[1];
			String idstr = params[2];
			int id = Integer.parseInt(idstr);

			if (metodo.equals("saldo")) {
				try {
					BigDecimal saldoActual = banc.getSaldo(id);
					JSONObject jout = new JSONObject().put("saldo", saldoActual);
					processf(res, "200 Ok", jout.toString());

				} catch (NotFoundException e) {
					processf(res, "404 Not Found", null);

				}
			} else if (metodo.equals("moviments")) {
				List<Moviment> moviments = new ArrayList<>();
				moviments = banc.getMoviments(id);

				JSONArray jArray = new JSONArray();
				for (Moviment m : moviments) {
					JSONObject move = new JSONObject();
					move.put("origen", m.origen);
					move.put("desti", m.desti);
					move.put("quantitat", m.quantitat);
					jArray.put(move);
				}
				

				processf(res, "200 Ok", jArray.toString());

			} else {
				processf(res, "400 Bad Request", null);
			}

			break;

		default:
			processf(res, "400 Bad Request", null);
		}
	}

	private void processf(HTTPResponse res, String status, String jsonStr) {

		PrintWriter out = res.getWriter();

		out.println("HTTP/1.1 " + status);
		out.println("Server: omlo22");
		out.println("Date: " + HTTPUtils.toHTTPDate(new Date()));
		if (jsonStr != null) {
			out.println("Content-type: application/json; charset=" + SocketHTTPServer.CHARSET);
			out.println("Content-length: " + jsonStr.getBytes(SocketHTTPServer.CHARSET).length);
		}
		out.println(); // blank line!
		if (jsonStr != null) {
			out.println(jsonStr);
		}
	}

}
