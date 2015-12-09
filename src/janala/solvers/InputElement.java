/*
 * Copyright (c) 2012, NTT Multimedia Communications Laboratories, Inc. and Koushik Sen
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Author: Koushik Sen (ksen@cs.berkeley.edu)
 */

package janala.solvers;

import java.io.Serializable;

import com.google.common.collect.Range;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import janala.config.Config;
import janala.interpreters.Value;

/**
 * Author: Koushik Sen (ksen@cs.berkeley.edu)
 * Date: 6/22/12
 * Time: 2:29 PM
 */
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
    	this(symbol,value,Range.closed((long) lo, (long) hi));
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
		return hf.newHasher().putInt(symbol)/*.putInt(value.hashCode())*/
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
//		if (value == null) {
//			if (other.value != null)
//				return false;
//		} else if (!value.equals(other.value))
//			return false;
		return true;
	} 
}
