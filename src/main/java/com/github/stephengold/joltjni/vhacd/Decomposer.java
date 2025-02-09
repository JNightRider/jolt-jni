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
package com.github.stephengold.joltjni.vhacd;

import com.github.stephengold.joltjni.JoltPhysicsObject;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Decompose 3-D meshes into collections of convex hulls using Khaled Mamou's
 * Volumetric-Hierarchical Approximate Convex Decomposition (V-HACD) algorithm.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class Decomposer extends JoltPhysicsObject {
    // *************************************************************************
    // constants

    /**
     * number of axes in the coordinate system
     */
    final private static int numAxes = 3;
    /**
     * number of vertices per triangle
     */
    final private static int vpt = 3;
    // *************************************************************************
    // fields

    /**
     * collect convex hulls generated by the latest decomposition
     */
    final private Collection<ConvexHull> hulls = new ArrayList<>(64);
    /**
     * registered progress listeners
     */
    final private Collection<ProgressListener> progressListeners
            = new ArrayList<>(4);
    // *************************************************************************
    // constructors

    /**
     * Instantiate a default instance.
     */
    public Decomposer() {
        long instanceVa = createDefault();
        setVirtualAddress(instanceVa, () -> free(instanceVa));
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Register the specified progress listener.
     *
     * @param listener the listener to register (not null, alias created)
     * @return the modified properties, for chaining
     */
    public Decomposer addProgressListener(ProgressListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        assert !progressListeners.contains(listener);

        progressListeners.add(listener);
        return this;
    }

    /**
     * Generate convex hulls to approximate the specified mesh.
     *
     * @param locations the locations of all mesh vertices (not null, length a
     * multiple of 3, unaffected)
     * @param indices the vertex indices of all mesh triangles (not null, length
     * a multiple of 3, unaffected)
     * @param params the tuning parameters to use (not null, unaffected)
     * @return a new unmodifiable collection of hulls, or an empty collection if
     * the algorithm failed
     */
    public Collection<ConvexHull> decompose(
            float[] locations, int[] indices, Parameters params) {
        Objects.requireNonNull(locations, "locations must not be null");
        Objects.requireNonNull(indices, "indices must not be null");
        assert locations.length % numAxes == 0 : locations.length;
        assert indices.length % vpt == 0 : indices.length;

        hulls.clear();
        long decomposerVa = va();
        long paramsVa = params.va();
        boolean debugOutput = params.isDebugOutputEnabled();
        decomposeAa(decomposerVa, locations, indices, paramsVa, debugOutput);

        Collection<ConvexHull> result
                = Collections.unmodifiableCollection(hulls);
        return result;
    }

    /**
     * Generate convex hulls to approximate the specified mesh.
     *
     * @param locations the locations of all mesh vertices (not null, direct,
     * capacity a multiple of 3, unaffected)
     * @param indices the vertex indices of all mesh triangles (not null,
     * direct, capacity a multiple of 3, unaffected)
     * @param params the tuning parameters to use (not null, unaffected)
     * @return a new unmodifiable collection of hulls, or an empty collection if
     * the algorithm failed
     */
    public Collection<ConvexHull> decompose(
            FloatBuffer locations, IntBuffer indices, Parameters params) {
        Objects.requireNonNull(locations, "locations must not be null");
        Objects.requireNonNull(indices, "indices must not be null");
        assert locations.capacity() % numAxes == 0 : locations.capacity();
        assert indices.capacity() % vpt == 0 : indices.capacity();

        hulls.clear();
        long decomposerVa = va();
        long paramsVa = params.va();
        boolean debugOutput = params.isDebugOutputEnabled();
        decomposeBb(decomposerVa, locations, indices, paramsVa, debugOutput);

        Collection<ConvexHull> result
                = Collections.unmodifiableCollection(hulls);
        return result;
    }

    /**
     * De-register the specified progress listener.
     *
     * @param listener the listener to de-register (not null, unaffected)
     */
    public void removeProgressListener(ProgressListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");

        boolean success = progressListeners.remove(listener);
        assert success;
    }
    // *************************************************************************
    // private methods

    /**
     * Add a hull to the result.
     * <p>
     * This method is invoked by native code.
     *
     * @param hullVa the virtual address of the native ConvexHull to add (not
     * zero)
     */
    private void addHull(long hullVa) {
        ConvexHull hull = new ConvexHull(hullVa, true);
        hulls.add(hull);
    }

    /**
     * Update all progress listeners.
     * <p>
     * This method is invoked by native code.
     *
     * @param overallPercent an overall completion percentage (&ge;0, &le;100)
     * @param stagePercent a completion percentage for the current stage (&ge;0,
     * &le;100)
     * @param operationPercent a completion percentage for the current operation
     * (&ge;0, &le;100)
     * @param stageName the name of the current stage
     * @param operationName the name of the current operation
     */
    private void update(double overallPercent, double stagePercent,
            double operationPercent, String stageName, String operationName) {
        for (ProgressListener listener : progressListeners) {
            listener.update(overallPercent, stagePercent,
                    operationPercent, stageName, operationName);
        }
    }
    // *************************************************************************
    // native private methods

    native private long createDefault();

    native private static void decomposeAa(long decomposerVa, float[] locations,
            int[] indices, long paramsVa, boolean debugOutput);

    native private static void decomposeBb(
            long decomposerVa, FloatBuffer locations, IntBuffer indices,
            long paramsVa, boolean debugOutput);

    native private static void free(long instanceVa);
}
