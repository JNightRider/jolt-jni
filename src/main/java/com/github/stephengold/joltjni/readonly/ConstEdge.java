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
package com.github.stephengold.joltjni.readonly;

/**
 * Read-only access to an {@code Edge}. (native type:
 * {@code const SoftBodySharedSettings::Edge})
 *
 * @author Stephen Gold sgold@sonic.net
 */
public interface ConstEdge extends ConstJoltPhysicsObject {
    /**
     * Return the inverse of the spring's stiffness. The edge is unaffected.
     *
     * @return the compliance value
     */
    float getCompliance();

    /**
     * Return the rest length of the spring. The edge is unaffected.
     *
     * @return the length (in meters)
     */
    float getRestLength();

    /**
     * Return the mesh vertex at the specified end.
     *
     * @param indexInEdge which end of the edge (0 or 1)
     * @return the mesh index of the vertex (&ge;0)
     */
    int getVertex(int indexInEdge);
}
