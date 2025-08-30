/*
Copyright (c) 2024 Stephen Gold

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
#include "Jolt/Physics/Collision/Shape/ConvexShape.h"
#include "auto/com_github_stephengold_joltjni_ConvexShape.h"
#include "glue/glue.h"

using namespace JPH;

/*
 * Class:     com_github_stephengold_joltjni_ConvexShape
 * Method:    getDensity
 * Signature: (J)F
 */
JNIEXPORT jfloat JNICALL Java_com_github_stephengold_joltjni_ConvexShape_getDensity
  (JNIEnv *, jclass, jlong shapeVa) {
    const ConvexShape * const pShape
            = reinterpret_cast<ConvexShape *> (shapeVa);
    const float result = pShape->GetDensity();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexShape
 * Method:    getSubmergedVolume
 * Signature: (JJLjava/nio/FloatBuffer;DDD)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_ConvexShape_getSubmergedVolume
  (JNIEnv *pEnv, jclass, jlong shapeVa, jlong comTransformVa,
  jobject floatBuffer, jdouble baseX, jdouble baseY, jdouble baseZ) {
    const ConvexShape * const pShape
            = reinterpret_cast<ConvexShape *> (shapeVa);
    const Mat44 * const pComTransform
            = reinterpret_cast<Mat44 *> (comTransformVa);
    DIRECT_FLOAT_BUFFER(pEnv, floatBuffer, pFloats, capacityFloats);
    JPH_ASSERT(capacityFloats >= 7);
    const Vec3 scale(pFloats[0], pFloats[1], pFloats[2]);
    const Vec3 normal(pFloats[3], pFloats[4], pFloats[5]);
    const Plane surface(normal, pFloats[6]);
    float totalVolume, submergedVolume;
    Vec3 centerOfBuoyancy;
    const RVec3 baseOffset(baseX, baseY, baseZ);
    pShape->GetSubmergedVolume(
            *pComTransform, scale, surface, totalVolume, submergedVolume,
            centerOfBuoyancy JPH_IF_DEBUG_RENDERER(, baseOffset));
    pFloats[0] = totalVolume;
    pFloats[1] = submergedVolume;
    pFloats[2] = centerOfBuoyancy.GetX();
    pFloats[3] = centerOfBuoyancy.GetY();
    pFloats[4] = centerOfBuoyancy.GetZ();
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexShape
 * Method:    getSupportFunction
 * Signature: (JIJFFF)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_ConvexShape_getSupportFunction
  (JNIEnv *, jclass, jlong shapeVa, jint ordinal, jlong bufferVa,
  jfloat scaleX, jfloat scaleY, jfloat scaleZ) {
    const ConvexShape * const pShape
            = reinterpret_cast<ConvexShape *> (shapeVa);
    const ConvexShape::ESupportMode supportMode
            = (ConvexShape::ESupportMode) ordinal;
    ConvexShape::SupportBuffer * const pBuffer
            = reinterpret_cast<ConvexShape::SupportBuffer *> (bufferVa);
    const Vec3 scale(scaleX, scaleY, scaleZ);
    const ConvexShape::Support * const pResult
            = pShape->GetSupportFunction(supportMode, *pBuffer, scale);
    return reinterpret_cast<jlong> (pResult);
}

/*
 * Class:     com_github_stephengold_joltjni_ConvexShape
 * Method:    setDensity
 * Signature: (JF)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_ConvexShape_setDensity
  (JNIEnv *, jclass, jlong shapeVa, jfloat density) {
    ConvexShape * const pShape = reinterpret_cast<ConvexShape *> (shapeVa);
    pShape->SetDensity(density);
}