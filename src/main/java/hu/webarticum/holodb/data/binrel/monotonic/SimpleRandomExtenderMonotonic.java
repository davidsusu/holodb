package hu.webarticum.holodb.data.binrel.monotonic;

import java.math.BigInteger;

import hu.webarticum.holodb.data.random.TreeRandom;
import hu.webarticum.holodb.util.Range;

public class SimpleRandomExtenderMonotonic implements Monotonic {

    private final TreeRandom treeRandom;
    
    private final BigInteger size;
    
    private final BigInteger imageSize;
    
    
    public SimpleRandomExtenderMonotonic(TreeRandom treeRandom, BigInteger size, BigInteger imageSize) {
        if (size.compareTo(imageSize) < 0) {
            throw new IllegalArgumentException("Size can not be smaller than imageSize");
        }
        
        this.treeRandom = treeRandom;
        this.size = size;
        this.imageSize = imageSize;
    }
    
    
    @Override
    public BigInteger size() {
        return size;
    }
    
    @Override
    public BigInteger imageSize() {
        return imageSize;
    }
    
    @Override
    public boolean isReversible() {
        return true;
    }

    @Override
    public BigInteger at(BigInteger index) {
        
        // TODO
        
        return BigInteger.ZERO;
    }
    
    @Override
    public Range indicesOf(BigInteger value) {
        
        // TODO
        
        return Range.fromLength(BigInteger.ZERO, 5);
    }
    
}