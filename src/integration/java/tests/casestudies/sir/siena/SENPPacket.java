//
package tests.casestudies.sir.siena;

import java.util.TreeMap;
import java.util.Iterator;

public class SENPPacket {
    public byte		version;
    public byte		method;
    public byte		ttl;
    public byte[]	to;
    public byte[]	id;
    public byte[]	handler;
    
    public Event	event;
    public Filter	filter;

    public SENPPacket() {
	version = SENP.ProtocolVersion;
	method = SENP.NOP;
	ttl = SENP.DefaultTtl;
    }

    public String toString() {
	return new String(SENP.encode(this));
    }
};