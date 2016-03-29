//
package tests.casestudies.sir.siena;

import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

public class Event {
    private Map attributes;

    /**
     * construct an empty event.
     */
    public Event() { 
	attributes = new TreeMap();
    }
    
    /**
     * returns an iterator over the set of attribute names.
     */
    public Iterator iterator() {
	return attributes.keySet().iterator();
    }

    /**
     * set the value of an attribute.  
     * Add the attribute if that is not present.
     * @param name attribute name.  An attribute name must contain
     * only letters (<code>'a'</code>-<code>'z'</code>,
     * <code>'A'</code>-<code>'Z'</code>), the underscore character
     * (<code>'_'</code>), the dot (<code>'.'</code>), and the forward
     * slash character (<code>'/'</code>).
     * @param value String value.  */
    public void putAttribute(String name, String value) {
	attributes.put(name, new AttributeValue(value));
    }

    /**
     * set the value of an attribute.  
     * Add the attribute if that is not present.
     * @param name attribute name.  An attribute name must contain
     * only letters (<code>'a'</code>-<code>'z'</code>,
     * <code>'A'</code>-<code>'Z'</code>), the underscore character
     * (<code>'_'</code>), the dot (<code>'.'</code>), and the forward
     * slash character (<code>'/'</code>).
     * @param value byte array value. 
     */
    public void putAttribute(String name, byte[] value) {
	attributes.put(name, new AttributeValue(value));
    }

    /**
     * set the value of an attribute.  
     * Add the attribute if that is not present.
     * @param name attribute name.  An attribute name must contain
     * only letters (<code>'a'</code>-<code>'z'</code>,
     * <code>'A'</code>-<code>'Z'</code>), the underscore character
     * (<code>'_'</code>), the dot (<code>'.'</code>), and the forward
     * slash character (<code>'/'</code>).
     * @param value int value. 
     */
    public void putAttribute(String name, int value) {
	attributes.put(name, new AttributeValue(value));
    }

    /**
     * set the value of an attribute.  
     * Add the attribute if that is not present.
     * @param name attribute name.  An attribute name must contain
     * only letters (<code>'a'</code>-<code>'z'</code>,
     * <code>'A'</code>-<code>'Z'</code>), the underscore character
     * (<code>'_'</code>), the dot (<code>'.'</code>), and the forward
     * slash character (<code>'/'</code>).
     * @param value int value. 
     */
    public void putAttribute(String name, long value) {
	attributes.put(name, new AttributeValue(value));
    }

    /**
     * set the value of an attribute.  
     * Add the attribute if that is not present.
     * @param name attribute name.  An attribute name must contain
     * only letters (<code>'a'</code>-<code>'z'</code>,
     * <code>'A'</code>-<code>'Z'</code>), the underscore character
     * (<code>'_'</code>), the dot (<code>'.'</code>), and the forward
     * slash character (<code>'/'</code>).
     * @param value double value. 
     */
    public void putAttribute(String name, double value) {
	attributes.put(name, new AttributeValue(value));
    }

    /**
     * set the value of an attribute.  
     * Add the attribute if that is not present.
     * @param name attribute name.  An attribute name must contain
     * only letters (<code>'a'</code>-<code>'z'</code>,
     * <code>'A'</code>-<code>'Z'</code>), the underscore character
     * (<code>'_'</code>), the dot (<code>'.'</code>), and the forward
     * slash character (<code>'/'</code>).
     * @param value boolean value. 
     */
    public void putAttribute(String name, boolean value) {
	attributes.put(name, new AttributeValue(value));
    }

    /**
     * set the value of an attribute.  
     * Add the attribute if that is not present.
     * @param name attribute name.  An attribute name must contain
     * only letters (<code>'a'</code>-<code>'z'</code>,
     * <code>'A'</code>-<code>'Z'</code>), the underscore character
     * (<code>'_'</code>), the dot (<code>'.'</code>), and the forward
     * slash character (<code>'/'</code>).
     * @param value value. 
     */
    public void putAttribute(String name, AttributeValue value) {
	attributes.put(name, value);
    }
    /**
     * returns the value of an attribute.  Returns <code>null</code> if
     * that attribute does not exist in this event.
     * 
     * @param name attribute name.  
     */
    public AttributeValue getAttribute(String name) {
	return (AttributeValue)attributes.get(name);
    }

    /**
     * returns the number of attributes in this event.
     * 
     * @param name attribute name.  
     */
    public int size() {
	return attributes.size();
    }

    /**
     * removes every attribute from this event.
     */
    public void clear() {
	attributes.clear();
    }

    /**
     * returns an iterator for the set of attribute names of this event.
     */
    public Iterator attributeNamesIterator() {
	return attributes.keySet().iterator();
    }

    public String toString() {
	return new String(SENP.encode(this));
    }
}