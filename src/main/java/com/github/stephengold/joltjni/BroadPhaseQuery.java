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

import com.github.stephengold.joltjni.readonly.ConstAaBox;
import com.github.stephengold.joltjni.readonly.ConstOrientedBox;
import com.github.stephengold.joltjni.readonly.Vec3Arg;

/**
 * Interface for crude collision detection against the bounding boxes in a
 * {@code PhysicsSystem}.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class BroadPhaseQuery extends NonCopyable {
    // *************************************************************************
    // constructors

    /**
     * Instantiate with no native object assigned.
     */
    BroadPhaseQuery() {
    }

    /**
     * Instantiate with the specified native object assigned but not owned.
     *
     * @param system the containing object, or {@code null} if none
     * @param queryVa the virtual address of the native object to assign (not
     * zero)
     */
    BroadPhaseQuery(PhysicsSystem system, long queryVa) {
        super(system, queryVa);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Cast a box along a line segment and collect the resulting hits.
     *
     * @param boxCast the test box and route (not null, unaffected)
     * @param collector the hit collector to use (not null)
     */
    public void castAaBox(
            AaBoxCast boxCast, CastShapeBodyCollector collector) {
        castAaBox(boxCast, collector, new BroadPhaseLayerFilter());
    }

    /**
     * Cast a box along a line segment and collect the resulting hits.
     *
     * @param boxCast the test box and route (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     */
    public void castAaBox(
            AaBoxCast boxCast, CastShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter) {
        castAaBox(boxCast, collector, bplFilter, new ObjectLayerFilter());
    }

    /**
     * Cast a box along a line segment and collect the resulting hits.
     *
     * @param boxCast the test box and route (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     * @param olFilter the object-layer filter to apply (not null, unaffected)
     */
    public void castAaBox(
            AaBoxCast boxCast, CastShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter, ObjectLayerFilter olFilter) {
        long queryVa = va();
        long boxCastVa = boxCast.va();
        long collectorVa = collector.va();
        long bplFilterVa = bplFilter.va();
        long olFilterVa = olFilter.va();
        castAaBox(queryVa, boxCastVa, collectorVa, bplFilterVa, olFilterVa);
    }

    /**
     * Cast a ray and collect the resulting hits.
     *
     * @param raycast the test ray (not null, unaffected)
     * @param collector the hit collector to use (not null)
     */
    public void castRay(RayCast raycast, RayCastBodyCollector collector) {
        castRay(raycast, collector, new BroadPhaseLayerFilter());
    }

    /**
     * Cast a ray and collect the resulting hits.
     *
     * @param raycast the test ray (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     */
    public void castRay(RayCast raycast, RayCastBodyCollector collector,
            BroadPhaseLayerFilter bplFilter) {
        castRay(raycast, collector, bplFilter, new ObjectLayerFilter());
    }

    /**
     * Cast a ray and collect the resulting hits.
     *
     * @param raycast the test ray (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     * @param olFilter the object-layer filter to apply (not null, unaffected)
     */
    public void castRay(RayCast raycast, RayCastBodyCollector collector,
            BroadPhaseLayerFilter bplFilter, ObjectLayerFilter olFilter) {
        long queryVa = va();
        long raycastVa = raycast.va();
        long collectorVa = collector.va();
        long bplFilterVa = bplFilter.va();
        long olFilterVa = olFilter.va();
        castRay(queryVa, raycastVa, collectorVa, bplFilterVa, olFilterVa);
    }

    /**
     * Collect bodies whose bounding boxes overlap with the specified test box.
     *
     * @param box the test box (not null, unaffected)
     * @param collector the hit collector to use (not null)
     */
    public void collideAaBox(
            ConstAaBox box, CollideShapeBodyCollector collector) {
        collideAaBox(box, collector, new BroadPhaseLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes overlap with the specified test box.
     *
     * @param box the test box (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     */
    public void collideAaBox(
            ConstAaBox box, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter) {
        collideAaBox(box, collector, bplFilter, new ObjectLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes overlap with the specified test box.
     *
     * @param box the test box (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     * @param olFilter the object-layer filter to apply (not null, unaffected)
     */
    public void collideAaBox(
            ConstAaBox box, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter, ObjectLayerFilter olFilter) {
        long queryVa = va();
        long boxVa = box.targetVa();
        long collectorVa = collector.va();
        long bplFilterVa = bplFilter.va();
        long olFilterVa = olFilter.va();
        collideAaBox(queryVa, boxVa, collectorVa, bplFilterVa, olFilterVa);
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified oriented box.
     *
     * @param box the box to test (not null, unaffected)
     * @param collector the hit collector to use (not null)
     */
    public void collideOrientedBox(
            ConstOrientedBox box, CollideShapeBodyCollector collector) {
        collideOrientedBox(box, collector, new BroadPhaseLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified oriented box.
     *
     * @param box the box to test (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     */
    public void collideOrientedBox(
            ConstOrientedBox box, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter) {
        collideOrientedBox(box, collector, bplFilter, new ObjectLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified oriented box.
     *
     * @param box the box to test (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     * @param olFilter the object-layer filter to apply (not null, unaffected)
     */
    public void collideOrientedBox(
            ConstOrientedBox box, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter, ObjectLayerFilter olFilter) {
        long queryVa = va();
        long boxVa = box.targetVa();
        long collectorVa = collector.va();
        long bplFilterVa = bplFilter.va();
        long olFilterVa = olFilter.va();
        collideOrientedBox(
                queryVa, boxVa, collectorVa, bplFilterVa, olFilterVa);
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified point.
     *
     * @param point the location to test (not null, unaffected)
     * @param collector the hit collector to use (not null)
     */
    public void collidePoint(
            Vec3Arg point, CollideShapeBodyCollector collector) {
        collidePoint(point, collector, new BroadPhaseLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified point.
     *
     * @param point the location to test (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     */
    public void collidePoint(Vec3Arg point, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter) {
        collidePoint(point, collector, bplFilter, new ObjectLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified point.
     *
     * @param point the location to test (not null, unaffected)
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     * @param olFilter the object-layer filter to apply (not null, unaffected)
     */
    public void collidePoint(Vec3Arg point, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter, ObjectLayerFilter olFilter) {
        long queryVa = va();
        float pointX = point.getX();
        float pointY = point.getY();
        float pointZ = point.getZ();
        long collectorVa = collector.va();
        long bplFilterVa = bplFilter.va();
        long olFilterVa = olFilter.va();
        collidePoint(queryVa, pointX, pointY, pointZ, collectorVa, bplFilterVa,
                olFilterVa);
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified test sphere.
     *
     * @param center the center of the test sphere (not null, unaffected)
     * @param radius the radius of the test sphere
     * @param collector the hit collector to use (not null)
     */
    public void collideSphere(
            Vec3Arg center, float radius, CollideShapeBodyCollector collector) {
        collideSphere(center, radius, collector, new BroadPhaseLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified test sphere.
     *
     * @param center the center of the test sphere (not null, unaffected)
     * @param radius the radius of the test sphere
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     */
    public void collideSphere(
            Vec3Arg center, float radius, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter) {
        collideSphere(
                center, radius, collector, bplFilter, new ObjectLayerFilter());
    }

    /**
     * Collect bodies whose bounding boxes intersect the specified test sphere.
     *
     * @param center the center of the test sphere (not null, unaffected)
     * @param radius the radius of the test sphere
     * @param collector the hit collector to use (not null)
     * @param bplFilter the broadphase-layer filter to apply (not null,
     * unaffected)
     * @param olFilter the object-layer filter to apply (not null, unaffected)
     */
    public void collideSphere(
            Vec3Arg center, float radius, CollideShapeBodyCollector collector,
            BroadPhaseLayerFilter bplFilter, ObjectLayerFilter olFilter) {
        long queryVa = va();
        float centerX = center.getX();
        float centerY = center.getY();
        float centerZ = center.getZ();
        long collectorVa = collector.va();
        long bplFilterVa = bplFilter.va();
        long olFilterVa = olFilter.va();
        collideSphere(queryVa, centerX, centerY, centerZ, radius, collectorVa,
                bplFilterVa, olFilterVa);
    }

    /**
     * Access the underlying {@code PhysicsSystem}.
     *
     * @return the pre-existing instance, or {@code null} if none
     */
    public PhysicsSystem getSystem() {
        return (PhysicsSystem) getContainingObject();
    }
    // *************************************************************************
    // protected methods

    /**
     * Assign a native object (assuming there's none already assigned) and
     * designate the JVM object as the owner.
     *
     * @param queryVa the virtual address of the native object to assign (not
     * zero)
     */
    final void setVirtualAddressAsOwner(long queryVa) {
        Runnable freeingAction = () -> free(queryVa);
        setVirtualAddress(queryVa, freeingAction);
    }
    // *************************************************************************
    // native private methods

    native private static void castAaBox(long queryVa, long boxCastVa,
            long collectorVa, long bplFilterVa, long olFilterVa);

    native private static void castRay(long queryVa, long raycastVa,
            long collectorVa, long bplFilterVa, long olFilterVa);

    native private static void collideAaBox(long queryVa, long boxVa,
            long collectorVa, long bplFilterVa, long olFilterVa);

    native private static void collideOrientedBox(long queryVa, long boxVa,
            long collectorVa, long bplFilterVa, long olFilterVa);

    native private static void collidePoint(long queryVa, float pointX,
            float pointY, float pointZ, long collectorVa, long bplFilterVa,
            long olFilterVa);

    native private static void collideSphere(long queryVa, float centerX,
            float centerY, float centerZ, float radius, long collectorVa,
            long bplFilterVa, long olFilterVa);

    native private static void free(long queryVa);
}
