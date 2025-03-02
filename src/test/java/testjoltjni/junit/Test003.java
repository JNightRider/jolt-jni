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
package testjoltjni.junit;

import com.github.stephengold.joltjni.AaBox;
import com.github.stephengold.joltjni.BodyCreationSettings;
import com.github.stephengold.joltjni.BoxShape;
import com.github.stephengold.joltjni.BoxShapeSettings;
import com.github.stephengold.joltjni.CharacterVirtual;
import com.github.stephengold.joltjni.CharacterVirtualSettings;
import com.github.stephengold.joltjni.CollisionGroup;
import com.github.stephengold.joltjni.ContactSettings;
import com.github.stephengold.joltjni.GroupFilterTable;
import com.github.stephengold.joltjni.JobSystem;
import com.github.stephengold.joltjni.JobSystemSingleThreaded;
import com.github.stephengold.joltjni.JobSystemThreadPool;
import com.github.stephengold.joltjni.Jolt;
import com.github.stephengold.joltjni.MassProperties;
import com.github.stephengold.joltjni.Mat44;
import com.github.stephengold.joltjni.MotionProperties;
import com.github.stephengold.joltjni.PhysicsSystem;
import com.github.stephengold.joltjni.Quat;
import com.github.stephengold.joltjni.RVec3;
import com.github.stephengold.joltjni.Shape;
import com.github.stephengold.joltjni.SkinWeight;
import com.github.stephengold.joltjni.SoftBodyMotionProperties;
import com.github.stephengold.joltjni.SphereShape;
import com.github.stephengold.joltjni.TempAllocator;
import com.github.stephengold.joltjni.TempAllocatorImpl;
import com.github.stephengold.joltjni.TempAllocatorImplWithMallocFallback;
import com.github.stephengold.joltjni.TempAllocatorMalloc;
import com.github.stephengold.joltjni.Vec3;
import com.github.stephengold.joltjni.enumerate.EAllowedDofs;
import com.github.stephengold.joltjni.enumerate.EMotionQuality;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.enumerate.EOverrideMassProperties;
import com.github.stephengold.joltjni.readonly.ConstBodyCreationSettings;
import com.github.stephengold.joltjni.readonly.ConstMassProperties;
import com.github.stephengold.joltjni.readonly.ConstShapeSettings;
import com.github.stephengold.joltjni.readonly.Vec3Arg;
import com.github.stephengold.joltjni.vhacd.FillMode;
import com.github.stephengold.joltjni.vhacd.Parameters;
import org.junit.Assert;
import org.junit.Test;
import testjoltjni.TestUtils;

/**
 * Automated JUnit4 tests for creation, destruction, accessors, and defaults of
 * various Jolt Physics objects.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class Test003 {
    // *************************************************************************
    // new methods exposed

    /**
     * Test object creation, destruction, accessors, and defaults.
     */
    @Test
    public void test003() {
        TestUtils.loadNativeLibrary();
        TestUtils.initializeNativeLibrary();

        doAaBox();
        doBodyCreationSettings();
        doCharacterVirtual();
        doCollisionGroup();
        doContactSettings();
        doJobSystemSingleThreaded();
        doJobSystemThreadPool();
        doMassProperties();
        doMotionProperties();
        doParameters();
        doSkinWeight();
        doSoftBodyMotionProperties();
        doTempAllocatorImpl();
        doTempAllocatorImplWithMallocFallback();
        doTempAllocatorMalloc();

        TestUtils.cleanup();
    }
    // *************************************************************************
    // Java private methods

    /**
     * Test the {@code AaBox} class.
     */
    private static void doAaBox() {
        {
            AaBox box = new AaBox();

            Assert.assertTrue(box.hasAssignedNativeObject());
            Assert.assertTrue(box.ownsNativeObject());
            Assert.assertNotEquals(0L, box.va());

            Assert.assertFalse(box.isValid());

            TestUtils.testClose(box);
        }
        {
            AaBox box = AaBox.sBiggest();

            Assert.assertTrue(box.hasAssignedNativeObject());
            Assert.assertTrue(box.ownsNativeObject());
            Assert.assertNotEquals(0L, box.va());

            TestUtils.assertEquals(0f, 0f, 0f, box.getCenter(), 0f);
            float halfMax = 0.5f * Float.MAX_VALUE;
            TestUtils.assertEquals(
                    halfMax, halfMax, halfMax, box.getExtent(), 0f);
            TestUtils.assertEquals(halfMax, halfMax, halfMax, box.getMax(), 0f);
            TestUtils.assertEquals(
                    -halfMax, -halfMax, -halfMax, box.getMin(), 0f);
            TestUtils.assertEquals(Float.MAX_VALUE, Float.MAX_VALUE,
                    Float.MAX_VALUE, box.getSize(), 0f);
            Assert.assertTrue(box.isValid());

            TestUtils.testClose(box);
        }
        {
            Vec3Arg max = new Vec3(4f, 5f, 6f);
            Vec3Arg min = new Vec3(1f, 2f, 3f);
            AaBox box = new AaBox(min, max);

            Assert.assertTrue(box.hasAssignedNativeObject());
            Assert.assertTrue(box.ownsNativeObject());
            Assert.assertNotEquals(0L, box.va());

            TestUtils.assertEquals(2.5f, 3.5f, 4.5f, box.getCenter(), 0f);
            TestUtils.assertEquals(4f, 5f, 6f, box.getMax(), 0f);
            TestUtils.assertEquals(1.5f, 1.5f, 1.5f, box.getExtent(), 0f);
            TestUtils.assertEquals(1f, 2f, 3f, box.getMin(), 0f);
            TestUtils.assertEquals(3f, 3f, 3f, box.getSize(), 0f);
            Assert.assertTrue(box.isValid());

            TestUtils.testClose(box);
        }

        System.gc();
    }

    /**
     * Test the {@code BodyCreationSettings} class.
     */
    private static void doBodyCreationSettings() {
        {
            BodyCreationSettings bcs = new BodyCreationSettings();

            Assert.assertNull(bcs.getShape());
            testBcsDefaults(bcs);
            testBcsSetters(bcs);

            TestUtils.testClose(bcs);
        }
        {
            ConstShapeSettings ss = new BoxShapeSettings(1f, 1f, 1f);
            int objectLayer = 0;
            BodyCreationSettings bcs = new BodyCreationSettings(ss,
                    new RVec3(), new Quat(), EMotionType.Dynamic, objectLayer);

            Assert.assertNotNull(bcs.getShape());
            Assert.assertTrue(bcs.getShape() instanceof BoxShape);
            testBcsDefaults(bcs);
            testBcsSetters(bcs);

            TestUtils.testClose(bcs, ss);
        }
        {
            Shape shape = new SphereShape(1f);
            int objectLayer = 0;
            BodyCreationSettings bcs = new BodyCreationSettings(shape,
                    new RVec3(), new Quat(), EMotionType.Dynamic, objectLayer);

            Assert.assertEquals(shape, bcs.getShape());
            testBcsDefaults(bcs);
            testBcsSetters(bcs);

            TestUtils.testClose(bcs, shape);
        }

        System.gc();
    }

    /**
     * Test the {@code CharacterVirtual} class.
     */
    private static void doCharacterVirtual() {
        CharacterVirtualSettings settings = new CharacterVirtualSettings();
        PhysicsSystem system = new PhysicsSystem();
        CharacterVirtual character = new CharacterVirtual(
                settings, new RVec3(), new Quat(), 0L, system);

        testCharacterVirtualDefaults(character);

        TestUtils.testClose(character, system, settings);
        System.gc();
    }

    /**
     * Test the {@code CollisionGroup} class.
     */
    private static void doCollisionGroup() {
        CollisionGroup group = new CollisionGroup();

        testCollisionGroupDefaults(group);
        testCollisionGroupSetters(group);

        TestUtils.testClose(group);
        System.gc();
    }

    /**
     * Test the {@code ContactSettings} class.
     */
    private static void doContactSettings() {
        ContactSettings settings = new ContactSettings();

        testContactSettingsDefaults(settings);
        testContactSettingsSetters(settings);

        TestUtils.testClose(settings);
        System.gc();
    }

    /**
     * Test the {@code JobSystemSingleThreaded} class.
     */
    private static void doJobSystemSingleThreaded() {
        JobSystem jobSystem = new JobSystemSingleThreaded(Jolt.cMaxPhysicsJobs);

        Assert.assertEquals(1, jobSystem.getMaxConcurrency());
        Assert.assertTrue(jobSystem.hasAssignedNativeObject());
        Assert.assertTrue(jobSystem.ownsNativeObject());
        Assert.assertNotEquals(0L, jobSystem.va());

        TestUtils.testClose(jobSystem);
        System.gc();
    }

    /**
     * Test the {@code JobSystemThreadPool} class.
     */
    private static void doJobSystemThreadPool() {
        JobSystemThreadPool jobSystem = new JobSystemThreadPool(
                Jolt.cMaxPhysicsJobs, Jolt.cMaxPhysicsBarriers);

        int numCpus = Runtime.getRuntime().availableProcessors();
        Assert.assertEquals(numCpus, jobSystem.getMaxConcurrency());

        Assert.assertTrue(jobSystem.hasAssignedNativeObject());
        Assert.assertTrue(jobSystem.ownsNativeObject());
        Assert.assertNotEquals(0L, jobSystem.va());

        jobSystem.setNumThreads(3);
        Assert.assertEquals(4, jobSystem.getMaxConcurrency());

        TestUtils.testClose(jobSystem);
        System.gc();
    }

    /**
     * Test the {@code MassProperties} class.
     */
    private static void doMassProperties() {
        MassProperties props = new MassProperties();

        testMpDefaults(props);
        testMpSetters(props);

        TestUtils.testClose(props);
        System.gc();
    }

    /**
     * Test the {@code MotionProperties} class.
     */
    private static void doMotionProperties() {
        MotionProperties props = new MotionProperties();

        testMotionDefaults(props);
        testMotionSetters(props);

        TestUtils.testClose(props);
        System.gc();
    }

    /**
     * Test the {@code Parameters} class.
     */
    private static void doParameters() {
        Parameters parameters = new Parameters();

        testParametersDefaults(parameters);
        testParametersSetters(parameters);

        TestUtils.testClose(parameters);
        System.gc();
    }

    /**
     * Test the {@code SkinWeight} class.
     */
    private static void doSkinWeight() {
        SkinWeight weight = new SkinWeight();
        testSkinWeightDefaults(weight);

        TestUtils.testClose(weight);
        System.gc();
    }

    /**
     * Test the {@code SoftBodyMotionProperties} class.
     */
    private static void doSoftBodyMotionProperties() {
        SoftBodyMotionProperties properties = new SoftBodyMotionProperties();

        testSoftBodyMotionPropertiesDefaults(properties);
        testSoftBodyMotionPropertiesSetters(properties);

        TestUtils.testClose(properties);
        System.gc();
    }

    /**
     * Test the {@code TempAllocatorImpl} class.
     */
    private static void doTempAllocatorImpl() {
        int numBytes = 1 << 8;
        TempAllocator tempAllocator = new TempAllocatorImpl(numBytes);

        Assert.assertTrue(tempAllocator.hasAssignedNativeObject());
        Assert.assertTrue(tempAllocator.ownsNativeObject());
        Assert.assertNotEquals(0L, tempAllocator.va());

        TestUtils.testClose(tempAllocator);
        System.gc();
    }

    /**
     * Test the {@code TempAllocatorImplWithMallocFallback} class.
     */
    private static void doTempAllocatorImplWithMallocFallback() {
        int numBytes = 1 << 8;
        TempAllocator tempAllocator
                = new TempAllocatorImplWithMallocFallback(numBytes);

        Assert.assertTrue(tempAllocator.hasAssignedNativeObject());
        Assert.assertTrue(tempAllocator.ownsNativeObject());
        Assert.assertNotEquals(0L, tempAllocator.va());

        TestUtils.testClose(tempAllocator);
        System.gc();
    }

    /**
     * Test the {@code TempAllocatorMalloc} class.
     */
    private static void doTempAllocatorMalloc() {
        TempAllocator tempAllocator = new TempAllocatorMalloc();

        Assert.assertTrue(tempAllocator.hasAssignedNativeObject());
        Assert.assertTrue(tempAllocator.ownsNativeObject());
        Assert.assertNotEquals(0L, tempAllocator.va());

        TestUtils.testClose(tempAllocator);
        System.gc();
    }

    /**
     * Test the getters and defaults of the specified
     * {@code BodyCreationSettings}.
     *
     * @param bcs the settings to test (not null, unaffected)
     */
    private static void testBcsDefaults(ConstBodyCreationSettings bcs) {
        Assert.assertTrue(bcs.hasAssignedNativeObject());
        Assert.assertTrue(bcs.ownsNativeObject());

        Assert.assertTrue(bcs.getAllowSleeping());
        Assert.assertEquals(0.05f, bcs.getAngularDamping(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, bcs.getAngularVelocity(), 0f);
        Assert.assertEquals(0.2f, bcs.getFriction(), 0f);
        Assert.assertEquals(1f, bcs.getGravityFactor(), 0f);
        Assert.assertFalse(bcs.getIsSensor());
        Assert.assertEquals(0.05f, bcs.getLinearDamping(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, bcs.getLinearVelocity(), 0f);
        Assert.assertEquals(15 * Math.PI, bcs.getMaxAngularVelocity(), 1e-6f);
        Assert.assertEquals(500f, bcs.getMaxLinearVelocity(), 0f);
        Assert.assertEquals(EMotionQuality.Discrete, bcs.getMotionQuality());
        Assert.assertEquals(EMotionType.Dynamic, bcs.getMotionType());
        Assert.assertEquals(0, bcs.getObjectLayer());
        Assert.assertEquals(EOverrideMassProperties.CalculateMassAndInertia,
                bcs.getOverrideMassProperties());
        TestUtils.assertEquals(0f, 0f, 0f, bcs.getPosition(), 0f);
        Assert.assertEquals(0f, bcs.getRestitution(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, 1f, bcs.getRotation(), 0f);
        Assert.assertTrue(bcs.hasMassProperties());
    }

    /**
     * Test the setters of the specified {@code BodyCreationSettings}.
     *
     * @param bcs the settings to test (not null, modified)
     */
    private static void testBcsSetters(BodyCreationSettings bcs) {
        bcs.setAllowSleeping(false);
        bcs.setAngularDamping(0.01f);
        bcs.setAngularVelocity(new Vec3(0.02f, 0.03f, 0.04f));
        bcs.setFriction(0.05f);
        bcs.setGravityFactor(0.06f);
        bcs.setIsSensor(true);
        bcs.setLinearDamping(0.07f);
        bcs.setLinearVelocity(new Vec3(0.08f, 0.09f, 0.1f));
        bcs.setMaxAngularVelocity(0.101f);
        bcs.setMaxLinearVelocity(0.102f);
        bcs.setMotionQuality(EMotionQuality.LinearCast);
        bcs.setMotionType(EMotionType.Kinematic);
        bcs.setObjectLayer(11);
        bcs.setOverrideMassProperties(
                EOverrideMassProperties.MassAndInertiaProvided);
        bcs.setPosition(new RVec3(0.12, 0.13, 0.14));
        bcs.setRestitution(0.15f);
        bcs.setRotation(new Quat(0.6f, 0f, 0f, 0.8f));

        Assert.assertFalse(bcs.getAllowSleeping());
        Assert.assertEquals(0.01f, bcs.getAngularDamping(), 0f);
        TestUtils.assertEquals(
                0.02f, 0.03f, 0.04f, bcs.getAngularVelocity(), 0f);
        Assert.assertEquals(0.05f, bcs.getFriction(), 0f);
        Assert.assertEquals(0.06f, bcs.getGravityFactor(), 0f);
        Assert.assertTrue(bcs.getIsSensor());
        Assert.assertEquals(0.07f, bcs.getLinearDamping(), 0f);
        TestUtils.assertEquals(0.08f, 0.09f, 0.1f, bcs.getLinearVelocity(), 0f);
        Assert.assertEquals(0.101f, bcs.getMaxAngularVelocity(), 0f);
        Assert.assertEquals(0.102f, bcs.getMaxLinearVelocity(), 0f);
        Assert.assertEquals(EMotionQuality.LinearCast, bcs.getMotionQuality());
        Assert.assertEquals(EMotionType.Kinematic, bcs.getMotionType());
        Assert.assertEquals(11, bcs.getObjectLayer());
        Assert.assertEquals(EOverrideMassProperties.MassAndInertiaProvided,
                bcs.getOverrideMassProperties());
        TestUtils.assertEquals(0.12f, 0.13f, 0.14f, bcs.getPosition(), 0f);
        Assert.assertEquals(0.15f, bcs.getRestitution(), 0f);
        TestUtils.assertEquals(0.6f, 0f, 0f, 0.8f, bcs.getRotation(), 0f);
    }

    /**
     * Test the getters and defaults of the specified {@code CharacterVirtual}.
     *
     * @param character the character to test (not null, unaffected)
     */
    private static void testCharacterVirtualDefaults(
            CharacterVirtual character) {
        Assert.assertFalse(character.getEnhancedInternalEdgeRemoval());
        Assert.assertEquals(1f, character.getHitReductionCosMaxAngle(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, character.getLinearVelocity(), 0f);
        Assert.assertEquals(70f, character.getMass(), 0f);
        Assert.assertEquals(256, character.getMaxNumHits());
        Assert.assertEquals(100f, character.getMaxStrength(), 0f);
        Assert.assertEquals(1f, character.getPenetrationRecoverySpeed(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, character.getPosition(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, 1f, character.getRotation(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, character.getShapeOffset(), 0f);
    }

    /**
     * Test the getters and defaults of the specified {@code CollisionGroup}.
     *
     * @param group the group to test (not null, unaffected)
     */
    private static void testCollisionGroupDefaults(CollisionGroup group) {
        Assert.assertNull(group.getGroupFilter());
        Assert.assertEquals(CollisionGroup.cInvalidGroup, group.getGroupId());
        Assert.assertEquals(
                CollisionGroup.cInvalidGroup, group.getSubGroupId());
    }

    /**
     * Test the setters of the specified {@code CollisionGroup}.
     *
     * @param group the group to test (not null)
     */
    private static void testCollisionGroupSetters(CollisionGroup group) {
        GroupFilterTable filter = new GroupFilterTable();
        group.setGroupFilter(filter);
        group.setGroupId(101);
        group.setSubGroupId(102);

        Assert.assertEquals(filter.va(), group.getGroupFilter().va());
        Assert.assertEquals(101, group.getGroupId());
        Assert.assertEquals(102, group.getSubGroupId());
    }

    /**
     * Test the getters and defaults of the specified {@code ContactSettings}.
     *
     * @param settings the settings to test (not null, unaffected)
     */
    private static void testContactSettingsDefaults(ContactSettings settings) {
        Assert.assertEquals(0f, settings.getCombinedFriction(), 0f);
        Assert.assertEquals(0f, settings.getCombinedRestitution(), 0f);
        Assert.assertEquals(1f, settings.getInvInertiaScale1(), 0f);
        Assert.assertEquals(1f, settings.getInvInertiaScale2(), 0f);
        Assert.assertEquals(1f, settings.getInvMassScale1(), 0f);
        Assert.assertEquals(1f, settings.getInvMassScale2(), 0f);
        Assert.assertFalse(settings.getIsSensor());
        TestUtils.assertEquals(0f, 0f, 0f,
                settings.getRelativeAngularSurfaceVelocity(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f,
                settings.getRelativeLinearSurfaceVelocity(), 0f);
    }

    /**
     * Test the setters of the specified {@code ContactSettings}.
     *
     * @param settings the settings to test (not null)
     */
    private static void testContactSettingsSetters(ContactSettings settings) {
        settings.setCombinedFriction(11f);
        settings.setCombinedRestitution(12f);
        settings.setInvInertiaScale1(13f);
        settings.setInvInertiaScale2(14f);
        settings.setInvMassScale1(15f);
        settings.setInvMassScale2(16f);
        settings.setIsSensor(true);
        settings.setRelativeAngularSurfaceVelocity(new Vec3(17f, 18f, 19f));
        settings.setRelativeLinearSurfaceVelocity(new Vec3(20f, 21f, 22f));

        Assert.assertEquals(11f, settings.getCombinedFriction(), 0f);
        Assert.assertEquals(12f, settings.getCombinedRestitution(), 0f);
        Assert.assertEquals(13f, settings.getInvInertiaScale1(), 0f);
        Assert.assertEquals(14f, settings.getInvInertiaScale2(), 0f);
        Assert.assertEquals(15f, settings.getInvMassScale1(), 0f);
        Assert.assertEquals(16f, settings.getInvMassScale2(), 0f);
        Assert.assertTrue(settings.getIsSensor());
        TestUtils.assertEquals(17f, 18f, 19f,
                settings.getRelativeAngularSurfaceVelocity(), 0f);
        TestUtils.assertEquals(20f, 21f, 22f,
                settings.getRelativeLinearSurfaceVelocity(), 0f);
    }

    /**
     * Test the getters and defaults of the specified {@code MotionProperties}.
     *
     * @param props the properties to test (not null, unaffected)
     */
    private static void testMotionDefaults(MotionProperties props) {
        Assert.assertTrue(props.hasAssignedNativeObject());
        Assert.assertTrue(props.ownsNativeObject());

        TestUtils.assertEquals(0f, 0f, 0f, props.getAccumulatedForce(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, props.getAccumulatedTorque(), 0f);
        Assert.assertEquals(EAllowedDofs.All, props.getAllowedDofs());
        Assert.assertFalse(props.getAllowSleeping());
        Assert.assertEquals(0f, props.getAngularDamping(), 0f);
        TestUtils.assertEquals(-1, -1, -1, 0, props.getAngularDofsMask());
        TestUtils.assertEquals(0f, 0f, 0f, props.getAngularVelocity(), 0f);
        Assert.assertEquals(0f, props.getGravityFactor(), 0f);
        Assert.assertEquals(0f, props.getInverseMassUnchecked(), 0f);
        Assert.assertEquals(0f, props.getLinearDamping(), 0f);
        TestUtils.assertEquals(0f, 0f, 0f, props.getLinearVelocity(), 0f);
        Assert.assertEquals(0f, props.getMaxAngularVelocity(), 0f);
        Assert.assertEquals(0f, props.getMaxLinearVelocity(), 0f);
        Assert.assertEquals(EMotionQuality.Discrete, props.getMotionQuality());
        Assert.assertEquals(0, props.getNumPositionStepsOverride());
        Assert.assertEquals(0, props.getNumVelocityStepsOverride());
    }

    /**
     * Test the setters of the specified {@code MotionProperties}.
     *
     * @param props the properties to test (not null, modified)
     */
    private static void testMotionSetters(MotionProperties props) {
        props.setAngularDamping(0.01f);
        Assert.assertEquals(0.01f, props.getAngularDamping(), 0f);

        props.setMaxAngularVelocity(10f);
        Assert.assertEquals(10f, props.getMaxAngularVelocity(), 0f);

        props.setAngularVelocity(new Vec3(0.02f, 0.03f, 0.04f));
        TestUtils.assertEquals(
                0.02f, 0.03f, 0.04f, props.getAngularVelocity(), 0f);

        props.setGravityFactor(0.05f);
        Assert.assertEquals(0.05f, props.getGravityFactor(), 0f);

        props.setLinearDamping(0.06f);
        Assert.assertEquals(0.06f, props.getLinearDamping(), 0f);

        props.setMaxLinearVelocity(11f);
        Assert.assertEquals(11f, props.getMaxLinearVelocity(), 0f);

        props.setLinearVelocity(new Vec3(0.07f, 0.08f, 0.09f));
        TestUtils.assertEquals(
                0.07f, 0.08f, 0.09f, props.getLinearVelocity(), 0f);

        props.setNumPositionStepsOverride(12);
        Assert.assertEquals(12, props.getNumPositionStepsOverride());

        props.setNumVelocityStepsOverride(13);
        Assert.assertEquals(13, props.getNumVelocityStepsOverride());
    }

    /**
     * Test the getters and defaults of the specified {@code Parameters}.
     *
     * @param params the parameters to test (not null, unaffected)
     */
    private static void testParametersDefaults(Parameters params) {
        Assert.assertTrue(params.hasAssignedNativeObject());
        Assert.assertTrue(params.ownsNativeObject());

        Assert.assertTrue(params.getAsyncAcd());
        Assert.assertFalse(params.isDebugOutputEnabled());
        Assert.assertEquals(FillMode.FloodFill, params.getFillMode());
        Assert.assertFalse(params.getFindBestPlane());
        Assert.assertEquals(64, params.getMaxConvexHulls());
        Assert.assertEquals(64, params.getMaxNumVerticesPerCh());
        Assert.assertEquals(10, params.getMaxRecursionDepth());
        Assert.assertEquals(2, params.getMinEdgeLength());
        Assert.assertEquals(
                1., params.getMinimumVolumePercentErrorAllowed(), 0.);
        Assert.assertEquals(400_000, params.getResolution());
        Assert.assertTrue(params.getShrinkWrap());
    }

    /**
     * Test the setters of the specified {@code Parameters}.
     *
     * @param params the parameters to test (not null, modified)
     */
    private static void testParametersSetters(Parameters params) {
        params.setAsyncAcd(false);
        params.setDebugOutputEnabled(true);
        params.setFillMode(FillMode.RaycastFill);
        params.setFindBestPlane(true);
        params.setMaxConvexHulls(123);
        params.setMaxNumVerticesPerCh(67);
        params.setMaxRecursionDepth(8);
        params.setMinEdgeLength(9);
        params.setMinimumVolumePercentErrorAllowed(12.);
        params.setResolution(345_678);
        params.setShrinkWrap(false);

        Assert.assertFalse(params.getAsyncAcd());
        Assert.assertTrue(params.isDebugOutputEnabled());
        Assert.assertEquals(FillMode.RaycastFill, params.getFillMode());
        Assert.assertTrue(params.getFindBestPlane());
        Assert.assertEquals(123, params.getMaxConvexHulls());
        Assert.assertEquals(67, params.getMaxNumVerticesPerCh());
        Assert.assertEquals(8, params.getMaxRecursionDepth());
        Assert.assertEquals(9, params.getMinEdgeLength());
        Assert.assertEquals(
                12., params.getMinimumVolumePercentErrorAllowed(), 0.);
        Assert.assertEquals(345_678, params.getResolution());
        Assert.assertFalse(params.getShrinkWrap());
    }

    /**
     * Test the getters and defaults of the specified {@code SkinWeight}.
     *
     * @param weight the weight to test (not null, unaffected)
     */
    private static void testSkinWeightDefaults(SkinWeight weight) {
        Assert.assertTrue(weight.hasAssignedNativeObject());
        Assert.assertTrue(weight.ownsNativeObject());

        Assert.assertEquals(0, weight.getInvBindIndex());
        Assert.assertEquals(0., weight.getWeight(), 0.);
    }

    /**
     * Test the getters and defaults of the specified
     * {@code SoftBodyMotionProperties}.
     *
     * @param properties the properties to test (not null, unaffected)
     */
    private static void testSoftBodyMotionPropertiesDefaults(
            SoftBodyMotionProperties properties) {
        Assert.assertTrue(properties.hasAssignedNativeObject());
        Assert.assertTrue(properties.ownsNativeObject());

        Assert.assertTrue(properties.getEnableSkinConstraints());
        Assert.assertEquals(0, properties.getNumIterations());
        Assert.assertEquals(
                1f, properties.getSkinnedMaxDistanceMultiplier(), 0f);
    }

    /**
     * Test the setters of the specified {@code SoftBodyMotionProperties}.
     *
     * @param properties the properties to test (not null, modified)
     */
    private static void testSoftBodyMotionPropertiesSetters(
            SoftBodyMotionProperties properties) {
        properties.setEnableSkinConstraints(false);
        properties.setNumIterations(2);
        properties.setSkinnedMaxDistanceMultiplier(9f);

        Assert.assertFalse(properties.getEnableSkinConstraints());
        Assert.assertEquals(2, properties.getNumIterations());
        Assert.assertEquals(
                9f, properties.getSkinnedMaxDistanceMultiplier(), 0f);
    }

    /**
     * Test the getters and defaults of the specified {@code MassProperties}.
     *
     * @param props the properties to test (not null, unaffected)
     */
    private static void testMpDefaults(ConstMassProperties props) {
        Assert.assertTrue(props.hasAssignedNativeObject());
        Assert.assertTrue(props.ownsNativeObject());

        Assert.assertEquals(0f, props.getMass(), 0f);
        Assert.assertTrue(props.getInertia().isEqual(Mat44.sZero()));
    }

    /**
     * Test the setters of the specified {@code MassProperties}.
     *
     * @param props the properties to test (not null, modified)
     */
    private static void testMpSetters(MassProperties props) {
        props.setMass(2f);
        Assert.assertEquals(2f, props.getMass(), 0f);

        props.setInertia(Mat44.sIdentity());
        Assert.assertTrue(props.getInertia().isEqual(Mat44.sIdentity()));
    }
}
