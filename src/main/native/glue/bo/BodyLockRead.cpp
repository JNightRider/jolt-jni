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
#include "Jolt/Physics/Body/BodyLock.h"
#include "auto/com_github_stephengold_joltjni_BodyLockRead.h"
#include "glue/glue.h"

using namespace JPH;

/*
 * Class:     com_github_stephengold_joltjni_BodyLockRead
 * Method:    createBodyLockRead
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_BodyLockRead_createBodyLockRead
  (JNIEnv *, jclass, jlong lockVa, jint bodyId) {
    const BodyLockInterface * const pInterface
            = reinterpret_cast<BodyLockInterface *> (lockVa);
    const BodyID id(bodyId);
    BodyLockRead * const pResult = new BodyLockRead(*pInterface, id);
    TRACE_NEW("BodyLockRead", pResult)
    return reinterpret_cast<jlong> (pResult);
}

/*
 * Class:     com_github_stephengold_joltjni_BodyLockRead
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_BodyLockRead_free
  BODYOF_FREE(BodyLockRead)

/*
 * Class:     com_github_stephengold_joltjni_BodyLockRead
 * Method:    getBody
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_BodyLockRead_getBody
  (JNIEnv *, jclass, jlong lockVa) {
    const BodyLockRead * const pLock
            = reinterpret_cast<BodyLockRead *> (lockVa);
    const Body& result = pLock->GetBody();
    return reinterpret_cast<jlong> (&result);
}

/*
 * Class:     com_github_stephengold_joltjni_BodyLockRead
 * Method:    releaseLock
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_BodyLockRead_releaseLock
  (JNIEnv *, jclass, jlong lockVa) {
    BodyLockRead * const pLock = reinterpret_cast<BodyLockRead *> (lockVa);
    pLock->ReleaseLock();
}

/*
 * Class:     com_github_stephengold_joltjni_BodyLockRead
 * Method:    succeeded
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_github_stephengold_joltjni_BodyLockRead_succeeded
  (JNIEnv *, jclass, jlong lockVa) {
    const BodyLockRead * const pLock
            = reinterpret_cast<BodyLockRead *> (lockVa);
    const bool result = pLock->Succeeded();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_BodyLockRead
 * Method:    succeededAndIsInBroadPhase
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_github_stephengold_joltjni_BodyLockRead_succeededAndIsInBroadPhase
  (JNIEnv *, jclass, jlong lockVa) {
    const BodyLockRead * const pLock
            = reinterpret_cast<BodyLockRead *> (lockVa);
    const bool result = pLock->SucceededAndIsInBroadPhase();
    return result;
}