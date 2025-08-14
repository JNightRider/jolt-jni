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
package com.github.stephengold.joltjni;

import com.github.stephengold.joltjni.enumerate.ESpringMode;
import com.github.stephengold.joltjni.readonly.ConstSpringSettings;
import com.github.stephengold.joltjni.template.Ref;
import com.github.stephengold.joltjni.template.RefTarget;

/**
 * Settings used to construct a spring.
 *
 * @author Stephen Gold sgold@sonic.net
 */
final public class SpringSettings
        extends JoltPhysicsObject
        implements ConstSpringSettings {
    // *************************************************************************
    // constructors

    /**
     * Instantiate default settings.
     */
    public SpringSettings() {
        long settingsVa = createDefault();
        setVirtualAddress(settingsVa, () -> free(settingsVa));
    }

    /**
     * Instantiate a copy of the specified settings.
     *
     * @param original the settings to copy (not {@code null}, unaffected)
     */
    public SpringSettings(ConstSpringSettings original) {
        long originalVa = original.targetVa();
        long copyVa = createCopy(originalVa);
        setVirtualAddress(copyVa, () -> free(copyVa));
    }

    /**
     * Instantiate settings with the specified native object assigned but not
     * owned.
     *
     * @param container the containing object, or {@code null} if none
     * @param settingsVa the virtual address of the native object to assign (not
     * zero)
     */
    SpringSettings(JoltPhysicsObject container, long settingsVa) {
        super(container, settingsVa);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Load settings from the specified binary stream.
     *
     * @param stream the stream to read from (not null)
     */
    public void restoreBinaryState(StreamIn stream) {
        long settingsVa = va();
        long streamVa = stream.va();
        restoreBinaryState(settingsVa, streamVa);
    }

    /**
     * Alter the spring's damping. (native attribute: mDamping)
     * <p>
     * When the mode is FrequencyAndDamping, this is the damping ratio (0 = no
     * damping, 1 = critical damping). When the mode is StiffnessAndDamping,
     * this is the damping coefficient {@code c} in the spring equation:
     * {@code F = -k * x - c * v} for a linear spring or
     * {@code T = -k * theta - c * w} for an angular spring.
     *
     * @param damping the desired damping (default=0)
     * @return the modified settings, for chaining
     */
    public SpringSettings setDamping(float damping) {
        long settingsVa = va();
        setDamping(settingsVa, damping);

        return this;
    }

    /**
     * Alter the spring's frequency. (native attribute: mFrequency)
     * <p>
     * Effective only when the mode is FrequencyAndDamping. If positive, the
     * constraint will have soft limits, and mFrequency specifies the
     * oscillation frequency in Hz. Otherwise, the constraint will have hard
     * limits.
     *
     * @param frequency the desired frequency (default=0)
     * @return the modified settings, for chaining
     */
    public SpringSettings setFrequency(float frequency) {
        long settingsVa = va();
        setFrequency(settingsVa, frequency);

        return this;
    }

    /**
     * Alter how the spring is specified. (native attribute: mMode)
     *
     * @param mode the desired mode (not null, default=FrequencyAndDamping)
     * @return the modified settings, for chaining
     */
    public SpringSettings setMode(ESpringMode mode) {
        long settingsVa = va();
        int ordinal = mode.ordinal();
        setMode(settingsVa, ordinal);

        return this;
    }

    /**
     * Alter the stiffness of the spring. (native attribute: mStiffness)
     * <p>
     * Effective only when the mode is StiffnessAndDamping. If positive, the
     * constraint will have soft limits, and mStiffness specifies the stiffness
     * {@code k} in the spring equation: {@code F = -k * x - c * v} for a linear
     * spring or {@code T = -k *
     * theta - c * w} for an angular spring.
     * <p>
     * If negative, the constraint will have hard limits.
     *
     * @param stiffness (default=0)
     * @return the modified settings, for chaining
     */
    public SpringSettings setStiffness(float stiffness) {
        long settingsVa = va();
        setStiffness(settingsVa, stiffness);

        return this;
    }
    // *************************************************************************
    // ConstSpringSettings methods

    /**
     * Access the underlying {@code Constraint}, if any.
     *
     * @return the pre-existing instance, or {@code null} if none
     */
    @Override
    public Constraint getConstraint() {
        JoltPhysicsObject container = getContainingObject();
        RefTarget result;
        if (!(container instanceof Ref)) {
            result = null;
        } else {
            result = ((Ref) container).getPtr();
            if (!(result instanceof Constraint)) {
                result = null;
            }
        }

        return (Constraint) result;
    }

    /**
     * Access the underlying {@code ConstraintSettings}, if any.
     *
     * @return the pre-existing instance, or {@code null} if none
     */
    @Override
    public ConstraintSettings getConstraintSettings() {
        JoltPhysicsObject container = getContainingObject();
        RefTarget result;
        if (!(container instanceof Ref)) {
            result = null;
        } else {
            result = ((Ref) container).getPtr();
            if (!(result instanceof ConstraintSettings)) {
                result = null;
            }
        }

        return (ConstraintSettings) result;
    }

    /**
     * Return the spring's damping. The settings are unaffected. (native
     * attribute: mDamping)
     * <p>
     * When the mode is FrequencyAndDamping, this is the damping ratio (0 = no
     * damping, 1 = critical damping). When the mode is StiffnessAndDamping,
     * this is the damping coefficient {@code c} in the spring equation:
     * {@code F = -k * x - c * v} for a linear spring or
     * {@code T = -k * theta - c * w} for an angular spring.
     *
     * @return the damping value
     */
    @Override
    public float getDamping() {
        long settingsVa = va();
        float result = getDamping(settingsVa);

        return result;
    }

    /**
     * Return the spring's frequency. The settings are unaffected. (native
     * attribute: mFrequency)
     * <p>
     * Effective only when the mode is FrequencyAndDamping. If positive, the
     * constraint will have soft limits, and mFrequency specifies the
     * oscillation frequency in Hz. If negative, the constraint will have hard
     * limits.
     *
     * @return the frequency value
     */
    @Override
    public float getFrequency() {
        long settingsVa = va();
        float result = getFrequency(settingsVa);

        return result;
    }

    /**
     * Return how the spring is specified. The settings are unaffected. (native
     * attribute: mMode)
     *
     * @return an enum value (not null)
     */
    @Override
    public ESpringMode getMode() {
        long settingsVa = va();
        int ordinal = getMode(settingsVa);
        ESpringMode result = ESpringMode.values()[ordinal];

        return result;
    }

    /**
     * Return the spring's stiffness. The settings are unaffected. (native
     * attribute: mStiffness)
     * <p>
     * Effective only when the mode is StiffnessAndDamping. If positive, the
     * constraint will have soft limits, and mStiffness specifies the stiffness
     * {@code k} in the spring equation: {@code F = -k * x - c * v} for a linear
     * spring or {@code T = -k *
     * theta - c * w} for an angular spring.
     * <p>
     * If negative, the constraint will have hard limits.
     *
     * @return the stiffness value
     */
    @Override
    public float getStiffness() {
        long settingsVa = va();
        float result = getStiffness(settingsVa);

        return result;
    }

    /**
     * Test for valid frequency/stiffness. The settings are unaffected.
     *
     * @return {@code true} if valid (the constraint will have soft limits),
     * otherwise {@code false} (hard limits)
     */
    @Override
    public boolean hasStiffness() {
        long settingsVa = va();
        boolean result = hasStiffness(settingsVa);

        return result;
    }

    /**
     * Save the settings to the specified binary stream. The settings are
     * unaffected.
     *
     * @param stream the stream to write to (not null)
     */
    @Override
    public void saveBinaryState(StreamOut stream) {
        long settingsVa = va();
        long streamVa = stream.va();
        saveBinaryState(settingsVa, streamVa);
    }
    // *************************************************************************
    // native private methods

    native private static long createCopy(long originalVa);

    native private static long createDefault();

    native private static void free(long settingsVa);

    native private static float getDamping(long settingsVa);

    native private static float getFrequency(long settingsVa);

    native private static int getMode(long settingsVa);

    native private static float getStiffness(long settingsVa);

    native private static boolean hasStiffness(long settingsVa);

    native private static void restoreBinaryState(
            long settingsVa, long streamVa);

    native private static void saveBinaryState(long settingsVa, long streamVa);

    native private static void setDamping(long settingsVa, float damping);

    native private static void setFrequency(long settingsVa, float frequency);

    native private static void setMode(long settingsVa, int ordinal);

    native private static void setStiffness(long settingsVa, float stiffness);
}
