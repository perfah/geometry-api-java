/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esri.core.geometry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.Transient;

public class TestEnvelope
{
	@Test
	/**The function returns the x and y coordinates of the center of the envelope.
	 * If the envelope is empty the point is set to empty otherwise the point is set to the center of the envelope.
	 */
	public void testGetCeneter(){
		//xmin,ymin,xmax,ymax of envelope
		Envelope env1 = new Envelope(1,1, 2, 4);
		Envelope env2 = new Envelope();
		Point p = new Point();
		Point p1 = new Point(1,2);

		/**Tests if the point is correctly set to the center of the envelope,   */
		env1.getCenter(p);
		assertTrue(p.getX() == 1.5);

		/** Tests if the point is empty because of the envelope is empty */
		env2.getCenter(p1);
		assertTrue(p1.isEmpty());
	}
	@Test
	/* Merge takes a Point as input and increas the bouandary of the envelope to contain the point.
	 *If the point is empty the envelope remains the same or if the envelope is empty the coordinates 
	 *of the point is assigned to the envelope */
	public void testMerge(){

		/* To increase the covarege the branch where the envelope is empty can be tested
		 * And that the envelope and the point is not empty */
		Envelope env1 = new Envelope(1,1, 2, 4);
		Envelope env2 = new Envelope(1,1, 2, 4);
		Envelope env3 = new Envelope(1,1, 2, 4);
		Point p = new Point(100,4);

		/*This should be false since env1 should change depending on point p */
		env1.merge(p);
		assertFalse(env1.equals(env2));
		
		/* This assert should be true since the point is empty and therefore env2 should not change */
		Point p1 = new Point();
		env2.merge(p1);
		assertTrue(env2.equals(env3));
	}


	@Test
	/** TESTEST ENVELOPE2D **
	 * ClipLine modify a line to be inside a envelope if possible */
	public void TestClipLine(){
		
		//checking if segPrama is 0 and the segment is outside of the clipping window
		//covers first return
		Envelope2D env0 = new Envelope2D(1, 1, 4, 4);
		// Reaches the branch where the delta is 0
		Point2D p1 = new Point2D(2,2);
		Point2D p2 = new Point2D(2,2);

		int lineExtension = 0;
		double[] segParams = {3,4};
		//reaches the branch where boundaryDistances is not 0
		double[] boundaryDistances = {2.0, 3.0};

		int a = env0.clipLine(p1, p2, lineExtension, segParams, boundaryDistances);
		//should be true since the points are inside the envelope
		assertTrue(a == 4);
		
		// Changes p3 to fit the envelop, the line is on the edge of the envelope
		Envelope2D env1 = new Envelope2D(1, 1, 4, 4);
		Point2D p3 = new Point2D(1,10);
		Point2D p4 = new Point2D(1,1);

		int b = env1.clipLine(p3, p4, lineExtension, segParams, boundaryDistances);
		assertTrue(b == 1);
		// the second point is outside and therefore changed
		Envelope2D env2 = new Envelope2D(1, 1, 4, 4);
		Point2D p5 = new Point2D(2,2);
		Point2D p6 = new Point2D(1,10);

		int c = env2.clipLine(p5, p6, lineExtension, segParams, boundaryDistances);
		assertTrue(c == 2);

		//Both points is outside the envelope and therefore no line is possible to clip, and this should return 0
		Envelope2D env3 = new Envelope2D(1, 1, 4, 4);
		Point2D p7 = new Point2D(11,10);
		Point2D p8 = new Point2D(5,5);

		int d = env3.clipLine(p7, p8, lineExtension, segParams, boundaryDistances);
		assertTrue(d == 0);
	}
	
	@Test
	/**Caluculates the distance between the envlelope and a 2D point */
	public void testSqrDistances(){
		//the point is on the envelope, which means that the distance is 0
		Envelope2D env0 = new Envelope2D(1, 1, 4, 4);
		Point2D p0 = new Point2D(4,4);
		assertTrue(env0.sqrDistance(p0) == 0.0);

		Envelope2D env1 = new Envelope2D(1, 1, 4, 4);
		Point2D p1 = new Point2D(1,0);
		
		assertTrue(env0.sqrDistance(p1) == 1.0);

	}
	/**The functon returns the max distance between two envelopes */
@Test 
	public void testsqrMaxDistance(){
		// If one of the envelopes is empty the funtions should return NaN
		Envelope2D env0 = new Envelope2D();
		Envelope2D env1 = new Envelope2D(1, 1, 4, 4);

		double a = env0.sqrMaxDistance(env1);
		// NaN does not equals NaN
		assertTrue(a != a);
		
		//A testacase that covers the rest of the branches
		Envelope2D env2 = new Envelope2D(5,1,6,1);
		Envelope2D env3 = new Envelope2D(1, 1, 4, 4);
		
		assertTrue(env2.sqrMaxDistance(env3) == 34.0);
	}
	/** Snaps a point to the boandary of the envelope */
	@Test
	public void test_snapToBoundary(){
		//Test if the point snaps to the boandry if the points has the same y-min value
		Envelope2D env0 = new Envelope2D(1, 1, 4, 4);
		Point2D p0 = new Point2D(5,1);
		Point2D p1 = new Point2D(4,1);

		p0 = env0._snapToBoundary(p0);
		
		assertTrue(p0.equals(p1));

		//Test if the the point is on the boundary of the envelope
		Envelope2D env1 = new Envelope2D(1, 1, 4, 4);
		Point2D p2 = new Point2D(1,4);
		Point2D p3 = new Point2D(1,4);

		p2 = env0._snapToBoundary(p2);
		
		assertTrue(p2.equals(p3));

		//Test if the point is inside the envelope
		Envelope2D env3 = new Envelope2D(1, 1, 4, 4);
		Point2D p4 = new Point2D(2,2);
		Point2D p5 = new Point2D(2,1);

		p4 = env0._snapToBoundary(p4);
		assertTrue(p4.equals(p5));

	}
	// Calculates distance of point from lower left corner of envelope,
    // moving clockwise along the envelope boundary.
    // The input point is assumed to lie exactly on envelope boundary
    // If this is not the case then a projection to the nearest position on the
    // envelope boundary is performed.
    // (If the user knows that the input point does most likely not lie on the
    // boundary,
    // it is more efficient to perform ProjectToBoundary before using this
	// function)
	
	@Test
	public void test_boundaryDistance(){
		//test if the point is on the first side
		Envelope2D env0 = new Envelope2D(1, 1, 4, 4);
		Point2D p0 = new Point2D(1,2);

		//Since xmin is equal p0.x the this should return  p0.y -ymin
		assertTrue(env0._boundaryDistance(p0) == 1.0);

		//Test if the point is on the top side
		Envelope2D env1 = new Envelope2D(1, 1, 4, 4);
		Point2D p1 = new Point2D(1,4);

		//Since ymax is equal p0.y the this should return  ymax - ymin + pt.x - xmin
		assertTrue(env1._boundaryDistance(p1) == 3.0);

		//Test if the point is outside of the envelope
		Envelope2D env2 = new Envelope2D(1, 1, 4, 4);
		Point2D p2 = new Point2D(0,1);

		//Since ymax is equal p0.y the this should return  ymax - ymin + pt.x - xmin
		
		assertTrue(env2._boundaryDistance(p2) == 13.0);

	}
	//Returns the side a point lies, 0 is left, 1 is top 2 is right 3 is bottom
	@Test
	public void test_envelopeSide(){
		//if the point is between 0 and 3, it should return 0
		Envelope2D env0 = new Envelope2D(1, 1, 4, 4);
		Point2D p0 = new Point2D(0,0);
		int a = env0._envelopeSide(p0);
		assertTrue(a == 0);

		//should be true since the point is on the lower boundary of the envelope
		Envelope2D env1 = new Envelope2D(1, 1, 4, 4);
		Point2D p1 = new Point2D(2,1);
		int b = env1._envelopeSide(p1);
		assertTrue(b == 3);


	}
	/** NEW TEST ENDS HERE */
	
	@Test
	public void testIntersect() {
		assertIntersection(new Envelope(0, 0, 5, 5), new Envelope(0, 0, 5, 5), new Envelope(0, 0, 5, 5));
		assertIntersection(new Envelope(0, 0, 5, 5), new Envelope(1, 1, 6, 6), new Envelope(1, 1, 5, 5));
		assertIntersection(new Envelope(1, 2, 3, 4), new Envelope(0, 0, 2, 3), new Envelope(1, 2, 2, 3));

		assertNoIntersection(new Envelope(), new Envelope());
		assertNoIntersection(new Envelope(0, 0, 5, 5), new Envelope());
		assertNoIntersection(new Envelope(), new Envelope(0, 0, 5, 5));
	}

	@Test
	public void testEquals() {
		Envelope env1 = new Envelope(10, 9, 11, 12);
		Envelope env2 = new Envelope(10, 9, 11, 13);
		Envelope1D emptyInterval = new Envelope1D();
		emptyInterval.setEmpty();
		assertFalse(env1.equals(env2));
		env1.queryInterval(VertexDescription.Semantics.M, 0).equals(emptyInterval);
		env2.setCoords(10, 9, 11, 12);
		assertTrue(env1.equals(env2));
		env1.addAttribute(VertexDescription.Semantics.M);
		env1.queryInterval(VertexDescription.Semantics.M, 0).equals(emptyInterval);
		assertFalse(env1.equals(env2));
		env2.addAttribute(VertexDescription.Semantics.M);
		assertTrue(env1.equals(env2));
		Envelope1D nonEmptyInterval = new Envelope1D();
		nonEmptyInterval.setCoords(1, 2);
		env1.setInterval(VertexDescription.Semantics.M, 0, emptyInterval);
		assertTrue(env1.equals(env2));
		env2.setInterval(VertexDescription.Semantics.M, 0, emptyInterval);
		assertTrue(env1.equals(env2));
		env2.setInterval(VertexDescription.Semantics.M, 0, nonEmptyInterval);
		assertFalse(env1.equals(env2));
		env1.setInterval(VertexDescription.Semantics.M, 0, nonEmptyInterval);
		assertTrue(env1.equals(env2));
		env1.queryInterval(VertexDescription.Semantics.M, 0).equals(nonEmptyInterval);
		env1.queryInterval(VertexDescription.Semantics.POSITION, 0).equals(new Envelope1D(10, 11));
		env1.queryInterval(VertexDescription.Semantics.POSITION, 0).equals(new Envelope1D(9, 13));
	}

	@Test
	public void testIntersect1D(){
		Envelope1D _this;
		Envelope1D other;

		// Check that '_this' envelope is no larger than envelope 'other':
		_this = new Envelope1D(5.0, 25.0);
		other = new Envelope1D(10.0, 20.0);
		_this.intersect(other);
		assertEquals(_this.vmin, other.vmin, 0.00000000000001);
		assertFalse(_this.isEmpty());
		assertFalse(other.isEmpty());

		// If envelope 'other' is empty, '_this' envelope should become empty:
		_this = new Envelope1D(5.0, 25.0);
		other = new Envelope1D(10.0, 20.0);
		other.setEmpty();
		_this.intersect(other);
		assertTrue(_this.isEmpty());
	}

	@Test
	public void testInflate1D() {
		Envelope1D _this;

		_this = new Envelope1D();
		_this.setEmpty();
		_this.inflate(5.0);
		assertTrue(_this.isEmpty());

		_this = new Envelope1D(-5.0, 5.0);
		_this.inflate(5.0);
		assertEquals(_this.vmin, -10, 0.000000001);
		assertEquals(_this.vmax, 10, 0.000000001);
	}

	@Test
	public void testReaspect() {
		Envelope2D env = new Envelope2D(1,1,5,5);

		env.reaspect(15.0, 5.0);

		assertEquals(env.xmax,9.0,0.000001);
		assertEquals(env.xmin,-3.0,0.000001);
		assertEquals(env.ymax,5.0,0.000001);
		assertEquals(env.ymin,1.0,0.000001);
	}

	private static void assertIntersection(Envelope envelope, Envelope other, Envelope intersection) {
		boolean intersects = envelope.intersect(other);
		assertTrue(intersects);
		assertEquals(envelope, intersection);
	}

	private static void assertNoIntersection(Envelope envelope, Envelope other) {
		boolean intersects = envelope.intersect(other);
		assertFalse(intersects);
		assertTrue(envelope.isEmpty());
	}
	
}

