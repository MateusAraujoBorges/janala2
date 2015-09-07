package janala.solvers.counters;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import janala.config.Config;
import janala.interpreters.Constraint;
import janala.interpreters.IntValue;
import janala.interpreters.SymbolicFalseConstraint;
import janala.solvers.InputElement;
import janala.utils.MyLogger;
import name.filieri.antonio.jpf.analysis.SequentialAnalyzerBarvinok;
import name.filieri.antonio.jpf.analysis.exceptions.AnalysisException;
import name.filieri.antonio.jpf.domain.Domain;
import name.filieri.antonio.jpf.domain.Problem;
import name.filieri.antonio.jpf.domain.UsageProfile;
import name.filieri.antonio.jpf.domain.exceptions.InvalidUsageProfileException;
import name.filieri.antonio.jpf.latte.LatteException;
import name.filieri.antonio.jpf.omega.exceptions.OmegaException;
import name.filieri.antonio.jpf.utils.BigRational;
import name.filieri.antonio.jpf.utils.Configuration;

public class PCPCounter implements Counter {

	private final static Logger logger = MyLogger.getLogger(PCPCounter.class.getName());
	
	private LoadingCache<Problem, BigRational> executionCache = null;
	
	public PCPCounter(){
		//Just to force the initialization of the caches
		List<Constraint> falseConstraint = ImmutableList.<Constraint>of(new SymbolicFalseConstraint());
		List<InputElement> anyElement = ImmutableList.of(new InputElement(1, new IntValue(0)));
		probabilityOf(falseConstraint,anyElement);
	}
	
	
	@Override
	public BigRational probabilityOf(List<Constraint> constraints, List<InputElement> inputs) {
		
		//List<InputElement> filteredInputs = filterInputs(constraints,inputs);
		
		Domain.Builder domainBuilder = new Domain.Builder();
		for (InputElement input : inputs) {
			long lo = input.range.lowerEndpoint();
			long hi = input.range.upperEndpoint();
			String var = "x"+input.symbol;
			domainBuilder.addVariable(var, lo, hi);
		}
		
		Domain domain = domainBuilder.build();
		logger.info(domain.asProblem().toString());

		UsageProfile.Builder usageProfileBuilder = new UsageProfile.Builder();
		usageProfileBuilder.addScenario(domain.asProblem(), BigRational.ONE);
		UsageProfile usageProfile;
		try {
			usageProfile = usageProfileBuilder.build();
			StringBuilder pc = new StringBuilder();
			for (Constraint cons : constraints) {
				pc.append(cons.toMathString() + "&&");
			}
			pc.delete(pc.length()-2, pc.length());
			
			Configuration configuration = new Configuration();
			configuration.setTemporaryDirectory(Config.instance.countersWorkingDirectory);
			configuration.setIsccExecutablePath(Config.instance.isccPath);
			configuration.setSecondLevelCachePath(Config.instance.countersSecondLevelCachePath);
			SequentialAnalyzerBarvinok analyzer = new SequentialAnalyzerBarvinok(configuration, domain, usageProfile, 1);
			
			if(this.executionCache!=null){
				analyzer.setBarvinokCache(executionCache);
			}
			
			BigRational probability = analyzer.analyzeSpfPC(pc.toString());
			this.executionCache=analyzer.getBarvinokCache();
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

	/**
	 * Collect the input variables in {@code inputs} present in {@code constraints}. 
	 * TODO This should be done with a visitor in the future, but
	 * @param constraints
	 * @param inputs
	 * @return
	 */
	
	private List<InputElement> filterInputs(List<Constraint> constraints, List<InputElement> inputs) {
		Set<Integer> varIDsInConstraints = VarCollectorVisitor.collectVariableIDs(constraints);
		List<InputElement> varsInConstraints = Lists.newArrayList();
		for (InputElement input : inputs) {
			if (varIDsInConstraints.contains(input.symbol)) {
				varsInConstraints.add(input);
			}
		}
		
		return varsInConstraints;
	}

}
