package tests.casestudies.havlak.cfg;
import java.util.*;

import tests.casestudies.havlak.cfg.BasicBlock;


/**
 * class BasicBlock
 *
 * BasicBlock only maintains a vector of in-edges and
 * a vector of out-edges.
 */
public class BasicBlock {

	  static int numBasicBlocks = 0;

	  public static int getNumBasicBlocks() {
	    return numBasicBlocks;
	  }

	  public BasicBlock(int name) {
	    this.name = name;
	    inEdges   = new ArrayList<BasicBlock>(2);
	    outEdges  = new ArrayList<BasicBlock>(2);
	    ++numBasicBlocks;
	  }

	  public void dump() {
	    System.out.format("BB#%03d: ", getName());
	    if (inEdges.size() > 0) {
	      System.out.format("in : ");
	      for (BasicBlock bb : inEdges) {
	        System.out.format("BB#%03d ", bb.getName());
	      }
	    }
	    if (outEdges.size() > 0) {
	      System.out.format("out: ");
	      for (BasicBlock bb : outEdges) {
	        System.out.format("BB#%03d ", bb.getName());
	      }
	    }
	    System.out.println();
	    
	  }

	  public int getName() {
	    return name;
	  }

	  public List<BasicBlock> getInEdges() {
	    return inEdges;
	  }
	  public List<BasicBlock> getOutEdges() {
	    return outEdges;
	  }

	  public int getNumPred() {
	    return inEdges.size();
	  }
	  public int getNumSucc() {
	    return outEdges.size();
	  }

	  public void addOutEdge(BasicBlock to) {
	    outEdges.add(to);
	  }
	  public void addInEdge(BasicBlock from) {
	    inEdges.add(from);
	  }

	  private List<BasicBlock> inEdges, outEdges;
	  private int name;
	};