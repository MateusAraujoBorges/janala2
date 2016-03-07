package tests.casestudies.havlak.cfg;

import java.util.*;





/**
 * class CFG
 *
 * CFG maintains a list of nodes, plus a start node.
 * That's it.
 */
public class CFG {
	  public CFG() {
	    startNode = null;
	    basicBlockMap = new HashMap<Integer, BasicBlock>();
	    edgeList = new ArrayList<BasicBlockEdge>();
	  }

	  public BasicBlock createNode(int name) {
	    BasicBlock node;
	    if (!basicBlockMap.containsKey(name)) {
	      node = new BasicBlock(name);
	      basicBlockMap.put(name, node);
	    } else {
	      node = basicBlockMap.get(name);
	    }

	    if (getNumNodes() == 1) {
	      startNode = node;
	    }

	    return node;
	  }

	  public void dump() {
	    for (BasicBlock bb : basicBlockMap.values()) {
	      bb.dump();
	    }
	  }

	  public void addEdge(BasicBlockEdge edge) {
	    edgeList.add(edge);
	  }

	  public int getNumNodes() {
	    return basicBlockMap.size();
	  }

	  public BasicBlock getStartBasicBlock() {
	    return startNode;
	  }

	  public BasicBlock getDst(BasicBlockEdge edge) {
	    return edge.getDst();
	  }

	  public BasicBlock getSrc(BasicBlockEdge edge) {
	    return edge.getSrc();
	    
	  }

	  public Map<Integer, BasicBlock> getBasicBlocks() {
	    return basicBlockMap;
	  }

	  private Map<Integer, BasicBlock>  basicBlockMap;
	  private BasicBlock                startNode;
	  private List<BasicBlockEdge>      edgeList;
	};