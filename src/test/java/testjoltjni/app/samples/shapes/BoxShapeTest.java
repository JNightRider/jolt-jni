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
package testjoltjni.app.samples.shapes;
import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.*;
import com.github.stephengold.joltjni.operator.Op;
import testjoltjni.app.samples.*;
/**
 * A line-for-line Java translation of the Jolt Physics box-shape test.
 * <p>
 * Compare with the original by Jorrit Rouwe at
 * https://github.com/jrouwe/JoltPhysics/blob/master/Samples/Tests/Shapes/BoxShapeTest.cpp
 */
public class BoxShapeTest extends Test{

public void Initialize()
{
	// Floor
	CreateFloor();

	// Different sized boxes
	mBodyInterface.createAndAddBody(new BodyCreationSettings(new BoxShape(new Vec3(20, 1, 1)),new RVec3(0, 10, 0), Quat.sIdentity(), EMotionType.Dynamic, Layers.MOVING), EActivation.Activate);

	mBodyInterface.createAndAddBody(new BodyCreationSettings(new BoxShape(new Vec3(2, 3, 4)),new RVec3(0, 10, 10), Quat.sRotation(Vec3.sAxisZ(), 0.25f * Jolt.JPH_PI), EMotionType.Dynamic, Layers.MOVING), EActivation.Activate);

	mBodyInterface.createAndAddBody(new BodyCreationSettings(new BoxShape(new Vec3(0.5f, 0.75f, 1.0f)),new RVec3(0, 10, 20), Op.multiply(Quat.sRotation(Vec3.sAxisX(), 0.25f * Jolt.JPH_PI) , Quat.sRotation(Vec3.sAxisZ(), 0.25f * Jolt.JPH_PI)), EMotionType.Dynamic, Layers.MOVING), EActivation.Activate);
}
}