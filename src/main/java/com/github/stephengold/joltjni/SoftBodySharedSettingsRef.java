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
package com.github.stephengold.joltjni;

import com.github.stephengold.joltjni.enumerate.EBendType;
import com.github.stephengold.joltjni.readonly.ConstSoftBodySharedSettings;
import com.github.stephengold.joltjni.readonly.ConstVertexAttributes;
import com.github.stephengold.joltjni.template.Ref;
import java.nio.IntBuffer;

/**
 * A counted reference to {@code SoftBodySharedSettings}. (native type:
 * {@code Ref<SoftBodySharedSettings>})
 *
 * @author Stephen Gold sgold@sonic.net
 */
final public class SoftBodySharedSettingsRef
        extends Ref
        implements ConstSoftBodySharedSettings {
    // *************************************************************************
    // constructors

    /**
     * Instantiate an empty reference.
     */
    public SoftBodySharedSettingsRef() {
        long refVa = createEmpty();
        setVirtualAddress(refVa, () -> free(refVa));
    }

    /**
     * Instantiate a reference with the specified native object assigned.
     *
     * @param refVa the virtual address of the native object to assign (not
     * zero)
     * @param owner {@code true} &rarr; make the JVM object the owner,
     * {@code false} &rarr; it isn't the owner
     */
    SoftBodySharedSettingsRef(long refVa, boolean owner) {
        Runnable freeingAction = owner ? () -> free(refVa) : null;
        setVirtualAddress(refVa, freeingAction);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Add the specified edge constraint.
     *
     * @param edge the edge to add (not null, unaffected)
     */
    public void addEdgeConstraint(Edge edge) {
        long settingsVa = targetVa();
        long edgeVa = edge.va();
        SoftBodySharedSettings.addEdgeConstraint(settingsVa, edgeVa);
    }

    /**
     * Add the specified face.
     *
     * @param face the face to add (not null, unaffected)
     */
    public void addFace(Face face) {
        long settingsVa = targetVa();
        long faceVa = face.va();
        SoftBodySharedSettings.addFace(settingsVa, faceVa);
    }

    /**
     * Append the specified inverse-bind matrix.
     *
     * @param invBind the matrix to add (not null)
     */
    public void addInvBindMatrix(InvBind invBind) {
        long settingsVa = targetVa();
        long bindVa = invBind.va();
        SoftBodySharedSettings.addInvBindMatrix(settingsVa, bindVa);
    }

    /**
     * Add the specified skinning constraint.
     *
     * @param skinned the constraint to add (not null)
     */
    public void addSkinnedConstraint(Skinned skinned) {
        long settingsVa = targetVa();
        long skinnedVa = skinned.va();
        SoftBodySharedSettings.addSkinnedConstraint(settingsVa, skinnedVa);
    }

    /**
     * Add the specified vertex.
     *
     * @param vertex the vertex to add (not null, unaffected)
     */
    public void addVertex(Vertex vertex) {
        long settingsVa = targetVa();
        long vertexVa = vertex.va();
        SoftBodySharedSettings.addVertex(settingsVa, vertexVa);
    }

    /**
     * Add the specified volume constraint.
     *
     * @param volume the constraint to add (not null, unaffected)
     */
    public void addVolumeConstraint(Volume volume) {
        long settingsVa = targetVa();
        long volumeVa = volume.va();
        SoftBodySharedSettings.addVolumeConstraint(settingsVa, volumeVa);
    }

    /**
     * Calculate the initial lengths of all edges in the body.
     */
    public void calculateEdgeLengths() {
        long settingsVa = targetVa();
        SoftBodySharedSettings.calculateEdgeLengths(settingsVa);
    }

    /**
     * Calculate the information needed for skinned constraint normals.
     */
    public void calculateSkinnedConstraintNormals() {
        long settingsVa = targetVa();
        SoftBodySharedSettings.calculateSkinnedConstraintNormals(settingsVa);
    }

    /**
     * Calculate the initial volumes of all tetrahedra in the body.
     */
    public void calculateVolumeConstraintVolumes() {
        long settingsVa = targetVa();
        SoftBodySharedSettings.calculateVolumeConstraintVolumes(settingsVa);
    }

    /**
     * Automatically generate constraints based on the faces.
     *
     * @param vertexAttributes the desired attributes (one for each vertex)
     * @param numAttributes the number of attributes provided (&ge;0)
     * @param bendType the desired type of bend constraint (not null,
     * default=Distance)
     */
    public void createConstraints(ConstVertexAttributes vertexAttributes,
            int numAttributes, EBendType bendType) {
        createConstraints(
                new ConstVertexAttributes[]{vertexAttributes}, bendType);
    }

    /**
     * Automatically generate constraints based on the faces.
     *
     * @param vertexAttributes the desired attributes (one for each vertex)
     * @param bendType the desired type of bend constraint (not null,
     * default=Distance)
     */
    public void createConstraints(
            ConstVertexAttributes[] vertexAttributes, EBendType bendType) {
        createConstraints(
                vertexAttributes, bendType, Jolt.degreesToRadians(8f));
    }

    /**
     * Automatically generate constraints based on the faces.
     *
     * @param vertexAttributes the desired attributes (one for each vertex)
     * @param bendType the desired type of bend constraint (not null,
     * default=Distance)
     * @param angleTolerance the desired tolerance for creating shear edges (in
     * radians, default=2*Pi/45)
     */
    public void createConstraints(ConstVertexAttributes[] vertexAttributes,
            EBendType bendType, float angleTolerance) {
        long settingsVa = targetVa();
        int numAttributes = vertexAttributes.length;
        long[] attributeVas = new long[numAttributes];
        for (int i = 0; i < numAttributes; ++i) {
            attributeVas[i] = vertexAttributes[i].targetVa();
        }
        int ordinal = bendType.ordinal();
        SoftBodySharedSettings.createConstraints(
                settingsVa, attributeVas, ordinal, angleTolerance);
    }

    /**
     * Optimize the settings without writing any results.
     */
    public void optimize() {
        long settingsVa = targetVa();
        SoftBodySharedSettings.optimize(settingsVa);
    }

    /**
     * Replace the materials. (native attribute: mMaterials)
     *
     * @param material the desired material, or {@code null}
     */
    public void setMaterials(PhysicsMaterial material) {
        long settingsVa = targetVa();
        long materialVa = (material == null) ? 0L : material.va();
        SoftBodySharedSettings.setMaterialsSingle(settingsVa, materialVa);
    }

    /**
     * Alter the size of every particle.
     *
     * @param radius the desired radius (&ge;0, default=0)
     */
    public void setVertexRadius(float radius) {
        long settingsVa = targetVa();
        SoftBodySharedSettings.setVertexRadius(settingsVa, radius);
    }
    // *************************************************************************
    // ConstSoftBodySharedSettings methods

    /**
     * Count the edge constraints. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    @Override
    public int countEdgeConstraints() {
        long settingsVa = targetVa();
        int result = SoftBodySharedSettings.countEdgeConstraints(settingsVa);

        return result;
    }

    /**
     * Count the faces. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    @Override
    public int countFaces() {
        long settingsVa = targetVa();
        int result = SoftBodySharedSettings.countFaces(settingsVa);

        return result;
    }

    /**
     * Count the pinned vertices. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    @Override
    public int countPinnedVertices() {
        long settingsVa = targetVa();
        int result = SoftBodySharedSettings.countPinnedVertices(settingsVa);

        return result;
    }

    /**
     * Count the vertices. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    @Override
    public int countVertices() {
        long settingsVa = targetVa();
        int result = SoftBodySharedSettings.countVertices(settingsVa);

        return result;
    }

    /**
     * Count the volume constraints. The settings are unaffected.
     *
     * @return the count (&ge;0)
     */
    @Override
    public int countVolumeConstraints() {
        long settingsVa = targetVa();
        int result = SoftBodySharedSettings.countVolumeConstraints(settingsVa);

        return result;
    }

    /**
     * Return the radius of each particle. The settings are unaffected. (native
     * attribute: mVertexRadius)
     *
     * @return the radius (in meters)
     */
    @Override
    public float getVertexRadius() {
        long settingsVa = targetVa();
        float result = SoftBodySharedSettings.getVertexRadius(settingsVa);

        return result;
    }

    /**
     * Write the vertex indices of all edges to the specified buffer and advance
     * the buffer's position. The settings are unaffected.
     *
     * @param storeIndices the destination buffer (not null, modified)
     */
    @Override
    public void putEdgeIndices(IntBuffer storeIndices) {
        long settingsVa = targetVa();
        int bufferPosition = storeIndices.position();
        bufferPosition = SoftBodySharedSettings.putEdgeIndices(
                settingsVa, bufferPosition, storeIndices);
        storeIndices.position(bufferPosition);
    }

    /**
     * Write the vertex indices of all faces to the specified buffer and advance
     * the buffer's position. The settings are unaffected.
     *
     * @param storeIndices the destination buffer (not null, modified)
     */
    @Override
    public void putFaceIndices(IntBuffer storeIndices) {
        long settingsVa = targetVa();
        int bufferPosition = storeIndices.position();
        bufferPosition = SoftBodySharedSettings.putFaceIndices(
                settingsVa, bufferPosition, storeIndices);
        storeIndices.position(bufferPosition);
    }
    // *************************************************************************
    // Ref methods

    /**
     * Temporarily access the referenced {@code SoftBodySharedSettings}.
     *
     * @return a new JVM object with the pre-existing native object assigned
     */
    @Override
    public SoftBodySharedSettings getPtr() {
        long settingsVa = targetVa();
        SoftBodySharedSettings result = new SoftBodySharedSettings(settingsVa);

        return result;
    }

    /**
     * Return the address of the native {@code SoftBodySharedSettings}. No
     * objects are affected.
     *
     * @return a virtual address (not zero)
     */
    @Override
    public long targetVa() {
        long refVa = va();
        long result = getPtr(refVa);

        return result;
    }

    /**
     * Create another counted reference to the native
     * {@code SoftBodySharedSettings}.
     *
     * @return a new JVM object with a new native object assigned
     */
    @Override
    public SoftBodySharedSettingsRef toRef() {
        long refVa = va();
        long copyVa = copy(refVa);
        SoftBodySharedSettingsRef result
                = new SoftBodySharedSettingsRef(copyVa, true);

        return result;
    }
    // *************************************************************************
    // native private methods

    native private static long copy(long refVa);

    native private static long createEmpty();

    native private static void free(long refVa);

    native private static long getPtr(long refVa);
}
