//
package tests.casestudies.sir.siena;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Iterator;

public class Filter {
    private Map constraints;

    /** 
	creates an empty filter.  A filter with no constraints.
    */
    public Filter() { 
	constraints = new TreeMap();
    }

    /**
       <code>true</code> iff this filter contains no constraints
    */
    public boolean isEmpty() {
	return constraints.isEmpty();
    }

    /**
       put a constraint <em>a</em> on attribute <em>name</em>. Example:
       <pre><code>
       Filter f = new Filter();
       AttributeConstraint a;
       a = new AttrbuteConstraint(AttributeConstraint.SS, "soft") 
       f.addConstraint("subject", a);
       </pre></code>
    */
    public void addConstraint(String name, AttributeConstraint a) {
	Set s = (Set)constraints.get(name);
	if (s == null) {
	    s = new HashSet();
	    constraints.put(name,s);
	}
	s.add(a);
    }

    /**
       put a constraint on attribute <em>name</em> using
       comparison operator <em>op</em> and a <code>String</code>
       argument <em>sval</em>.

       <pre><code>
       Filter f = new Filter();
       f.addConstraint("subject", AttributeConstraint.SS, "soft");
       </pre></code> 
    */
    public void addConstraint(String s, short op, String sval) {
	addConstraint(s,new AttributeConstraint(op,sval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       comparison operator <em>op</em> and a <code>byte[]</code>
       argument <em>sval</em>.
    */
    public void addConstraint(String s, short op, byte[] sval) {
	addConstraint(s,new AttributeConstraint(op,sval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       comparison operator <em>op</em> and an <code>int</code>
       argument <em>ival</em>.

       <pre><code>
       Filter f = new Filter();
       f.addConstraint("price", AttributeConstraint.LT, 500);
       </pre></code> 
    */
    public void addConstraint(String s, short op, int ival) {
	addConstraint(s,new AttributeConstraint(op,ival));
    };

    /**
       put a constraint on attribute <em>name</em> using
       comparison operator <em>op</em> and a <code>long</code>
       argument <em>lval</em>.
    */
    public void addConstraint(String s, short op, long lval) {
	addConstraint(s,new AttributeConstraint(op,lval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       comparison operator <em>op</em> and a <code>boolean</code>
       argument <em>bval</em>.

       <pre><code>
       Filter f = new Filter();
       f.addConstraint("failed", AttributeConstraint.EQ, true);
       </pre></code> 
    */
    public void addConstraint(String s, short op, boolean bval) {
	addConstraint(s,new AttributeConstraint(op,bval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       comparison operator <em>op</em> and a <code>double</code>
       argument <em>dval</em>.
    */
    public void addConstraint(String s, short op, double dval) {
	addConstraint(s,new AttributeConstraint(op,dval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       the equality operator and a <code>String</code>
       argument <em>sval</em>. Example:
       <pre><code>
       Filter f = new Filter();
       f.addConstraint("name", "Antonio");
       </pre></code> 
    */
    public void addConstraint(String s, String sval) {
	addConstraint(s,new AttributeConstraint(AttributeConstraint.EQ,sval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       the equality operator and a <code>byte[]</code>
       argument <em>sval</em>.
    */
    public void addConstraint(String s, byte[] sval) {
	addConstraint(s,new AttributeConstraint(AttributeConstraint.EQ,sval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       the equality operator and a <code>int</code>
       argument <em>ival</em>.
    */
    public void addConstraint(String s, int ival) {
	addConstraint(s,new AttributeConstraint(AttributeConstraint.EQ,ival));
    };

    /**
       put a constraint on attribute <em>name</em> using
       the equality operator and a <code>boolean</code>
       argument <em>bval</em>.
    */
    public void addConstraint(String s, boolean bval) {
	addConstraint(s,new AttributeConstraint(AttributeConstraint.EQ,bval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       the equality operator and a <code>long</code>
       argument <em>lval</em>.
    */
    public void addConstraint(String s, long lval) {
	addConstraint(s,new AttributeConstraint(AttributeConstraint.EQ,lval));
    };

    /**
       put a constraint on attribute <em>name</em> using
       the equality operator and a <code>double</code>
       argument <em>dval</em>.
    */
    public void addConstraint(String s, double dval) {
	addConstraint(s,new AttributeConstraint(AttributeConstraint.EQ,dval));
    };

    /**
        returns an iterator for the set of attribute (constraint) 
        names of this <code>Filter</code>.
     */
    public Iterator constraintNamesIterator() {
	return constraints.keySet().iterator();
    }

    /**
        returns an iterator for the set of constraints over attribute
        <em>name</em> of this <code>Filter</code>.  
     */
    public Iterator constraintsIterator(String name) {
	Set s = (Set)constraints.get(name);
	if (s == null) return null;
	return s.iterator();
    }

    public String toString() {
	return new String(SENP.encode(this));
    }
}