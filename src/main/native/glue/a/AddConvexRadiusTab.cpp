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

/*
 * Author: Stephen Gold
 */
#include "Jolt/Jolt.h"
#include "Jolt/Geometry/AABox.h"
#include "Jolt/Geometry/ConvexSupport.h"
#include "Jolt/Physics/Collision/Shape/ConvexShape.h"

#include "auto/com_github_stephengold_joltjni_AddConvexRadiusTab.h"
#include "glue/glue.h"

using namespace JPH;

/*
 * Class:     com_github_stephengold_joltjni_AddConvexRadiusTab
 * Method:    create
 * Signature: (JF)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_AddConvexRadiusTab_create
  (JNIEnv *, jclass, jlong tabVa, jfloat convexRadius) {
    const TransformedConvexObject<AABox> * const pTab
            = reinterpret_cast<TransformedConvexObject<AABox> *> (tabVa);
    AddConvexRadius<TransformedConvexObject<AABox>> * const pAdd
            = new AddConvexRadius<TransformedConvexObject<AABox>>(*pTab, convexRadius);
    TRACE_NEW("AddConvexRadius<TransformedConvexObject<AABox>>", pAdd)
    return reinterpret_cast<jlong> (pAdd);
}

/*
 * Class:     com_github_stephengold_joltjni_AddConvexRadiusTab
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_AddConvexRadiusTab_free
  BODYOF_FREE(AddConvexRadius<TransformedConvexObject<AABox>>)

/*
 * Class:     com_github_stephengold_joltjni_AddConvexRadiusTab
 * Method:    getSupport
 * Signature: (JFFF[F)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_AddConvexRadiusTab_getSupport
  (JNIEnv *pEnv, jclass, jlong addVa, jfloat dx, jfloat dy, jfloat dz,
  jfloatArray storeFloats) {
    const AddConvexRadius<TransformedConvexObject<AABox>> * const pAdd
            = reinterpret_cast<AddConvexRadius<TransformedConvexObject<AABox>> *> (addVa);
    const Vec3 direction(dx, dy, dz);
    const Vec3 result = pAdd->GetSupport(direction);
    jboolean isCopy;
    jfloat * const pStoreFloats
            = pEnv->GetFloatArrayElements(storeFloats, &isCopy);
    pStoreFloats[0] = result.GetX();
    pStoreFloats[1] = result.GetY();
    pStoreFloats[2] = result.GetZ();
    pEnv->ReleaseFloatArrayElements(storeFloats, pStoreFloats, 0);
}