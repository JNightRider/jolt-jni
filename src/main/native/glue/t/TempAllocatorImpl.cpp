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
#include "Jolt/Core/TempAllocator.h"

#include "auto/com_github_stephengold_joltjni_TempAllocatorImpl.h"
#include "glue/glue.h"
#include <iostream>

using namespace JPH;

/*
 * Class:     com_github_stephengold_joltjni_TempAllocatorImpl
 * Method:    create
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_TempAllocatorImpl_create
  (JNIEnv *, jclass, jint numBytes) {
#if defined(JPH_DEBUG) && !defined(JPH_DISABLE_CUSTOM_ALLOCATOR)
    if (!Allocate) {
        std::cerr << "Can't create a TempAllocatorImpl because no default allocator is registered!"
                << std::endl;
        return 0;
    }
#endif
    TempAllocatorImpl * const pResult = new TempAllocatorImpl(numBytes);
    TRACE_NEW("TempAllocatorImpl", pResult)
    return reinterpret_cast<jlong> (pResult);
}

/*
 * Class:     com_github_stephengold_joltjni_TempAllocatorImpl
 * Method:    getSize
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_TempAllocatorImpl_getSize
  (JNIEnv *, jclass, jlong allocatorVa) {
    const TempAllocatorImpl * const pAllocator
            = reinterpret_cast<TempAllocatorImpl *> (allocatorVa);
    const size_t result = pAllocator->GetSize();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_TempAllocatorImpl
 * Method:    getUsage
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_TempAllocatorImpl_getUsage
  (JNIEnv *, jclass, jlong allocatorVa) {
    const TempAllocatorImpl * const pAllocator
            = reinterpret_cast<TempAllocatorImpl *> (allocatorVa);
    const size_t result = pAllocator->GetUsage();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_TempAllocatorImpl
 * Method:    isEmpty
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_github_stephengold_joltjni_TempAllocatorImpl_isEmpty
  (JNIEnv *, jclass, jlong allocatorVa) {
    const TempAllocatorImpl * const pAllocator
            = reinterpret_cast<TempAllocatorImpl *> (allocatorVa);
    const bool result = pAllocator->IsEmpty();
    return result;
}