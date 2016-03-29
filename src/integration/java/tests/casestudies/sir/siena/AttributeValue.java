//
package tests.casestudies.sir.siena;

/**
   value of an attribute in an event notification.

   @doc container for typed vaules in notifications.  An
   <code>AttributeValue</code> can represents values of type
   <code>String</code>, <code>byte[]</code>, <code>int</code>,
   <code>long</code>, <code>double</code>, and <code>boolean</code>.
  */
public class AttributeValue {
    /** <em>null</em> type, it is the default type of a Siena attribute */
    public static final int	NULL		= 0;
    /** string of bytes */
    public static final int	BYTEARRAY	= 1;
    /** an alias to <code>BYTEARRAY</code> 
	provided only for backward compatibility */
    public static final int	STRING		= 1;
    /** integer type.  Corresponds to the Java <code>long</code> type. */
    public static final int	LONG		= 2;
    /** integer type.  Corresponds to the Java <code>int</code> type. */
    public static final int	INT		= 2;
    /** double type.  Corresponds to the Java <code>double</code> type. */
    public static final int	DOUBLE		= 3;
    /** boolean type.  Corresponds to the Java <code>boolean</code> type. */
    public static final int	BOOL		= 4;

    protected	int		type;

    protected	byte[]		sval;
    protected	long		ival;
    protected	double		dval;
    protected	boolean		bval;
    // other types here...

    public AttributeValue() {
	type = NULL;
    }

    public AttributeValue(AttributeValue x) {
	type = x.type;
	switch(type) {
	case INT: ival = x.ival; break;
	case BOOL: bval = x.bval; break;
	case DOUBLE: dval = x.dval; break;
	case BYTEARRAY: sval = x.sval; break;
	}
    }

    public AttributeValue(String s) {
	type = BYTEARRAY;
	sval = s.getBytes();
    }

    public AttributeValue(byte[] s) {
	type = BYTEARRAY;
	sval = s;
    }

    public AttributeValue(int i) {
	type = LONG;
	ival = i;
	sval = null;
    }

    public AttributeValue(long i) {
	type = LONG;
	ival = i;
	sval = null;
    }

    public AttributeValue(boolean b) {
	type = BOOL;
	bval = b;
	sval = null;
    }

    public AttributeValue(double d) {
	type = DOUBLE;
	dval = d;
	sval = null;
    }
    //
    // other types here ...work in progress...
    //

    public int getType() {
	return type;
    }

    public int intValue() {
	switch(type) {
	case INT: return (int)ival;
	case BOOL: return bval ? 1 : 0;
	case DOUBLE: return (int)dval;
	case BYTEARRAY: return Integer.decode(new String(sval)).intValue();
	default:
	    return 0; // should throw an exception here 
	              // ...work in progress...
	}
    }

    public long longValue() {
	switch(type) {
	case INT: return ival;
	case BOOL: return bval ? 1 : 0;
	case DOUBLE: return (int)dval;
	case BYTEARRAY: return Long.decode(new String(sval)).intValue();
	default:
	    return 0; // should throw an exception here 
	              // ...work in progress...
	}
    }

    public double doubleValue() {
	switch(type) {
	case INT: return ival;
	case BOOL: return bval ? 1 : 0;
	case DOUBLE: return dval;
	case BYTEARRAY: return Double.valueOf(new String(sval)).doubleValue();
	default:
	    return 0; // should throw an exception here 
	              // ...work in progress...
	}
    }

    public boolean booleanValue() {
	switch(type) {
	case INT: return ival != 0;
	case BOOL: return bval;
	case DOUBLE: return dval != 0;
	case BYTEARRAY: return Boolean.getBoolean(new String(sval));
	default:
	    return false; // should throw an exception here 
	                  // ...work in progress...
	}
    }

    public String stringValue() {
	switch(type) {
	case INT: return String.valueOf(ival);
	case BOOL: return String.valueOf(bval);
	case DOUBLE: return String.valueOf(dval);
	case BYTEARRAY: return new String(sval);
	default:
	    return ""; // should throw an exception here 
	               // ...work in progress...
	}
    }

    public byte[] byteArrayValue() {
	switch(type) {
	case INT: return String.valueOf(ival).getBytes();
	case BOOL: return String.valueOf(bval).getBytes();
	case DOUBLE: return String.valueOf(dval).getBytes();
	case BYTEARRAY: return sval;
	default:
	    return null; // should throw an exception here 
	                 // ...work in progress...
	}
    }

    public boolean isEqualTo(AttributeValue x) {
	switch(type) {
	case BYTEARRAY: return sval.equals(x.sval);
	case INT: return ival == x.longValue();
	case DOUBLE: return dval == x.doubleValue();
	case BOOL: return bval == x.booleanValue();
	default: return false;
	}
    }

    public String toString() {
	return new String(SENP.encode(this));
    }

    public int hashCode() {
	return this.toString().hashCode();
    }
}


