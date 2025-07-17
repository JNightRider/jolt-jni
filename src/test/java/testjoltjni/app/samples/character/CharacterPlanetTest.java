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
package testjoltjni.app.samples.character;
import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.*;
import com.github.stephengold.joltjni.readonly.*;
import com.github.stephengold.joltjni.std.*;
import testjoltjni.app.samples.*;
import testjoltjni.app.testframework.CameraState;
import static com.github.stephengold.joltjni.Jolt.*;
import static com.github.stephengold.joltjni.operator.Op.*;

/**
 * A line-for-line Java translation of the Jolt-Physics character-planet test.
 * <p>
 * Compare with the original by Jorrit Rouwe at
 * https://github.com/jrouwe/JoltPhysics/blob/master/Samples/Tests/Character/CharacterPlanetTest.cpp
 */
public class CharacterPlanetTest extends Test{
float cPlanetRadius=20;
float cCharacterHeightStanding=1.35f;
float cCharacterRadiusStanding=0.3f;
float cJumpSpeed=4;
CharacterVirtualRef mCharacter=new CharacterVirtualRef();
Vec3 mDesiredVelocity=Vec3.sZero();
Vec3 mDesiredVelocityWS=Vec3.sZero();
boolean mJump;

public void Initialize()
{
	// Create planet
	mBodyInterface.createAndAddBody(new BodyCreationSettings(new SphereShape(cPlanetRadius), RVec3.sZero(), Quat.sIdentity(), EMotionType.Static, Layers.NON_MOVING), EActivation.DontActivate);

	// Create spheres
	BodyCreationSettings sphere=new BodyCreationSettings(new SphereShape(0.5f), RVec3.sZero(), Quat.sIdentity(), EMotionType.Dynamic, Layers.MOVING);
	sphere.setGravityFactor ( 0.0f); // We do our own gravity
	sphere.setOverrideMassProperties ( EOverrideMassProperties.CalculateInertia);
	sphere.getMassPropertiesOverride().setMass ( 10.0f);
	sphere.setAngularDamping ( 0.5f);
	DefaultRandomEngine random=new DefaultRandomEngine();
	for (int i = 0; i < 200; ++i)
	{
		UniformFloatDistribution theta=new UniformFloatDistribution(0, JPH_PI);
		UniformFloatDistribution phi=new UniformFloatDistribution(0, 2 * JPH_PI);
		sphere.setPosition (new RVec3(star(1.1f * cPlanetRadius , Vec3.sUnitSpherical(theta.nextFloat(random), phi.nextFloat(random)))));
		mBodyInterface.createAndAddBody(sphere, EActivation.Activate);
	}

	// Register to receive OnStep callbacks to apply gravity
	mPhysicsSystem.addStepListener(new CustomPhysicsStepListener() {
		    public void onStep(long context) {
			CharacterPlanetTest.this.OnStep(new PhysicsStepListenerContext(context));
		    }});

	// Create 'player' character
	CharacterVirtualSettings settings = new CharacterVirtualSettings();
	settings.setShape (new RotatedTranslatedShapeSettings(new Vec3(0, 0.5f * cCharacterHeightStanding + cCharacterRadiusStanding, 0), Quat.sIdentity(), new CapsuleShape(0.5f * cCharacterHeightStanding, cCharacterRadiusStanding)).create().get());
	settings.setSupportingVolume (new Plane(Vec3.sAxisY(), -cCharacterRadiusStanding)); // Accept contacts that touch the lower sphere of the capsule
	mCharacter = new CharacterVirtual(settings, new RVec3(0, cPlanetRadius, 0), Quat.sIdentity(), 0, mPhysicsSystem).toRef();
	mCharacter.getPtr().setListener(new CustomCharacterContactListener() {
		    public void onContactAdded(long characterVa, int bodyId2, int subShapeId2, double contactLocationX, double contactLocationY,
			   double contactLocationZ, float contactNormalX, float contactNormalY, float contactNormalZ, long settingsVa) {
			RVec3Arg inContactPosition=new RVec3(contactLocationX, contactLocationY, contactLocationZ);
			Vec3Arg inContactNormal=new Vec3(contactNormalX, contactNormalY, contactNormalZ);
			CharacterPlanetTest.this.OnContactAdded(new CharacterVirtual(characterVa, mPhysicsSystem), bodyId2, subShapeId2, inContactPosition, inContactNormal, new CharacterContactSettings(settingsVa));
		    }});
}
/*TODO

void CharacterPlanetTest::ProcessInput(const ProcessInputParams &inParams)
{
	// Determine controller input
	Vec3 control_input = Vec3::sZero();
	if (inParams.mKeyboard->IsKeyPressed(DIK_LEFT))		control_input.SetZ(-1);
	if (inParams.mKeyboard->IsKeyPressed(DIK_RIGHT))	control_input.SetZ(1);
	if (inParams.mKeyboard->IsKeyPressed(DIK_UP))		control_input.SetX(1);
	if (inParams.mKeyboard->IsKeyPressed(DIK_DOWN))		control_input.SetX(-1);
	if (control_input != Vec3::sZero())
		control_input = control_input.Normalized();

	// Smooth the player input
	mDesiredVelocity = 0.25f * control_input * cCharacterSpeed + 0.75f * mDesiredVelocity;

	// Convert player input to world space
	Vec3 up = mCharacter->GetUp();
	Vec3 right = inParams.mCameraState.mForward.Cross(up).NormalizedOr(Vec3::sAxisZ());
	Vec3 forward = up.Cross(right).NormalizedOr(Vec3::sAxisX());
	mDesiredVelocityWS = right * mDesiredVelocity.GetZ() + forward * mDesiredVelocity.GetX();

	// Check actions
	mJump = inParams.mKeyboard->IsKeyPressedAndTriggered(DIK_RCONTROL, mWasJump);
}
*/

public void PrePhysicsUpdate( PreUpdateParams inParams)
{
	// Calculate up vector based on position on planet surface
	Vec3 old_up = mCharacter.getUp();
	Vec3 up =new Vec3(mCharacter.getPosition()).normalized();
	mCharacter.setUp(up);

	// Rotate capsule so it points up relative to the planet surface
	mCharacter.setRotation(star(Quat.sFromTo(old_up, up) , mCharacter.getRotation()).normalized());

	// Draw character pre update (the sim is also drawn pre update)
if (implementsDebugRendering()) {
	mCharacter.getShape().draw(mDebugRenderer, mCharacter.getCenterOfMassTransform(), Vec3.sReplicate(1.0f), Color.sGreen, false, true);
}

	// Determine new character velocity
	Vec3 current_vertical_velocity = star(mCharacter.getLinearVelocity().dot(up) , up);
	Vec3 ground_velocity = mCharacter.getGroundVelocity();
	Vec3 new_velocity;
	if (mCharacter.getGroundState() == EGroundState.OnGround // If on ground
		&& minus(current_vertical_velocity , ground_velocity).dot(up) < 0.1f) // And not moving away from ground
	{
		// Assume velocity of ground when on ground
		new_velocity = ground_velocity;

		// Jump
		if (mJump)
			plusEquals(new_velocity , star(cJumpSpeed , up));
	}
	else
		new_velocity = current_vertical_velocity;

	// Apply gravity
	Vec3 gravity = star(minus(up) , mPhysicsSystem.getGravity().length());
	plusEquals(new_velocity , star(gravity , inParams.mDeltaTime));

	// Apply player input
	plusEquals(new_velocity , mDesiredVelocityWS);

	// Update character velocity
	mCharacter.setLinearVelocity(new_velocity);

	// Update the character position
	ExtendedUpdateSettings update_settings = new ExtendedUpdateSettings();
	mCharacter.extendedUpdate(inParams.mDeltaTime,
		gravity,
		update_settings,
		mPhysicsSystem.getDefaultBroadPhaseLayerFilter(Layers.MOVING),
		mPhysicsSystem.getDefaultLayerFilter(Layers.MOVING),
		new BodyFilter(){ },
		new ShapeFilter(){ },
		mTempAllocator);
}

public void GetInitialCamera(CameraState ioState)
{
	ioState.mPos =new RVec3(0, 0.5f, 0);
	ioState.mForward =new Vec3(1, -0.3f, 0).normalized();
}

RMat44 GetCameraPivot(float inCameraHeading, float inCameraPitch)
{
	// Pivot is center of character + distance behind based on the heading and pitch of the camera.
	Vec3 fwd = new Vec3(cos(inCameraPitch) * cos(inCameraHeading), sin(inCameraPitch), cos(inCameraPitch) * sin(inCameraHeading));
	RVec3 cam_pos = minus(mCharacter.getPosition() , star(5.0f , star(mCharacter.getRotation() , fwd)));
	return RMat44.sRotationTranslation(mCharacter.getRotation(), cam_pos);
}

public void SaveState(StateRecorder inStream)
{
	mCharacter.saveState(inStream);

	// Save character up, it's not stored by default but we use it in this case update the rotation of the character
	inStream.write(mCharacter.getUp());
}

public void RestoreState(StateRecorder inStream)
{
	mCharacter.restoreState(inStream);

	Vec3 up = mCharacter.getUp();
	inStream.readVec3(up);
	mCharacter.setUp(up);
}

void SaveInputState(StateRecorder inStream)
{
	inStream.write(mDesiredVelocity);
	inStream.write(mDesiredVelocityWS);
	inStream.write(mJump);
}

void RestoreInputState(StateRecorder inStream)
{
	inStream.readVec3(mDesiredVelocity);
	inStream.readVec3(mDesiredVelocityWS);
	mJump=inStream.readBoolean(mJump);
}

void OnStep( PhysicsStepListenerContext inContext)
{
	// Use the length of the global gravity vector
	float gravity = inContext.getPhysicsSystem().getGravity().length();

	// We don't need to lock the bodies since they're already locked in the OnStep callback.
	// Note that this means we're responsible for avoiding race conditions with other step listeners while accessing bodies.
	// We know that this is safe because in this demo there's only one step listener.
	 BodyLockInterface body_interface = inContext.getPhysicsSystem().getBodyLockInterfaceNoLock();

	// Loop over all active bodies
	BodyIdVector body_ids=new BodyIdVector();
	inContext.getPhysicsSystem().getActiveBodies(EBodyType.RigidBody, body_ids);
	for ( int id : body_ids.toList())
	{
		BodyLockWrite lock = new BodyLockWrite(body_interface, id);
		if (lock.succeeded())
		{
			// Apply gravity towards the center of the planet
			Body body = lock.getBody();
			RVec3 position = body.getPosition();
			float mass = 1.0f / body.getMotionProperties().getInverseMass();
			body.addForce(star(-gravity * mass , new Vec3(position).normalized()));
		}
		lock.releaseLock();
	}
}

void OnContactAdded( ConstCharacterVirtual inCharacter, int inBodyID2,  int inSubShapeID2, RVec3Arg inContactPosition, Vec3Arg inContactNormal, CharacterContactSettings ioSettings)
{
	// We don't want the spheres to push the player character
	ioSettings.setCanPushCharacter( false);
}
}
