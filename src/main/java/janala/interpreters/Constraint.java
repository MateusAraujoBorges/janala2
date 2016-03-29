package janala.interpreters;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

import com.google.common.hash.Hashing;

/**
 * Author: Koushik Sen (ksen@cs.berkeley.edu) Date: 6/22/12 Time: 12:23 PM
 */
public abstract class Constraint implements Serializable {
  private static final long serialVersionUID = 2442956915284687102L;
  public int iid;
  public int index;

  public abstract void accept(ConstraintVisitor v);

  public abstract Constraint not();

  public abstract Constraint substitute(Map<String, Long> assignments);

  /*
   * Mateus: Not 100% sure, but I believe Constraints are "handled" like
   * immutable objects. For now I will use toString() for the equals, but this
   * may be changed if the previous hypothesis is incorrect.
   */

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Constraint) {
      Constraint cons = (Constraint) obj;
      return obj.toString().equals(this.toString());
    }
    return false;
  }

  public int hashCode() {
    return Hashing.goodFastHash(32).newHasher().putString(this.toMathString(), Charset.defaultCharset()).hash().asInt();
  }

  public abstract String toMathString();

}
