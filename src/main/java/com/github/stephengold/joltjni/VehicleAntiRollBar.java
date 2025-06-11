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

import com.github.stephengold.joltjni.readonly.ConstVehicleAntiRollBar;

/**
 * A spring that connects two wheels and reduces a vehicle's rolling motion.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class VehicleAntiRollBar
        extends JoltPhysicsObject
        implements ConstVehicleAntiRollBar {
    // *************************************************************************
    // constructors

    /**
     * Instantiate a default bar.
     */
    public VehicleAntiRollBar() {
        long settingsVa = createDefault();
        setVirtualAddress(settingsVa, () -> free(settingsVa));
    }

    /**
     * Instantiate a copy of the specified settings.
     *
     * @param original the settings to copy (not {@code null}, unaffected)
     */
    public VehicleAntiRollBar(ConstVehicleAntiRollBar original) {
        long originalVa = original.targetVa();
        long copyVa = createCopy(originalVa);
        setVirtualAddress(copyVa, () -> free(copyVa));
    }

    /**
     * Instantiate with the specified container and native object.
     *
     * @param container the containing object, or {@code null} if none
     * @param barVa the virtual address of the native object to assign (not
     * zero)
     */
    VehicleAntiRollBar(JoltPhysicsObject container, long barVa) {
        super(container, barVa);
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
     * Alter the index of the left wheel. (native attribute: mLeftWheel)
     *
     * @param wheelIndex the index of the desired wheel (default=0)
     * @return the modified settings, for chaining
     */
    public VehicleAntiRollBar setLeftWheel(int wheelIndex) {
        long barVa = va();
        setLeftWheel(barVa, wheelIndex);

        return this;
    }

    /**
     * Alter the index of the right wheel. (native attribute: mRightWheel)
     *
     * @param wheelIndex the index of the desired wheel (default=1)
     * @return the modified settings, for chaining
     */
    public VehicleAntiRollBar setRightWheel(int wheelIndex) {
        long barVa = va();
        setRightWheel(barVa, wheelIndex);

        return this;
    }

    /**
     * Alter the stiffness of the bar. (native attribute: mStiffness)
     *
     * @param stiffness the desired spring constant (in Newtons per meter,
     * default=1000)
     * @return the modified settings, for chaining
     */
    public VehicleAntiRollBar setStiffness(float stiffness) {
        long barVa = va();
        setStiffness(barVa, stiffness);

        return this;
    }
    // *************************************************************************
    // ConstVehicleAntiRollBar methods

    /**
     * Return the index of the left wheel. The bar is unaffected. (native
     * attribute: mLeftWheel)
     *
     * @return the index of the wheel
     */
    @Override
    public int getLeftWheel() {
        long barVa = va();
        int result = getLeftWheel(barVa);

        return result;
    }

    /**
     * Return the index of the right wheel. The bar is unaffected. (native
     * attribute: mRightWheel)
     *
     * @return the index of the wheel
     */
    @Override
    public int getRightWheel() {
        long barVa = va();
        int result = getRightWheel(barVa);
        return result;
    }

    /**
     * Return the stiffness of the bar. The bar is unaffected. (native
     * attribute: mStiffness)
     *
     * @return the spring constant (in Newtons per meter)
     */
    @Override
    public float getStiffness() {
        long barVa = va();
        float result = getStiffness(barVa);

        return result;
    }

    /**
     * Save the bar to the specified binary stream. The bar is unaffected.
     *
     * @param stream the stream to write to (not null)
     */
    @Override
    public void saveBinaryState(StreamOut stream) {
        long barVa = va();
        long streamVa = stream.va();
        saveBinaryState(barVa, streamVa);
    }
    // *************************************************************************
    // native private methods

    native private static long createCopy(long originalVa);

    native private static long createDefault();

    native private static void free(long barVa);

    native private static int getLeftWheel(long barVa);

    native private static int getRightWheel(long barVa);

    native private static float getStiffness(long barVa);

    native private static void restoreBinaryState(long barVa, long streamVa);

    native private static void saveBinaryState(long barVa, long streamVa);

    native private static void setLeftWheel(long barVa, int wheelIndex);

    native private static void setRightWheel(long barVa, int wheelIndex);

    native private static void setStiffness(long barVa, float stiffness);
}
