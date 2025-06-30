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
#include "Jolt/Physics/Collision/GroupFilter.h"

#include "auto/com_github_stephengold_joltjni_GroupFilter.h"
#include "auto/com_github_stephengold_joltjni_GroupFilterRef.h"
#include "glue/glue.h"

using namespace JPH;

IMPLEMENT_REF(GroupFilter,
  Java_com_github_stephengold_joltjni_GroupFilterRef_copy,
  Java_com_github_stephengold_joltjni_GroupFilterRef_createDefault,
  Java_com_github_stephengold_joltjni_GroupFilterRef_free,
  Java_com_github_stephengold_joltjni_GroupFilterRef_getPtr,
  Java_com_github_stephengold_joltjni_GroupFilterRef_toRefC)

/*
 * Class:     com_github_stephengold_joltjni_GroupFilter
 * Method:    canCollide
 * Signature: (JJJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_github_stephengold_joltjni_GroupFilter_canCollide
  (JNIEnv *, jclass, jlong filterVa, jlong group1Va, jlong group2Va) {
    const GroupFilter * const pFilter
            = reinterpret_cast<GroupFilter *> (filterVa);
    const CollisionGroup * const pGroup1
            = reinterpret_cast<CollisionGroup *> (group1Va);
    const CollisionGroup * const pGroup2
            = reinterpret_cast<CollisionGroup *> (group2Va);
    const bool result = pFilter->CanCollide(*pGroup1, *pGroup2);
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_GroupFilter
 * Method:    getRefCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_GroupFilter_getRefCount
  (JNIEnv *, jclass, jlong filterVa) {
    const GroupFilter * const pFilter
            = reinterpret_cast<GroupFilter *> (filterVa);
    const uint32 result = pFilter->GetRefCount();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_GroupFilter
 * Method:    saveBinaryState
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_GroupFilter_saveBinaryState
  (JNIEnv *, jclass, jlong filterVa, jlong streamVa) {
    const GroupFilter * const pFilter
            = reinterpret_cast<GroupFilter *> (filterVa);
    StreamOut * const pStream = reinterpret_cast<StreamOut *> (streamVa);
    pFilter->SaveBinaryState(*pStream);
}

/*
 * Class:     com_github_stephengold_joltjni_GroupFilter
 * Method:    setEmbedded
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_GroupFilter_setEmbedded
  (JNIEnv *, jclass, jlong filterVa) {
    GroupFilter * const pFilter = reinterpret_cast<GroupFilter *> (filterVa);
    pFilter->SetEmbedded();
}

/*
 * Class:     com_github_stephengold_joltjni_GroupFilter
 * Method:    sRestoreFromBinaryState
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_GroupFilter_sRestoreFromBinaryState
  (JNIEnv *, jclass, jlong streamVa) {
    StreamIn * const pStream = reinterpret_cast<StreamIn *> (streamVa);
    Result<Ref<GroupFilter>> * const pResult = new Result<Ref<GroupFilter>>();
    TRACE_NEW("Result<Ref<GroupFilter>>", pResult);
    *pResult = GroupFilter::sRestoreFromBinaryState(*pStream);
    return reinterpret_cast<jlong> (pResult);
}

/*
 * Class:     com_github_stephengold_joltjni_GroupFilter
 * Method:    toRef
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_GroupFilter_toRef
  (JNIEnv *, jclass, jlong filterVa) {
    GroupFilter * const pFilter = reinterpret_cast<GroupFilter *> (filterVa);
    Ref<GroupFilter> * const pResult = new Ref<GroupFilter>(pFilter);
    TRACE_NEW("Ref<GroupFilter>", pResult)
    return reinterpret_cast<jlong> (pResult);
}