package hu.webarticum.holodb.core.data.binrel.monotonic;

import java.math.BigInteger;

import hu.webarticum.holodb.core.data.distribution.Sampler;
import hu.webarticum.holodb.core.data.random.TreeRandom;
import hu.webarticum.holodb.core.data.random.TreeRandomUtil;
import hu.webarticum.holodb.core.data.selection.Range;
import hu.webarticum.holodb.core.util.MathUtil;

public class SurjectiveMonotonic extends AbstractCachingRecursiveMonotonic {

    private static final SamplerFactory DEFAULT_SAMPLER_FACTORY = SamplerFactory.DEFAULT;
    
    private static final int DEFAULT_CACHE_DEPTH = 10;
    
    private static final BigInteger DEFAULT_SAMPLER_MAX_LENGTH = BigInteger.valueOf(1000L);
    
    
    private final TreeRandom treeRandom;
    
    private final SamplerFactory samplerFactory;
    
    private final BigInteger samplerMaxLength;

    
    public SurjectiveMonotonic(TreeRandom treeRandom, long size, long imageSize) {
        this(treeRandom, BigInteger.valueOf(size), BigInteger.valueOf(imageSize));
    }

    public SurjectiveMonotonic(TreeRandom treeRandom, SamplerFactory samplerFactory, long size, long imageSize) {
        this(treeRandom, samplerFactory, BigInteger.valueOf(size), BigInteger.valueOf(imageSize), DEFAULT_CACHE_DEPTH);
    }

    public SurjectiveMonotonic(TreeRandom treeRandom, BigInteger size, BigInteger imageSize) {
        this(treeRandom, size, imageSize, DEFAULT_CACHE_DEPTH);
    }

    public SurjectiveMonotonic(TreeRandom treeRandom, BigInteger size, BigInteger imageSize, int cacheDepth) {
        this(treeRandom, DEFAULT_SAMPLER_FACTORY, size, imageSize, cacheDepth);
    }

    public SurjectiveMonotonic(
            TreeRandom treeRandom, SamplerFactory samplerFactory, BigInteger size, BigInteger imageSize, int cacheDepth) {
        this(treeRandom, samplerFactory, size, imageSize, cacheDepth, DEFAULT_SAMPLER_MAX_LENGTH);
    }
    
    public SurjectiveMonotonic(
            TreeRandom treeRandom, SamplerFactory samplerFactory,
            BigInteger size, BigInteger imageSize, int cacheDepth, BigInteger samplerMaxLength) {
        
        super(checkSize(size, imageSize), imageSize, cacheDepth);
        this.treeRandom = treeRandom;
        this.samplerFactory = samplerFactory;
        this.samplerMaxLength = samplerMaxLength;
    }
    
    private static BigInteger checkSize(BigInteger size, BigInteger imageSize) {
        if (size.compareTo(imageSize) < 0) {
            throw new IllegalArgumentException("size must not be less then imageSize");
        }
        return size;
    }
    
    
    @Override
    protected BigInteger splitCacheable(Range range, Range imageRange, BigInteger imageSplitPoint, int level) {
        BigInteger length = range.size();
        
        // TODO: create standalone SamplerFactory interface
        // FIXME: functional lambda?
        // TODO: SamplerFactory::isFast()
        // TODO: SamplerFactory::isBig()
        
        BigInteger splitPoint;
        Range rangeToSplit = Range.fromUntil(
                range.from().add(imageSplitPoint.subtract(imageRange.from())),
                range.until().subtract(imageRange.until().subtract(imageSplitPoint)));
        
        if (length.compareTo(samplerMaxLength) > 0) {
            splitPoint = splitFast(rangeToSplit);
        } else {
            splitPoint = splitWithSampler(rangeToSplit, imageRange, imageSplitPoint);
        }

        return splitPoint;
    }

    private BigInteger splitFast(Range range) {
        return range.from().add(range.size().divide(BigInteger.TWO));
    }

    private BigInteger splitWithSampler(Range range, Range imageRange, BigInteger imageSplitPoint) {
        BigInteger imageFirstLength = imageSplitPoint.subtract(imageRange.from());
        double probability = MathUtil.divideBigIntegers(imageFirstLength, imageRange.size());
        long seed = TreeRandomUtil.fetchLong(treeRandom.sub(imageSplitPoint));
        Sampler sampler = samplerFactory.create(seed, range.size(), probability);
        BigInteger relativeSplitPoint = sampler.sample();
        return range.from().add(relativeSplitPoint);
    }
    
}
