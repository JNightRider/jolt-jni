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
package com.github.stephengold.joltjni;

import com.github.stephengold.joltjni.template.Ref;

/**
 * A counted reference to a {@code VehicleCollisionTester}. (native type:
 * {@code Ref<VehicleCollisionTester>})
 *
 * @author Stephen Gold sgold@sonic.net
 */
final public class VehicleCollisionTesterRef extends Ref {
    // *************************************************************************
    // constructors

    /**
     * Instantiate a reference with the specified native object assigned.
     *
     * @param refVa the virtual address of the native object to assign (not
     * zero)
     * @param owner true &rarr; make the current object the owner, false &rarr;
     * the current object isn't the owner
     */
    VehicleCollisionTesterRef(long refVa, boolean owner) {
        Runnable freeingAction = owner ? () -> free(refVa) : null;
        setVirtualAddress(refVa, freeingAction);
    }
    // *************************************************************************
    // Ref methods

    /**
     * Temporarily access the referenced {@code VehicleCollisionTester}.
     *
     * @return a new JVM object that refers to the pre-existing native object
     */
    @Override
    public VehicleCollisionTester getPtr() {
        long refVa = va();
        long testerVa = getPtr(refVa);
        VehicleCollisionTester result = new VehicleCollisionTester(testerVa);

        return result;
    }

    /**
     * Create a counted reference to the native {@code VehicleCollisionTester}.
     *
     * @return a new JVM object with a new native object assigned
     */
    @Override
    public VehicleCollisionTesterRef toRef() {
        long refVa = va();
        long copyVa = copy(refVa);
        VehicleCollisionTesterRef result
                = new VehicleCollisionTesterRef(copyVa, true);

        return result;
    }
    // *************************************************************************
    // native private methods

    native private static long copy(long refVa);

    native private static void free(long refVa);

    native private static long getPtr(long refVa);
}
