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

import com.github.stephengold.joltjni.readonly.Vec3Arg;

/**
 * 3-D geometric description of a point, for use in convex-support calculations.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class PointConvexSupport extends JoltPhysicsObject {
    // *************************************************************************
    // constructors

    /**
     * Instantiate a point at the specified location.
     *
     * @param location the desired location (not null, unaffected)
     */
    public PointConvexSupport(Vec3Arg location) {
        float x = location.getX();
        float y = location.getY();
        float z = location.getZ();
        long pointVa = create(x, y, z);
        setVirtualAddress(pointVa, () -> free(pointVa));
    }
    // *************************************************************************
    // native private methods

    native private static long create(float x, float y, float z);

    native private static void free(long sphereVa);
}
