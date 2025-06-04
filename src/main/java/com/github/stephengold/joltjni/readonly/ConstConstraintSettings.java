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

import com.github.stephengold.joltjni.StreamOut;

/**
 * Read-only access to a {@code ConstraintSettings} object. (native type: const
 * ConstraintSettings)
 *
 * @author Stephen Gold sgold@sonic.net
 */
public interface ConstConstraintSettings extends ConstSerializableObject {
    /**
     * Return the constraint's priority when solving. The settings are
     * unaffected.
     *
     * @return the priority level
     */
    int getConstraintPriority();

    /**
     * Return the constraint's controller type. The settings are unaffected.
     *
     * @return a numeric code, such as {@code VehicleController.motorcycleType}
     */
    int getControllerType();

    /**
     * Return the size of the constraint in debug renders. The settings are
     * unaffected.
     *
     * @return the size
     */
    float getDrawConstraintSize();

    /**
     * Test whether the constraint will be enabled initially. The settings are
     * unaffected.
     *
     * @return {@code true} if enabled, otherwise {@code false}
     */
    boolean getEnabled();

    /**
     * Return the override for the number of position iterations used in the
     * solver. The settings are unaffected.
     *
     * @return the number of iterations, or 0 to use the default in
     * {@code PhysicsSettings}
     */
    int getNumPositionStepsOverride();

    /**
     * Return the override for the number of velocity iterations used in the
     * solver. The settings are unaffected.
     *
     * @return the number of iterations, or 0 to use the default configured in
     * {@code PhysicsSettings}
     */
    int getNumVelocityStepsOverride();

    /**
     * Save the settings to the specified binary stream. The settings are
     * unaffected.
     *
     * @param stream the stream to write to (not null)
     */
    void saveBinaryState(StreamOut stream);
}
