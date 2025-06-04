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
package com.github.stephengold.joltjni.readonly;

import com.github.stephengold.joltjni.Color;
import com.github.stephengold.joltjni.PhysicsMaterialRefC;
import com.github.stephengold.joltjni.StreamOut;

/**
 * Read-only access to a {@code PhysicsMaterial}. (native type: const
 * PhysicsMaterial)
 *
 * @author Stephen Gold sgold@sonic.net
 */
public interface ConstPhysicsMaterial extends ConstSerializableObject {
    /**
     * Copy the debug color. The material is unaffected.
     *
     * @return a new object
     */
    Color getDebugColor();

    /**
     * Return the debug name. The material is unaffected.
     *
     * @return a string of text or {@code null}
     */
    String getDebugName();

    /**
     * Save the material to the specified binary stream. The material is
     * unaffected.
     *
     * @param stream the stream to write to (not null)
     */
    void saveBinaryState(StreamOut stream);

    /**
     * Create a counted reference to the native {@code PhysicsMaterial}.
     *
     * @return a new JVM object with a new native object assigned
     */
    PhysicsMaterialRefC toRefC();
}
