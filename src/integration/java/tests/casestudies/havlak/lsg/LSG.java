package tests.casestudies.havlak.lsg;

import java.util.*;

import tests.casestudies.havlak.cfg.*;

/**
 * LoopStructureGraph
 *
 * Maintain loop structure for a given CFG.
 *
 * Two values are maintained for this loop graph, depth, and nesting level.
 * For example:
 *
 * loop        nesting level    depth
 *----------------------------------------
 * loop-0      2                0
 *   loop-1    1                1
 *   loop-3    1                1
 *     loop-2  0                2
 */
public class LSG {
	  public LSG() {
	    loopCounter = 0;
	    loops = new ArrayList<SimpleLoop>();
	    root = new SimpleLoop();
	    root.setNestingLevel(0);
	    root.setCounter(loopCounter++);
	    addLoop(root);
	    
	  }

	  public SimpleLoop createNewLoop() {
	    SimpleLoop loop = new SimpleLoop();
	    loop.setCounter(loopCounter++);
	    return loop;
	  }

	  public void addLoop(SimpleLoop loop) {
	    loops.add(loop);
	  }

	  public void dump() {
	    dumpRec(root, 0);
	  }

	  private void dumpRec(SimpleLoop loop, int indent) {
	    // Simplified for readability purposes.
	    loop.dump(indent);

	    for (SimpleLoop liter : loop.getChildren())
	      dumpRec(liter,  indent + 1);
	  }

	  public void calculateNestingLevel() {
	    // link up all 1st level loops to artificial root node.
	    for (SimpleLoop liter : loops) {
	      if (liter.isRoot()) {
	        continue;
	      }
	      if (liter.getParent() == null) {
	        liter.setParent(root);
	      }
	    }

	    // recursively traverse the tree and assign levels.
	    calculateNestingLevelRec(root, 0);
	  }

	  public void calculateNestingLevelRec(SimpleLoop loop, int depth) {
	    loop.setDepthLevel(depth);
	    for (SimpleLoop liter : loop.getChildren()) {
	      calculateNestingLevelRec(liter, depth + 1);

	      loop.setNestingLevel(Math.max(loop.getNestingLevel(),
	                                    1 + liter.getNestingLevel()));
	    }
	  }

	  public int getNumLoops() {
	    return loops.size();
	  }
	  public SimpleLoop getRoot() {
	    return root;
	  }

	  private SimpleLoop       root;
	  private List<SimpleLoop> loops;
	  private int              loopCounter;
	};