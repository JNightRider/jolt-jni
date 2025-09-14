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
#include "Jolt/Physics/Collision/ContactListener.h"
#include "auto/com_github_stephengold_joltjni_ContactManifold.h"
#include "glue/glue.h"

using namespace JPH;

/*
 * Class:     com_github_stephengold_joltjni_ContactManifold
 * Method:    getBaseOffset
 * Signature: (JLjava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_ContactManifold_getBaseOffset
  (JNIEnv *pEnv, jclass, jlong manifoldVa, jobject storeDoubles) {
    const ContactManifold * const pManifold
            = reinterpret_cast<ContactManifold *> (manifoldVa);
    DIRECT_DOUBLE_BUFFER(pEnv, storeDoubles, pDoubles, capacityDoubles);
    JPH_ASSERT(capacityDoubles >= 3);
    const RVec3& result = pManifold->mBaseOffset;
    pDoubles[0] = result.GetX();
    pDoubles[1] = result.GetY();
    pDoubles[2] = result.GetZ();
}

/*
 * Class:     com_github_stephengold_joltjni_ContactManifold
 * Method:    getPenetrationDepth
 * Signature: (J)F
 */
JNIEXPORT jfloat JNICALL Java_com_github_stephengold_joltjni_ContactManifold_getPenetrationDepth
  (JNIEnv *, jclass, jlong manifoldVa) {
    const ContactManifold * const pManifold
            = reinterpret_cast<ContactManifold *> (manifoldVa);
    const float result = pManifold->mPenetrationDepth;
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_ContactManifold
 * Method:    getSubShapeId1
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_ContactManifold_getSubShapeId1
  (JNIEnv *, jclass, jlong manifoldVa) {
    const ContactManifold * const pManifold
            = reinterpret_cast<ContactManifold *> (manifoldVa);
    const SubShapeID result = pManifold->mSubShapeID1;
    return result.GetValue();
}

/*
 * Class:     com_github_stephengold_joltjni_ContactManifold
 * Method:    getSubShapeId2
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_ContactManifold_getSubShapeId2
  (JNIEnv *, jclass, jlong manifoldVa) {
    const ContactManifold * const pManifold
            = reinterpret_cast<ContactManifold *> (manifoldVa);
    const SubShapeID result = pManifold->mSubShapeID2;
    return result.GetValue();
}

/*
 * Class:     com_github_stephengold_joltjni_ContactManifold
 * Method:    getWorldSpaceNormal
 * Signature: (JLjava/nio/FloatBuffer;)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_ContactManifold_getWorldSpaceNormal
  (JNIEnv *pEnv, jclass, jlong manifoldVa, jobject storeFloats) {
    const ContactManifold * const pManifold
            = reinterpret_cast<ContactManifold *> (manifoldVa);
    DIRECT_FLOAT_BUFFER(pEnv, storeFloats, pFloats, capacityFloats);
    JPH_ASSERT(capacityFloats >= 3);
    const Vec3& result = pManifold->mWorldSpaceNormal;
    pFloats[0] = result.GetX();
    pFloats[1] = result.GetY();
    pFloats[2] = result.GetZ();
}