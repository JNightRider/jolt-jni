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
package testjoltjni.app.helloworld;
import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.*;
import testjoltjni.TestUtils;
import static com.github.stephengold.joltjni.Jolt.*;
import static com.github.stephengold.joltjni.std.Std.*;

/**
 * A line-for-line Java translation of the Jolt-Physics "hello world" sample
 * application.
 * <p>
 * Compare with the original by Jorrit Rouwe at
 * https://github.com/jrouwe/JoltPhysics/blob/master/HelloWorld/HelloWorld.cpp
 */
public class HelloWorld {










// Layer that objects can be in, determines which other objects it can collide with
// Typically you at least want to have 1 layer for moving bodies and 1 layer for static bodies, but you can have more
// layers if you want. E.g. you could have a layer for high detail collision (which is not used by the physics simulation
// but only if you do collision testing).
class Layers
{
	static final int NON_MOVING = 0;
	static final int MOVING = 1;
	static final int NUM_LAYERS = 2;
};

/// Class that determines if two object layers can collide
static class ObjectLayerPairFilterImpl extends ObjectLayerPairFilterTable
{
	ObjectLayerPairFilterImpl() {
		super(Layers.NUM_LAYERS);
		enableCollision(Layers.MOVING, Layers.MOVING);
		enableCollision(Layers.MOVING, Layers.NON_MOVING);
	}
};

// Each broadphase layer results in a separate bounding volume tree in the broad phase. You at least want to have
// a layer for non-moving and moving objects to avoid having to update a tree full of static objects every frame.
// You can have a 1-on-1 mapping between object layers and broadphase layers (like in this case) but if you have
// many object layers you'll be creating many broad phase trees, which is not efficient. If you want to fine tune
// your broadphase layers define JPH_TRACK_BROADPHASE_STATS and look at the stats reported on the TTY.
class BroadPhaseLayers
{
	static final int NON_MOVING=0;
	static final int MOVING=1;
	static final int NUM_LAYERS=2;
};

// BroadPhaseLayerInterface implementation
// This defines a mapping between object and broadphase layers.
static class BPLayerInterfaceImpl extends BroadPhaseLayerInterfaceTable
{
									BPLayerInterfaceImpl()
	{
		// Create a mapping table from object to broad phase layer
            super(Layers.NUM_LAYERS, BroadPhaseLayers.NUM_LAYERS);
		mapObjectToBroadPhaseLayer(Layers.NON_MOVING, BroadPhaseLayers.NON_MOVING);
		mapObjectToBroadPhaseLayer(Layers.MOVING, BroadPhaseLayers.MOVING);
	}

};

/// Class that determines if an object layer can collide with a broadphase layer
static class ObjectVsBroadPhaseLayerFilterImpl extends ObjVsBpFilter
{
	ObjectVsBroadPhaseLayerFilterImpl() {
		super(Layers.NUM_LAYERS, BroadPhaseLayers.NUM_LAYERS);
		disablePair(Layers.NON_MOVING, BroadPhaseLayers.NON_MOVING);
	}
};

// An example contact listener
static class MyContactListener extends CustomContactListener
{
public
	// See: ContactListener
	int onContactValidate(long body1Va, long body2Va, double offsetX, double offsetY, double offsetZ, long inCollisionResult)
	{
		cout .print( "Contact validate callback" + endl);

		// Allows you to ignore a contact before it is created (using layers to not make objects collide is cheaper!)
		return ValidateResult.AcceptAllContactsForThisBodyPair.ordinal();
	}

	public void			onContactAdded(long  inBody1, long  inBody2, long  inManifold, long ioSettings)
	{
		cout .print( "A contact was added" + endl);
	}

	public void			onContactPersisted(long  inBody1, long  inBody2, long  inManifold, long ioSettings)
	{
		cout .print( "A contact was persisted" + endl);
	}

	public void			onContactRemoved(long  inSubShapePair)
	{
		cout .print( "A contact was removed" + endl);
	}
};

// An example activation listener
static class MyBodyActivationListener extends  CustomBodyActivationListener
{
public
	 void		onBodyActivated(int bodyId, long inBodyUserData)
	{
		cout .print( "A body got activated" + endl);
	}

	public void		onBodyDeactivated(int bodyId, long inBodyUserData)
	{
		cout .print( "A body went to sleep" + endl);
	}
};

// Program entry point
public static void main(  String[] argv)
{
	TestUtils.loadNativeLibrary();
	// Register allocation hook. In this example we'll just let Jolt use malloc / free but you can override these if you want (see Memory.h).
	// This needs to be done before any other Jolt function is called.
	registerDefaultAllocator();

	// Install trace and assert callbacks
	installDefaultTraceCallback(  );
	installDefaultAssertCallback(  );

	// Create a factory, this class is responsible for creating instances of classes based on their name or hash and is mainly used for deserialization of saved data.
	// It is not directly used in this example but still required.
	newFactory();

	// Register all physics types with the factory and install their collision handlers with the CollisionDispatch class.
	// If you have your own custom shape types you probably need to register their handlers with the CollisionDispatch before calling this function.
	// If you implement your own default material (PhysicsMaterial::sDefault) make sure to initialize it before this function or else this function will create one for you.
	registerTypes();

	// We need a temp allocator for temporary allocations during the physics update. We're
	// pre-allocating 10 MB to avoid having to do allocations during the physics update.
	// B.t.w. 10 MB is way too much for this example but it is a typical value you can use.
	// If you don't want to pre-allocate you can also use TempAllocatorMalloc to fall back to
	// malloc / free.
	TempAllocatorImpl temp_allocator=new TempAllocatorImpl(10 * 1024 * 1024);

	// We need a job system that will execute physics jobs on multiple threads. Typically
	// you would implement the JobSystem interface yourself and let Jolt Physics run on top
	// of your own job scheduler. JobSystemThreadPool is an example implementation.
	JobSystemThreadPool job_system=new JobSystemThreadPool(cMaxPhysicsJobs, cMaxPhysicsBarriers, TestUtils.numThreads());

	// This is the max amount of rigid bodies that you can add to the physics system. If you try to add more you'll get an error.
	// Note: This value is low because this is a simple test. For a real project use something in the order of 65536.
	final int cMaxBodies = 1024;

	// This determines how many mutexes to allocate to protect rigid bodies from concurrent access. Set it to 0 for the default settings.
	final int cNumBodyMutexes = 0;

	// This is the max amount of body pairs that can be queued at any time (the broad phase will detect overlapping
	// body pairs based on their bounding boxes and will insert them into a queue for the narrowphase). If you make this buffer
	// too small the queue will fill up and the broad phase jobs will start to do narrow phase work. This is slightly less efficient.
	// Note: This value is low because this is a simple test. For a real project use something in the order of 65536.
	final int cMaxBodyPairs = 1024;

	// This is the maximum size of the contact constraint buffer. If more contacts (collisions between bodies) are detected than this
	// number then these contacts will be ignored and bodies will start interpenetrating / fall through the world.
	// Note: This value is low because this is a simple test. For a real project use something in the order of 10240.
	final int cMaxContactConstraints = 1024;

	// Create mapping table from object layer to broadphase layer
	// Note: As this is an interface, PhysicsSystem will take a reference to this so this instance needs to stay alive!
	// Also have a look at BroadPhaseLayerInterfaceTable or BroadPhaseLayerInterfaceMask for a simpler interface.
	BPLayerInterfaceImpl broad_phase_layer_interface=new BPLayerInterfaceImpl();

	// Create class that filters object vs broadphase layers
	// Note: As this is an interface, PhysicsSystem will take a reference to this so this instance needs to stay alive!
	// Also have a look at ObjectVsBroadPhaseLayerFilterTable or ObjectVsBroadPhaseLayerFilterMask for a simpler interface.
	ObjectVsBroadPhaseLayerFilterImpl object_vs_broadphase_layer_filter=new ObjectVsBroadPhaseLayerFilterImpl();

	// Create class that filters object vs object layers
	// Note: As this is an interface, PhysicsSystem will take a reference to this so this instance needs to stay alive!
	// Also have a look at ObjectLayerPairFilterTable or ObjectLayerPairFilterMask for a simpler interface.
	ObjectLayerPairFilterImpl object_vs_object_layer_filter=new ObjectLayerPairFilterImpl();

	// Now we can create the actual physics system.
	PhysicsSystem physics_system=new PhysicsSystem();
	physics_system.init(cMaxBodies, cNumBodyMutexes, cMaxBodyPairs, cMaxContactConstraints, broad_phase_layer_interface, object_vs_broadphase_layer_filter, object_vs_object_layer_filter);

	// A body activation listener gets notified when bodies activate and go to sleep
	// Note that this is called from a job so whatever you do here needs to be thread safe.
	// Registering one is entirely optional.
	MyBodyActivationListener body_activation_listener=new MyBodyActivationListener();
	physics_system.setBodyActivationListener(body_activation_listener);

	// A contact listener gets notified when bodies (are about to) collide, and when they separate again.
	// Note that this is called from a job so whatever you do here needs to be thread safe.
	// Registering one is entirely optional.
	MyContactListener contact_listener=new MyContactListener();
	physics_system.setContactListener(contact_listener);

	// The main way to interact with the bodies in the physics system is through the body interface. There is a locking and a non-locking
	// variant of this. We're going to use the locking version (even though we're not planning to access bodies from multiple threads)
	BodyInterface body_interface = physics_system.getBodyInterface();

	// Next we can create a rigid body to serve as the floor, we make a large box
	// Create the settings for the collision volume (the shape).
	// Note that for simple shapes (like boxes) you can also directly construct a BoxShape.
	BoxShapeSettings floor_shape_settings=new BoxShapeSettings(new Vec3(100.0f, 1.0f, 100.0f));
	floor_shape_settings.setEmbedded(); // A ref counted object on the stack (base class RefTarget) should be marked as such to prevent it from being freed when its reference count goes to 0.

	// Create the shape
	ShapeResult floor_shape_result = floor_shape_settings.create();
	ShapeRefC floor_shape = floor_shape_result.get(); // We don't expect an error here, but you can check floor_shape_result for HasError() / GetError()

	// Create the settings for the body itself. Note that here you can also set other properties like the restitution / friction.
	BodyCreationSettings floor_settings=new BodyCreationSettings(floor_shape,new RVec3(0.0, -1.0, 0.0), new Quat(), EMotionType.Static, Layers.NON_MOVING);

	// Create the actual rigid body
	Body floor = body_interface.createBody(floor_settings); // Note that if we run out of bodies this can return nullptr

	// Add it to the world
	body_interface.addBody(floor.getId(), EActivation.DontActivate);

	// Now create a dynamic body to bounce on the floor
	// Note that this uses the shorthand version of creating and adding a body to the world
	BodyCreationSettings sphere_settings=new BodyCreationSettings(new SphereShape(0.5f),new RVec3(0f, 2f, 0f), new Quat(), EMotionType.Dynamic, Layers.MOVING);
	int sphere_id = body_interface.createAndAddBody(sphere_settings, EActivation.Activate);

	// Now you can interact with the dynamic body, in this case we're going to give it a velocity.
	// (note that if we had used CreateBody then we could have set the velocity straight on the body before adding it to the physics system)
	body_interface.setLinearVelocity(sphere_id,new Vec3(0.0f, -5.0f, 0.0f));

	// We simulate the physics world in discrete time steps. 60 Hz is a good rate to update the physics system.
	final float cDeltaTime = 1.0f / 60.0f;

	// Optional step: Before starting the physics simulation you can optimize the broad phase. This improves collision detection performance (it's pointless here because we only have 2 bodies).
	// You should definitely not call this every frame or when e.g. streaming in a new level section as it is an expensive operation.
	// Instead insert all new objects in batches instead of 1 at a time to keep the broad phase efficient.
	physics_system.optimizeBroadPhase();

	// Now we're ready to simulate the body, keep simulating until it goes to sleep
	int step = 0;
	while (body_interface.isActive(sphere_id))
	{
		// Next step
		++step;

		// Output current position and velocity of the sphere
		RVec3 position = body_interface.getCenterOfMassPosition(sphere_id);
		Vec3 velocity = body_interface.getLinearVelocity(sphere_id);
		cout .print( "Step " + step + ": Position = " + position + ", Velocity = " + velocity + endl) ;

		// If you take larger steps than 1 / 60th of a second you need to do multiple collision steps in order to keep the simulation stable. Do 1 collision step per 1 / 60th of a second (round up).
		final int cCollisionSteps = 1;

		// Step the world
		physics_system.update(cDeltaTime, cCollisionSteps, temp_allocator, job_system);
	}

	// Remove the sphere from the physics system. Note that the sphere itself keeps all of its state and can be re-added at any time.
	body_interface.removeBody(sphere_id);

	// Destroy the sphere. After this the sphere ID is no longer valid.
	body_interface.destroyBody(sphere_id);

	// Remove and destroy the floor
	body_interface.removeBody(floor.getId());
	body_interface.destroyBody(floor.getId());

	// Unregisters all types with the factory and cleans up the default material
	unregisterTypes();

	// Destroy the factory
	destroyFactory();

}
}
