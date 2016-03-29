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
import janala.solvers.counters.FilteredTreeBuilderStrategy;
import janala.solvers.counters.trees.TreePolicy;
import janala.solvers.counters.trees.UCB1TreePolicy;
import janala.solvers.counters.trees.UCB1TreePolicy.FPU_TYPE;

public class Config {
  public static final String mainClass = System.getProperty("janala.mainClass", null);
  public static final int iteration = Integer.getInteger("janala.iteration", 0);
  public static final String propFile = System.getProperty("janala.conf", "catg.conf");
  public static final Config instance = new Config();

  public boolean isTest;
  public boolean verbose;
  public boolean printTrace;
  public boolean printConstraints;
  public String analysisClass;
  public String traceFileName;
  public String traceAuxFileName;
  public String history;
  public String coverage;
  public String inputs;
  public String yicesCommand;
  public String formulaFile;
  public String testLog;
  public String cvc3Command;
  public String cvc4Command;
  public String[] excludeList;
  public String[] includeList;
  private String loggerClass;
  private String solver;
  private String counter;
  public String policy;
  public String strategy;
  public int maxStringLength;
  public int pathId;
  public boolean printFormulaAndSolutions;
  public String scopeBeginMarker;
  public String scopeEndMarker;
  public int scopeBeginSymbol = -1;
  public int scopeEndSymbol = -2;
  public String test;
  public String oldStates;
  public boolean printHistory;
  public String symtreeFile;
  public long seed;
  public Range<Long> defaultRange;
  public String rngFile;
  public boolean printDomainCoverage;
  public String bfsFile;
  private boolean useDomainBoundConstraint;
  private boolean filterConstraints;

  public String isccPath;
  public String lattePath;
  public String omegaPath;
  public String countersWorkingDirectory;
  public String countersSecondLevelCachePath;
  public String remoteCounterAddress;
  public int remoteCounterPort;

  public boolean ucbUseConstant;
  public FPU_TYPE ucbFpuType;
  public String setFile;
  public String inputElementsFile;

  public Config() {
    try {
      Properties properties = new Properties();
      properties.load(new FileInputStream(propFile));

      isTest = properties.getProperty("catg.isInternalTestMode", "false").equals("true");
      verbose = properties.getProperty("catg.isVerbose", "false").equals("true");
      printHistory = properties.getProperty("catg.isPrintHistory", "false").equals("true");
      printTrace = properties.getProperty("catg.isPrintTrace", "false").equals("true");
      printConstraints = properties.getProperty("catg.isPrintConstraints", "false").equals("true");
      printFormulaAndSolutions = properties.getProperty("catg.isPrintFormulaAndSolutions", "false").equals("true");
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
      policy = properties.getProperty("catg.policyClass", "janala.solvers.counters.trees.QuantolicTreePolicy");
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
      printDomainCoverage = properties.getProperty("catg.printDomainCoverage", "false").trim().equals("true");
      useDomainBoundConstraint = Boolean
          .parseBoolean(properties.getProperty("catg.solver.useDomainBoundConstraint", "false").trim());
      filterConstraints = Boolean.parseBoolean(properties.getProperty("catg.solver.filterConstraints", "false").trim());

      isccPath = properties.getProperty("counters.isccPath");
      countersWorkingDirectory = properties.getProperty("counters.counterWorkingDirectory");
      countersSecondLevelCachePath = properties.getProperty("counters.secondLevelCachePath");
      lattePath = properties.getProperty("counters.lattePath");
      omegaPath = properties.getProperty("counters.omegaPath");

      remoteCounterAddress = properties.getProperty("counters.remoteAddress", "none");
      remoteCounterPort = Integer.parseInt(properties.getProperty("counters.remotePort", "0"));

      ucbUseConstant = Boolean.parseBoolean(properties.getProperty("mcts.ucb1.useConstant", "true"));
      ucbFpuType = FPU_TYPE.valueOf(properties.getProperty("mcts.ucb1.fpu", "ONE").toUpperCase().trim());
      setFile = properties.getProperty("catg.solver.pathfilter.setFile", "coveredPaths.ser");
      inputElementsFile = properties.getProperty("catg.solver.pathfilter.inputElementsFile", "inputElements.ser");
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
      Class<?> solverClass = Class.forName(strategy);
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
    } catch (IOException e) {
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

  public TreePolicy getPolicy() {
    try {
      if (policy.equals(UCB1TreePolicy.class.getName())) {
        return new UCB1TreePolicy(ucbUseConstant, ucbFpuType);
      } else {
        Class<?> policyClass = Class.forName(policy);
        TreePolicy ret = (TreePolicy) policyClass.newInstance();
        return ret;
      }
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
}