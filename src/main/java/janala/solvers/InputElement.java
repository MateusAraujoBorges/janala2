package janala.solvers;

import java.io.Serializable;

import com.google.common.collect.Range;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import janala.config.Config;
import janala.interpreters.Value;

public class InputElement implements Serializable {
  private static final long serialVersionUID = -5131083673317378995L;
  public final Integer symbol;
  public final Value value;
  public final Range<Long> range;

  public InputElement(Integer symbol, Value value) {
    this.symbol = symbol;
    this.value = value;
    this.range = Config.instance.getDefaultRange();
  }

  public InputElement(Integer symbol, Value value, int lo, int hi) {
    this(symbol, value, Range.closed((long) lo, (long) hi));
  }

  public InputElement(Integer symbol, Value value, long lo, long hi) {
    this(symbol, value, Range.closed(lo, hi));
  }

  public InputElement(Integer symbol, Value value, Range<Long> range) {
    this.symbol = symbol;
    this.value = value;
    this.range = range;
  }

  // Value can have symbolic integers, which in turn can have fields like iid
  // which can change between executions. For this reason I will not include
  // 'value' in hashcode() and equals().

  private int computeHashCode() {
    HashFunction hf = Hashing.goodFastHash(32);
    return hf.newHasher().putInt(symbol)/* .putInt(value.hashCode()) */
        .putInt(range.hashCode()).hash().asInt();
  }

  @Override
  public int hashCode() {
    return computeHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InputElement other = (InputElement) obj;
    if (range == null) {
      if (other.range != null)
        return false;
    } else if (!range.equals(other.range))
      return false;
    if (symbol == null) {
      if (other.symbol != null)
        return false;
    } else if (!symbol.equals(other.symbol))
      return false;
    // if (value == null) {
    // if (other.value != null)
    // return false;
    // } else if (!value.equals(other.value))
    // return false;
    return true;
  }
}
