package tests.casestudies.havlak.cfg;


/**
 * class BasicBlockEdga
 *
 * These data structures are stubbed out to make the code below easier
 * to review.
 *
 * BasicBlockEdge only maintains two pointers to BasicBlocks.
 */
public class BasicBlockEdge {
	  public BasicBlockEdge(CFG cfg, int fromName, int toName) {
	    from = cfg.createNode(fromName);

	    to   = cfg.createNode(toName);

	    from.addOutEdge(to);
	    to.addInEdge(from);

	    cfg.addEdge(this);
	  }

	  public  BasicBlock getSrc() { return from; }
	  public  BasicBlock getDst() { return to; }

	  private BasicBlock from, to;
	};