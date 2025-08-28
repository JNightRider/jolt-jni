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
package com.github.stephengold.joltjni;

import com.github.stephengold.joltjni.template.Result;

/**
 * Either an error or a {@code SoftBodySharedSettingsRef}. (native type:
 * {@code Result<Ref<SoftBodySharedSettings>>})
 *
 * @author Stephen Gold sgold@sonic.net
 */
final public class SettingsResult extends Result<SoftBodySharedSettingsRef> {
    // *************************************************************************
    // constructors

    /**
     * Instantiate a result with the specified native object assigned.
     *
     * @param resultVa the virtual address of the native object to assign (not
     * zero)
     * @param owner {@code true} &rarr; make the JVM object the owner,
     * {@code false} &rarr; it isn't the owner
     */
    SettingsResult(long resultVa, boolean owner) {
        Runnable freeingAction = owner ? () -> free(resultVa) : null;
        setVirtualAddress(resultVa, freeingAction);
    }
    // *************************************************************************
    // Result<SoftBodySharedSettingsRef> methods

    /**
     * Return the {@code ShapeRefC}.
     *
     * @return a new JVM object with a new native reference assigned
     */
    @Override
    public SoftBodySharedSettingsRef get() {
        long resultVa = va();
        long shapeRefVa = get(resultVa);
        SoftBodySharedSettingsRef result
                = new SoftBodySharedSettingsRef(shapeRefVa, true);

        return result;
    }
    // *************************************************************************
    // native methods

    native private static void free(long resultVa);

    native private static long get(long resultVa);

    @Override
    native protected String getError(long resultVa);

    @Override
    native protected boolean hasError(long resultVa);

    @Override
    native protected boolean isValid(long resultVa);
}
