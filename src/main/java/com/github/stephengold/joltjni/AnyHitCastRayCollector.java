/*
Copyright (c) 2025 Stephen Gold

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.github.stephengold.joltjni;

/**
 * Collect one result from a narrow-phase ray-cast query. (native type: {@code
 * AnyHitCollisionCollector<CastRayCollector>})
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class AnyHitCastRayCollector extends CastRayCollector {
    // *************************************************************************
    // constructors

    /**
     * Instantiate a default collector.
     */
    public AnyHitCastRayCollector() {
        long collectorVa = createDefault();
        setVirtualAddress(collectorVa, true);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Access the hit result.
     *
     * @return a new JVM object with the pre-existing native object assigned
     */
    public RayCastResult getHit() {
        long collectorVa = va();
        long resultVa = getHit(collectorVa);
        RayCastResult result = new RayCastResult(this, resultVa);

        return result;
    }

    /**
     * Test whether the latest query resulted in a hit. The collector is
     * unaffected.
     *
     * @return {@code true} if there was a hit, otherwise {@code false}
     */
    public boolean hadHit() {
        long collectorVa = va();
        boolean result = hadHit(collectorVa);

        return result;
    }
    // *************************************************************************
    // native private methods

    native private static long createDefault();

    native private static long getHit(long collectorVa);

    native private static boolean hadHit(long collectorVa);
}
