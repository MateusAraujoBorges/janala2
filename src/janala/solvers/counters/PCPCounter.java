package janala.solvers.counters;

import java.util.List;
import java.util.logging.Logger;

import janala.interpreters.Constraint;
import janala.solvers.History;
import janala.solvers.InputElement;
import janala.utils.MyLogger;
import name.filieri.antonio.jpf.analysis.SequentialAnalyzerBarvinok;
import name.filieri.antonio.jpf.analysis.exceptions.AnalysisException;
import name.filieri.antonio.jpf.domain.Domain;
import name.filieri.antonio.jpf.domain.UsageProfile;
import name.filieri.antonio.jpf.domain.exceptions.InvalidUsageProfileException;
import name.filieri.antonio.jpf.latte.LatteException;
import name.filieri.antonio.jpf.omega.exceptions.OmegaException;
import name.filieri.antonio.jpf.utils.BigRational;
import name.filieri.antonio.jpf.utils.Configuration;

public class PCPCounter implements Counter {

	private final static Logger logger = MyLogger.getLogger(PCPCounter.class.getName());
	
	@Override
	public BigRational count(List<Constraint> constraints, List<InputElement> inputs) {
		
		Domain.Builder domainBuilder = new Domain.Builder();
		StringBuilder scenario = new StringBuilder();
		for (InputElement input : inputs) {
			long lo = input.range.lowerEndpoint();
			long hi = input.range.upperEndpoint();
			String var = "x"+input.symbol;
			domainBuilder.addVariable(var, lo, hi);
			scenario.append(var + "<=" + hi + "&&" + var + ">=" + lo + "&&");
		}
		scenario.delete(scenario.length()-2, scenario.length());
		
		Domain domain = domainBuilder.build();
		logger.info(domain.asProblem().toString());

		UsageProfile.Builder usageProfileBuilder = new UsageProfile.Builder();
		usageProfileBuilder.addScenario(scenario.toString(), 1);
		UsageProfile usageProfile;
		try {
			usageProfile = usageProfileBuilder.build();
			StringBuilder pc = new StringBuilder();
			for (Constraint cons : constraints) {
				pc.append(cons.toMathString() + "&&");
			}
			pc.delete(pc.length()-2, pc.length());
			
			Configuration configuration = new Configuration();
			configuration.setTemporaryDirectory("/tmp/");
			configuration.setIsccExecutablePath("/home/mateus/bin/iscc");
			SequentialAnalyzerBarvinok analyzer = new SequentialAnalyzerBarvinok(configuration, domain, usageProfile, 1);
			
			BigRational probability = analyzer.analyzeSpfPC(pc.toString());
			System.out.println(probability);
			return probability;
			
		} catch (InvalidUsageProfileException e) {
			logger.info("Problem when counting constraint: " + e.getMessage());
			e.printStackTrace();
		} catch (LatteException e) {
			logger.info("Problem when counting constraint: " + e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			logger.info("Problem when counting constraint: " + e.getMessage());
			e.printStackTrace();
		} catch (OmegaException e) {
			logger.info("Problem when counting constraint: " + e.getMessage());
			e.printStackTrace();
		} catch (AnalysisException e) {
			logger.info("Problem when counting constraint: " + e.getMessage());
			e.printStackTrace();
		}

		return BigRational.MINUS_ONE;
	}

}
