/*
 * Copyright (c) 2012, NTT Multimedia Communications Laboratories, Inc. and Koushik Sen
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Author: Koushik Sen (ksen@cs.berkeley.edu)
 */

package janala.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.common.collect.Range;

import janala.logger.Logger;
import janala.solvers.Solver;
import janala.solvers.Strategy;
import janala.solvers.counters.BoundedDomainSolverWrapper;
import janala.solvers.counters.Counter;
import janala.solvers.counters.DomainCoverageStrategyWrapper;

public class Config {
	public static final String mainClass = System.getProperty("janala.mainClass", null);
	public static final int iteration = Integer.getInteger("janala.iteration", 0);
	public static final String propFile = System.getProperty("janala.conf", "catg.conf");
	public static final Config instance = new Config();

	public final boolean isTest;
	public final boolean verbose;
	public final boolean printTrace;
	public final boolean printConstraints;
	public final String analysisClass;
	public final String traceFileName;
	public final String traceAuxFileName;
	public final String history;
	public final String coverage;
	public final String inputs;
	public final String yicesCommand;
	public final String formulaFile;
	public final String testLog;
	public final String cvc3Command;
	public final String cvc4Command;
	public final String[] excludeList;
	public final String[] includeList;
	private String loggerClass;
	private String solver;
	private String counter;
	public final String strategy;
	public final int maxStringLength;
	public final int pathId;
	public final boolean printFormulaAndSolutions;
	public final String scopeBeginMarker;
	public final String scopeEndMarker;
	public final int scopeBeginSymbol = -1;
	public final int scopeEndSymbol = -2;
	public final String test;
	public final TestChecker testChecker;
	public final String oldStates;
	public final boolean printHistory;
	public final String symtreeFile;
	public final long seed;
	public final Range<Long> defaultRange;
	public final String rngFile;
	public final boolean printDomainCoverage;
	public final String bfsFile;
	private boolean useDomainBoundConstraint;

	public final String isccPath;
	public final String lattePath;
	public final String omegaPath;
	public final String countersWorkingDirectory;
	public final String countersSecondLevelCachePath;
	public final String  remoteCounterAddress;
	public final int remoteCounterPort;

	public Config() {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(propFile));

			isTest = properties.getProperty("catg.isInternalTestMode", "false").equals("true");
			verbose = properties.getProperty("catg.isVerbose", "false").equals("true");
			printHistory = properties.getProperty("catg.isPrintHistory", "false").equals("true");
			printTrace = properties.getProperty("catg.isPrintTrace", "false").equals("true");
			printConstraints = properties.getProperty("catg.isPrintConstraints", "false").equals("true");
			printFormulaAndSolutions = properties.getProperty("catg.isPrintFormulaAndSolutions", "false")
					.equals("true");
			traceFileName = properties.getProperty("catg.traceFile", "trace");
			traceAuxFileName = properties.getProperty("catg.auxTraceFile", "trace.aux");
			history = properties.getProperty("catg.historyFile", "history");
			coverage = properties.getProperty("catg.coverageFile", "coverage.catg");
			inputs = properties.getProperty("catg.inputsFile", "inputs");
			yicesCommand = properties.getProperty("catg.yicesCommand", "yices");
			formulaFile = properties.getProperty("catg.formulaFile", "formula");
			testLog = properties.getProperty("catg.testLogFile", "test.log");
			cvc3Command = properties.getProperty("catg.cvc3Command", "cvc3");
			cvc4Command = properties.getProperty("catg.cvc4Command", "cvc4");
			loggerClass = System.getProperty("janala.loggerClass", "janala.logger.FileLogger");
			analysisClass = properties.getProperty("catg.analysisClass", "janala.logger.DJVM").replace('.', '/');
			solver = properties.getProperty("catg.solverClass", "janala.solvers.YicesSolver2");
			counter = properties.getProperty("catg.countingClass", "janala.solvers.counters.PCPCounter");
			strategy = properties.getProperty("catg.strategyClass", "janala.solvers.DFSStrategy");
			excludeList = properties.getProperty("catg.excludeList", "").split(",");
			includeList = properties.getProperty("catg.includeList", "catg.CATG").split(",");
			maxStringLength = Integer.parseInt(properties.getProperty("catg.maxStringLength", "30"));
			pathId = Integer.parseInt(properties.getProperty("catg.pathId", "1"));
			scopeBeginMarker = properties.getProperty("catg.scopeBeginMarker", "begin$$$$");
			scopeEndMarker = properties.getProperty("catg.scopeEndMarker", "end$$$$");
			symtreeFile = properties.getProperty("catg.symbolicTreeFile", "symtree.ser");
			seed = Long.parseLong(properties.getProperty("catg.seed", "-388213196"));
			rngFile = properties.getProperty("catg.rngFile", "rng.ser");
			bfsFile = properties.getProperty("catg.bfs.mapfile", "bfsmap.ser");

			oldStates = properties.getProperty("catg.oldStatesFile", "oldStates");
			test = System.getProperty("catg.test", properties.getProperty("catg.test", "test"));
			String testCheckingClass = System.getProperty("catg.testCheckingClass",
					properties.getProperty("catg.testCheckingClass", "janala.config.DefaultTestCheckerImpl"));
			testChecker = (TestChecker) loadClass(testCheckingClass);
			printDomainCoverage = properties.getProperty("catg.printDomainCoverage", "false").trim().equals("true");
			useDomainBoundConstraint = properties.getProperty("catg.solver.useDomainBoundConstraint", "false").trim()
					.equals("true");

			isccPath = properties.getProperty("counters.isccPath");
			countersWorkingDirectory = properties.getProperty("counters.counterWorkingDirectory");
			countersSecondLevelCachePath = properties.getProperty("counters.secondLevelCachePath");
			lattePath = properties.getProperty("counters.lattePath");
			omegaPath = properties.getProperty("counters.omegaPath");
			
			remoteCounterAddress = properties.getProperty("counters.remoteAddress","none");
			remoteCounterPort = Integer.parseInt(properties.getProperty("counters.remotePort","0"));

			String rangeStr = properties.getProperty("catg.defaultRange", "-1000,1000");
			long lo = Long.parseLong(rangeStr.split(",")[0]);
			long hi = Long.parseLong(rangeStr.split(",")[1]);
			defaultRange = Range.closed(lo, hi);

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Object loadClass(String cName) {
		try {
			Class clazz = Class.forName(cName);
			Object ret = clazz.newInstance();
			return ret;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public Logger getLogger() {
		try {
			Class solverClass = Class.forName(loggerClass);
			Logger ret = (Logger) solverClass.newInstance();
			return ret;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public Solver getSolver() {
		try {
			Class solverClass = Class.forName(solver);
			Solver ret = (Solver) solverClass.newInstance();
			if (useDomainBoundConstraint) {
				ret = new BoundedDomainSolverWrapper(ret);
			}
			return ret;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public Strategy getStrategy() {
		try {
			Class solverClass = Class.forName(strategy);
			Strategy ret = (Strategy) solverClass.newInstance();
			if (printDomainCoverage) {
				ret = new DomainCoverageStrategyWrapper(ret);
			}
			return ret;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public Counter getCounter() {
		try {
			Class counterClass = Class.forName(counter);
			Counter ret = (Counter) counterClass.newInstance();
			return ret;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public Range<Long> getDefaultRange() {
		return defaultRange;
	}
}
