package tests.casestudies.havlak.looptester;

import tests.casestudies.havlak.cfg.BasicBlock;
import tests.casestudies.havlak.cfg.BasicBlockEdge;
import tests.casestudies.havlak.cfg.CFG;
import tests.casestudies.havlak.havlakloopfinder.HavlakLoopFinder;
import tests.casestudies.havlak.lsg.LSG;

class LoopTesterApp {
  public LoopTesterApp() {
    cfg = new CFG();
    lsg = new LSG();
    root = cfg.createNode(0);
  }

  // Create 4 basic blocks, corresponding to and if/then/else clause
  // with a CFG that looks like a diamond
  public int buildDiamond(int start) {
    int bb0 = start;
    new BasicBlockEdge(cfg, bb0, bb0 + 1);
    new BasicBlockEdge(cfg, bb0, bb0 + 2);
    new BasicBlockEdge(cfg, bb0 + 1, bb0 + 3);
    new BasicBlockEdge(cfg, bb0 + 2, bb0 + 3);

    return bb0 + 3;
  }

  // Connect two existing nodes
  public void buildConnect(int start, int end) {
    new BasicBlockEdge(cfg, start, end);
  }

  // Form a straight connected sequence of n basic blocks
  public int buildStraight(int start, int n) {
    for (int i = 0; i < n; i++) {
      buildConnect(start + i, start + i + 1);
    }
    return start + n;
  }

  // Construct a simple loop with two diamonds in it
  public int buildBaseLoop(int from) {
    int header = buildStraight(from, 1);
    int diamond1 = buildDiamond(header);
    int d11 = buildStraight(diamond1, 1);
    int diamond2 = buildDiamond(d11);
    int footer = buildStraight(diamond2, 1);
    buildConnect(diamond2, d11);
    buildConnect(diamond1, header);

    buildConnect(footer, from);
    footer = buildStraight(footer, 1);
    return footer;
  }

  public void getMem() {
    Runtime runtime = Runtime.getRuntime();
    long val = ((runtime.totalMemory() / (1024*3)));
    System.out.println("  Total Memory: " + val + " KB");
  }
  // to VM if you want to be very carefully: else test
  //-server -Xss15500k -Xmx1G 
  
  public static void main(String[] args) {
   System.out.println("Welcome to LoopTesterApp, Java edition");
   
    System.out.println("Constructing App...");
    LoopTesterApp app = new LoopTesterApp();
    app.getMem();

    System.out.println("Constructing Simple CFG...");
    app.cfg.createNode(0);
    app.buildBaseLoop(0);
    app.cfg.createNode(1);
    new BasicBlockEdge(app.cfg, 0, 2);

    System.out.println(" dummy loops");
    for (int dummyloop = 0; dummyloop < 15; dummyloop++) {
    	  System.out.println(dummyloop);
      HavlakLoopFinder finder = new HavlakLoopFinder(app.cfg, app.lsg);
      finder.findLoops();
    }

    System.out.println("Constructing CFG...");
     int n = 2;

   for (int parlooptrees = 0; parlooptrees <2; parlooptrees++) {
      app.cfg.createNode(n + 1);
     // System.out.println("parlooptrees"+parlooptrees);

      app.buildConnect(2, n + 1);

      n = n + 1;

      for (int i = 0; i < 2; i++) {
        int top = n;
        n = app.buildStraight(n, 1);
      //  System.out.println("buildStraight "+i);

        for (int j = 0; j < 25; j++) {
          n = app.buildBaseLoop(n);
        //  System.out.println("BaseLoop "+j);

        }
        int bottom = app.buildStraight(n, 1);
        app.buildConnect(n, top);
        n = bottom;
      }
      app.buildConnect(n, 1);
     }

    app.getMem();
    System.out.format("Performing Loop Recognition\n1 Iteration\n");
    HavlakLoopFinder finder = new HavlakLoopFinder(app.cfg, app.lsg);
    finder.findLoops();
   /* app.getMem();

    System.out.println("Another 50 iterations...");
    for (int i = 0; i < 2; i++) {
      System.out.format(".");
      HavlakLoopFinder finder2 = new HavlakLoopFinder(app.cfg, new LSG());
      finder2.findLoops();
    }*/

    System.out.println("");
    app.getMem();
    System.out.println("# of loops: " + app.lsg.getNumLoops() +
                       " (including 1 artificial root node)");
    System.out.println("# of BBs  : " + BasicBlock.getNumBasicBlocks());
    System.out.println("# max time: " + finder.getMaxMillis());
    System.out.println("# min time: " + finder.getMinMillis());
    app.lsg.calculateNestingLevel();
    //app.lsg.Dump();
  }

  public  CFG        cfg;
  private LSG        lsg;
  private BasicBlock root;
}