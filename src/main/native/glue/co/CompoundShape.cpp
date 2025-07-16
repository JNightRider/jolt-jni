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
#include "Jolt/Physics/Collision/Shape/CompoundShape.h"
#include "auto/com_github_stephengold_joltjni_CompoundShape.h"
#include "glue/glue.h"

using namespace JPH;

/*
 * Class:     com_github_stephengold_joltjni_CompoundShape
 * Method:    getNumSubShapes
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_github_stephengold_joltjni_CompoundShape_getNumSubShapes
  (JNIEnv *, jclass, jlong shapeVa) {
    const CompoundShape * const pShape
            = reinterpret_cast<CompoundShape *> (shapeVa);
    const uint result = pShape->GetNumSubShapes();
    return result;
}

/*
 * Class:     com_github_stephengold_joltjni_CompoundShape
 * Method:    getSubShape
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_CompoundShape_getSubShape
  (JNIEnv *, jclass, jlong shapeVa, jint subShapeIndex) {
    const CompoundShape * const pShape
            = reinterpret_cast<CompoundShape *> (shapeVa);
    const CompoundShape::SubShape * const pResult
            = &pShape->GetSubShape(subShapeIndex);
    return reinterpret_cast<jlong> (pResult);
}

/*
 * Class:     com_github_stephengold_joltjni_CompoundShape
 * Method:    getSubShapeIdFromIndex
 * Signature: (JIJ)J
 */
JNIEXPORT jlong JNICALL Java_com_github_stephengold_joltjni_CompoundShape_getSubShapeIdFromIndex
  (JNIEnv *, jclass, jlong shapeVa, jint subShapeIndex, jlong parentVa) {
    const CompoundShape * const pShape
            = reinterpret_cast<CompoundShape *> (shapeVa);
    const SubShapeIDCreator * const pParent
            = reinterpret_cast<SubShapeIDCreator *> (parentVa);
    SubShapeIDCreator * const pResult = new SubShapeIDCreator();
    TRACE_NEW("SubShapeIDCreator", pResult)
    *pResult = pShape->GetSubShapeIDFromIndex(subShapeIndex, *pParent);
    return reinterpret_cast<jlong> (pResult);
}

/*
 * Class:     com_github_stephengold_joltjni_CompoundShape
 * Method:    restoreSubShapeState
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_github_stephengold_joltjni_CompoundShape_restoreSubShapeState
  (JNIEnv *, jclass, jlong shapeVa, jlong listVa) {
    CompoundShape * const pCompound
            = reinterpret_cast<CompoundShape *> (shapeVa);
    const ShapeList * const pList = reinterpret_cast<ShapeList *> (listVa);
    const ShapeList::size_type numShapes = pList->size();
    ShapeRefC * const pSubShapes = new ShapeRefC[numShapes];
    for (size_t i = 0; i < numShapes; ++i) {
        pSubShapes[i] = pList->at(i);
    }
    pCompound->RestoreSubShapeState(pSubShapes, numShapes);
    delete[] pSubShapes;
}