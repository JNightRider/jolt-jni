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

/*
 * Author: Stephen Gold
 */
#include "Jolt/Jolt.h"
#include "Jolt/Physics/Collision/Shape/ConvexHullShape.h"
#include "auto/com_github_stephengold_joltjni_ConvexHullShape.h"
#include "glue/glue.h"

using namespace JPH;

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    drawShrunkShape
 * Signature: (JJJFFF)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_drawShrunkShape
  (JNIEnv *, jclass, jlong shapeVa, jlong rendererVa, jlong transformVa,
  jfloat scaleX, jfloat scaleY, jfloat scaleZ) {
#ifdef JPH_DEBUG_RENDERER
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    DebugRenderer * const pRenderer
            = reinterpret_cast<DebugRenderer *> (rendererVa);
    const RMat44 * const pTransform
            = reinterpret_cast<RMat44 *> (transformVa);
    const Vec3 scale(scaleX, scaleY, scaleZ);
    pShape->DrawShrunkShape(pRenderer, *pTransform, scale);
#endif
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getConvexRadius
 * Signature: (J)F
 */
JNIEXPORT jfloat JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getConvexRadius
  (JNIEnv *, jclass, jlong shapeVa) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    const float result = pShape->GetConvexRadius();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getFaceVertices
 * Signature: (JII[I)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getFaceVertices
  (JNIEnv *pEnv, jclass, jlong shapeVa, jint faceIndex, jint maxVertices,
  jintArray storeIndices) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    uint * const pTempArray = new uint[maxVertices];
    TRACE_NEW("uint[]", pTempArray)
    const uint result
            = pShape->GetFaceVertices(faceIndex, maxVertices, pTempArray);
    jboolean isCopy;
    jint * const pStoreJints = pEnv->GetIntArrayElements(storeIndices, &isCopy);
    for (uint32 i = 0; i < maxVertices; ++i) {
        pStoreJints[i] = pTempArray[i];
    }
    TRACE_DELETE("uint[]", pTempArray)
    delete[] pTempArray;
    pEnv->ReleaseIntArrayElements(storeIndices, pStoreJints, 0);
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getNumFaces
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getNumFaces
  (JNIEnv *, jclass, jlong shapeVa) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    const uint result = pShape->GetNumFaces();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getNumPlanes
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getNumPlanes
  (JNIEnv *, jclass, jlong shapeVa) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    const Array<Plane>& planes = pShape->GetPlanes();
    const Array<Plane>::size_type result = planes.size();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getNumPoints
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getNumPoints
  (JNIEnv *, jclass, jlong shapeVa) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    const uint result = pShape->GetNumPoints();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getNumVerticesInFace
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getNumVerticesInFace
  (JNIEnv *, jclass, jlong shapeVa, jint faceIndex) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    const uint result = pShape->GetNumVerticesInFace(faceIndex);
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getPlanes
 * Signature: (J[F)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getPlanes
  (JNIEnv *pEnv, jclass, jlong shapeVa, jfloatArray storeFloats) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    const Array<Plane>& planes = pShape->GetPlanes();
    jboolean isCopy;
    jfloat * const pFloats = pEnv->GetFloatArrayElements(storeFloats, &isCopy);
    for (size_t i = 0; i < planes.size(); ++i) {
        pFloats[4*i] = planes[i].GetNormal().GetX();
        pFloats[4*i + 1] = planes[i].GetNormal().GetY();
        pFloats[4*i + 2] = planes[i].GetNormal().GetZ();
        pFloats[4*i + 3] = planes[i].GetConstant();
    }
    pEnv->ReleaseFloatArrayElements(storeFloats, pFloats, 0);
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexHullShape
 * Method:    getPoint
 * Signature: (JILjava/nio/FloatBuffer;)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_ConvexHullShape_getPoint
  (JNIEnv *pEnv, jclass, jlong shapeVa, jint pointIndex, jobject storeFloats) {
    const ConvexHullShape * const pShape
            = reinterpret_cast<ConvexHullShape *> (shapeVa);
    DIRECT_FLOAT_BUFFER(pEnv, storeFloats, pFloats, capacityFloats);
    JPH_ASSERT(capacityFloats >= 3);
    const Vec3 result = pShape->GetPoint(pointIndex);
    pFloats[0] = result.GetX();
    pFloats[1] = result.GetY();
    pFloats[2] = result.GetZ();
}