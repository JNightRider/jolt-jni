/*
Copyright (c) 2024-2025 Stephen Gold

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
package com.github.stephengold.joltjni.std;

import com.github.stephengold.joltjni.JoltPhysicsObject;

/**
 * Generate uniformly distributed {@code float} values. (native type:
 * {@code std::uniform_real_distribution<float>})
 * <p>
 * The algorithm isn't specific to Jolt Physics. It's included in Jolt JNI for
 * expediency, since BroadPhaseInsertionTest.cpp uses it.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class UniformFloatDistribution extends JoltPhysicsObject {
    // *************************************************************************
    // constructors

    /**
     * Instantiate a uniform distribution with the specified range of output
     * values.
     *
     * @param min the desired lower limit
     * @param max the desired upper limit
     */
    public UniformFloatDistribution(float min, float max) {
        long distributionVa = createDistribution(min, max);
        setVirtualAddress(distributionVa, () -> free(distributionVa));
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Iterate and return the next {@code float} value in the sequence.
     *
     * @param generator the sequence generator to use (not null, modified)
     * @return the value
     */
    public float nextFloat(RandomNumberEngine generator) {
        long distributionVa = va();
        long generatorVa = generator.targetVa();
        float result;
        if (generator instanceof DefaultRandomEngine) {
            result = nextFloatDre(distributionVa, generatorVa);
        } else if (generator instanceof Mt19937) {
            result = nextFloat(distributionVa, generatorVa);
        } else {
            String className = generator.getClass().getSimpleName();
            throw new IllegalArgumentException("className = " + className);
        }

        return result;
    }
    // *************************************************************************
    // native private methods

    native private static long createDistribution(float min, float max);

    native private static void free(long distributionVa);

    native private static float nextFloat(
            long distributionVa, long generatorVa);

    native private static float nextFloatDre(
            long distributionVa, long generatorVa);
}
