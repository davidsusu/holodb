package hu.webarticum.holodb.metamodel;

import java.math.BigInteger;

// FIXME: should this be more complex (decorable, limit etc.)
public interface Selection extends Iterable<BigInteger> {

    public int size();
    
    public default BigInteger get(long nth) {
        return get(BigInteger.valueOf(nth));
    }
    
    public BigInteger get(BigInteger nth);
    
}