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
package com.github.stephengold.joltjni.readonly;

import com.github.stephengold.joltjni.StreamOut;
import com.github.stephengold.joltjni.streamutils.MaterialToIdMap;
import com.github.stephengold.joltjni.streamutils.SharedSettingsToIdMap;
import java.nio.IntBuffer;

/**
 * Read-only access to a {@code SoftBodySharedSettings} object. (native type:
 * const SoftBodySharedSettings)
 *
 * @author Stephen Gold sgold@sonic.net
 */
public interface ConstSoftBodySharedSettings extends ConstJoltPhysicsObject {
    /**
     * Count the edge constraints. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    int countEdgeConstraints();

    /**
     * Count the faces. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    int countFaces();

    /**
     * Count the pinned vertices. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    int countPinnedVertices();

    /**
     * Count the bend-twist constraints. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    int countRodBendTwistConstraints();

    /**
     * Count the discrete Cosserat rods. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    int countRodStretchShearConstraints();

    /**
     * Count the vertices. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    int countVertices();

    /**
     * Count the volume constraints. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    int countVolumeConstraints();

    /**
     * Enumerate all Cosserat rods in the settings. (native member:
     * mRodStretchShearConstraints)
     *
     * @return a new array of new JVM objects with the pre-existing native
     * objects assigned
     */
    ConstRodStretchShear[] getRodStretchShearConstraints();

    /**
     * Write the vertex indices of all edges to the specified buffer and advance
     * the buffer's position. The settings are unaffected.
     *
     * @param storeIndices the destination buffer (not null, modified)
     */
    void putEdgeIndices(IntBuffer storeIndices);

    /**
     * Write the vertex indices of all faces to the specified buffer and advance
     * the buffer's position. The settings are unaffected.
     *
     * @param storeIndices the destination buffer (not null, modified)
     */
    void putFaceIndices(IntBuffer storeIndices);

    /**
     * Write the state of this object to the specified stream, excluding the
     * materials. The settings are unaffected.
     *
     * @param stream where to write objects (not {@code null})
     */
    void saveBinaryState(StreamOut stream);

    /**
     * Write the state of this object to the specified stream. The settings are
     * unaffected.
     *
     * @param stream where to write objects (not {@code null})
     * @param settingsMap track multiple uses of shared settings (not
     * {@code null})
     * @param materialMap track multiple uses of physics materials (not
     * {@code null})
     */
    void saveWithMaterials(StreamOut stream, SharedSettingsToIdMap settingsMap,
            MaterialToIdMap materialMap);
}
