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
#include "auto/com_github_stephengold_joltjni_std_Mt19937.h"
#include "glue/glue.h"

using namespace std;

/*
 * Class:     com_github_stephengold_joltjni_std_Mt19937
 * Method:    createDefault
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_std_Mt19937_createDefault
  BODYOF_CREATE_DEFAULT(mt19937)

/*
 * Class:     com_github_stephengold_joltjni_std_Mt19937
 * Method:    createSeeded
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_std_Mt19937_createSeeded
  (JNIEnv *, jclass, jint seed) {
    mt19937 * const pResult = new mt19937(seed);
    TRACE_NEW("mt19937", pResult)
    return reinterpret_cast<jlong> (pResult);
}

/*
 * Class:     com_github_stephengold_joltjni_std_Mt19937
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_std_Mt19937_free
  BODYOF_FREE(mt19937)

/*
 * Class:     com_github_stephengold_joltjni_std_Mt19937
 * Method:    nextInt
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_std_Mt19937_nextInt
  (JNIEnv *, jclass, jlong generatorVa) {
    mt19937 * const pGenerator
            = reinterpret_cast<mt19937 *> (generatorVa);
    mt19937::result_type result = (*pGenerator)();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_std_Mt19937
 * Method:    setSeed
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_std_Mt19937_setSeed
  (JNIEnv *, jclass, jlong generatorVa, jint value) {
    mt19937 * const pGenerator
            = reinterpret_cast<mt19937 *> (generatorVa);
    pGenerator->seed(value);
}