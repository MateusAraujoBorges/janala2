package janala.solvers.counters.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import janala.solvers.counters.trees.ConcolicCountTree;
import janala.solvers.counters.trees.SymbolicTree;

public class CountUtils {

	public static void writeTreeToFile(SymbolicTree tree, String symtreeFile) throws FileNotFoundException, IOException {
		File f = new File(symtreeFile);
		f.createNewFile(); // create new file if it not exists already
		tree.writeToDisk(f);
	}

	public static SymbolicTree readTreeFromFile(String symtreeFile)
	        throws FileNotFoundException, ClassNotFoundException, IOException {
		return ConcolicCountTree.readFromDisk(new File(symtreeFile));
	}

	public static RandomGenerator readRNGFromFile(String rngFile)
	        throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(rngFile)));
		Object tmp = is.readObject();
		is.close();
		if (tmp instanceof MersenneTwister) {
			return (MersenneTwister) tmp;
		} else {
			throw new RuntimeException("Unexpected rng type! " + tmp.getClass().getName());
		}
	}

	public static void writeRNGToFile(RandomGenerator rng, String rngFile) throws FileNotFoundException, IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(rngFile)));
		os.writeObject(rng);
		os.close();
	}

}
