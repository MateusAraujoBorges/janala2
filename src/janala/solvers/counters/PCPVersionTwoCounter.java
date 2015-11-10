package janala.solvers.counters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import janala.config.Config;
import janala.interpreters.Constraint;
import janala.interpreters.IntValue;
import janala.interpreters.LongValue;
import janala.interpreters.Value;
import janala.solvers.InputElement;
import janala.utils.MyLogger;
import name.filieri.antonio.jpf.utils.BigRational;

public class PCPVersionTwoCounter implements Counter {

	private final static Logger logger = MyLogger.getLogger(PCPVersionTwoCounter.class.getName());
	private final ConnectionManager conn;

	public PCPVersionTwoCounter() throws IOException {
		this(Config.instance.remoteCounterAddress,Config.instance.remoteCounterPort);
	}
	
	public PCPVersionTwoCounter(String address, int port) throws IOException {
		conn = new PCPConnectionManager(address,port); 
	}
	
	public PCPVersionTwoCounter(ConnectionManager conn) {
		this.conn = conn;
	}
	
	@Override
	public BigRational probabilityOf(List<Constraint> constraints, List<InputElement> inputs) {
		StringBuilder query = new StringBuilder();
		//prepare domain
		for (InputElement input : inputs) {
			String dec = processInputElement(input);
			query.append(dec);
			query.append('\n');
		}
		
		query.append("(assert ");
		for (Constraint constraint : constraints) {
			String pcpConstraint = PCPVisitor.toPCPFormat(constraint);
			query.append(pcpConstraint);
			query.append(' ');
		}
		query.delete(query.length() - 1, query.length());
		query.append(")\n");
		
		query.append("(count)\n");
		return conn.count(query.toString());
	}

	public static String processInputElement(InputElement input) {
		StringBuilder sb = new StringBuilder();
		sb.append("(declare-var x");
		sb.append(input.symbol);
		String type = toPcpType(input.value);
		sb.append(" (");
		sb.append(type);
		sb.append(' ');
		sb.append(input.range.lowerEndpoint());
		sb.append(' ');
		sb.append(input.range.upperEndpoint());
		sb.append("))");
		return sb.toString();
	}

	@Override
	public void shutdown() {
		try {
			conn.close();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error shutting down connection with count server", e);
		}
	}
	
	private static String toPcpType(Value value) {
		if (value instanceof IntValue) {
			return "Int";
		} else if (value instanceof LongValue) {
			return "Long";
		} else {
			throw new RuntimeException("Unsupported type (for now): " + value.getClass());
		}
	}

	public interface ConnectionManager {
		public BigRational count(String query);
		public void close() throws IOException;
	}
	
	public static class PCPConnectionManager implements ConnectionManager {
		private Socket countServer;
		private OutputStreamWriter out;
		private BufferedReader in;

		public PCPConnectionManager(String serverAddress, int serverPort) throws IOException {
			// Initialize connection
			this.countServer = new Socket(serverAddress, serverPort);
			this.out = new OutputStreamWriter(countServer.getOutputStream(), "UTF-8");
			this.in = new BufferedReader(new InputStreamReader(countServer.getInputStream()));
		}

		@Override
		public BigRational count(String query) {
			try {
				logger.log(Level.INFO, "[pcpV2] query sent: \n	" + query.replaceAll("\n", "\n	"));
				out.write(query,0,query.length());
				out.flush();
				String response = in.readLine();
				if (response == null || response.isEmpty()) {
					throw new RuntimeException("The count server returned an null/empty string");
				} else if (response.contains("error")) {
					throw new RuntimeException("The count server returned an error: " + response);
				} else {
					String[] toks = response.split(" ");
					BigRational prob = new BigRational(toks[0].substring(1));
					BigRational var = new BigRational(toks[1].substring(0,toks[1].length()-1));
					logger.log(Level.INFO, "[pcpV2] successfull count received: pr=" + prob + " var=" + var);
					return new BigRational(prob);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void close() throws IOException {
			this.out.close();
			this.in.close();
			this.countServer.close();
		}
	}
}