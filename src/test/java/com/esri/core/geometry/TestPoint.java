/*
 Copyright 1995-2017 Esri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 For additional information, contact:
 Environmental Systems Research Institute, Inc.
 Attn: Contracts Dept
 380 New York Street
 Redlands, California, USA 92373

 email: contracts@esri.com
 */

package com.esri.core.geometry;

import java.util.Random;

import junit.framework.TestCase;

import org.junit.Test;

public class TestPoint extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testPt() {
		Point pt = new Point();
		assertTrue(pt.isEmpty());
		assertTrue(Double.isNaN(pt.getX()));
		assertTrue(Double.isNaN(pt.getY()));
		assertTrue(Double.isNaN(pt.getM()));
		assertTrue(pt.getZ() == 0);
		Point pt1 = new Point();
		assertTrue(pt.equals(pt1));
		int hash1 = pt.hashCode();
		pt.setXY(10, 2);
		assertFalse(pt.isEmpty());
		assertTrue(pt.getX() == 10);
		assertTrue(pt.getY() == 2);
		assertTrue(pt.getXY().equals(new Point2D(10, 2)));
		assertTrue(pt.getXYZ().x == 10);
		assertTrue(pt.getXYZ().y == 2);
		assertTrue(pt.getXYZ().z == 0);
		assertFalse(pt.equals(pt1));
		pt.copyTo(pt1);
		assertTrue(pt.equals(pt1));
		int hash2 = pt.hashCode();
		assertFalse(hash1 == hash2);
		pt.setZ(5);
		assertFalse(pt.equals(pt1));
		pt.copyTo(pt1);
		assertTrue(pt.equals(pt1));
		assertFalse(hash1 == pt.hashCode());
		assertFalse(hash2 == pt.hashCode());
		assertTrue(pt.hasZ());
		assertTrue(pt.getZ() == 5);
		assertTrue(pt.hasAttribute(VertexDescription.Semantics.Z));
		pt.toString();
	}

	@Test
	public void testEnvelope2000() {
		Point points[] = new Point[2000];
		Random random = new Random(69);
		for (int i = 0; i < 2000; i++) {
			points[i] = new Point();
			points[i].setX(random.nextDouble() * 100);
			points[i].setY(random.nextDouble() * 100);
		}
		for (int iter = 0; iter < 2; iter++) {
			final long startTime = System.nanoTime();
			Envelope geomExtent = new Envelope();
			Envelope fullExtent = new Envelope();
			for (int i = 0; i < 2000; i++) {
				points[i].queryEnvelope(geomExtent);
				fullExtent.merge(geomExtent);
			}
			long endTime = System.nanoTime();
		}
	}

	@Test
	public void testBasic() {
		assertTrue(Geometry.getDimensionFromType(Geometry.Type.Polygon.value()) == 2);
		assertTrue(Geometry
				.getDimensionFromType(Geometry.Type.Polyline.value()) == 1);
		assertTrue(Geometry
				.getDimensionFromType(Geometry.Type.Envelope.value()) == 2);
		assertTrue(Geometry.getDimensionFromType(Geometry.Type.Line.value()) == 1);
		assertTrue(Geometry.getDimensionFromType(Geometry.Type.Point.value()) == 0);
		assertTrue(Geometry.getDimensionFromType(Geometry.Type.MultiPoint
				.value()) == 0);

		assertTrue(Geometry.isLinear(Geometry.Type.Polygon.value()));
		assertTrue(Geometry.isLinear(Geometry.Type.Polyline.value()));
		assertTrue(Geometry.isLinear(Geometry.Type.Envelope.value()));
		assertTrue(Geometry.isLinear(Geometry.Type.Line.value()));
		assertTrue(!Geometry.isLinear(Geometry.Type.Point.value()));
		assertTrue(!Geometry.isLinear(Geometry.Type.MultiPoint.value()));

		assertTrue(Geometry.isArea(Geometry.Type.Polygon.value()));
		assertTrue(!Geometry.isArea(Geometry.Type.Polyline.value()));
		assertTrue(Geometry.isArea(Geometry.Type.Envelope.value()));
		assertTrue(!Geometry.isArea(Geometry.Type.Line.value()));
		assertTrue(!Geometry.isArea(Geometry.Type.Point.value()));
		assertTrue(!Geometry.isArea(Geometry.Type.MultiPoint.value()));

		assertTrue(!Geometry.isPoint(Geometry.Type.Polygon.value()));
		assertTrue(!Geometry.isPoint(Geometry.Type.Polyline.value()));
		assertTrue(!Geometry.isPoint(Geometry.Type.Envelope.value()));
		assertTrue(!Geometry.isPoint(Geometry.Type.Line.value()));
		assertTrue(Geometry.isPoint(Geometry.Type.Point.value()));
		assertTrue(Geometry.isPoint(Geometry.Type.MultiPoint.value()));

		assertTrue(Geometry.isMultiVertex(Geometry.Type.Polygon.value()));
		assertTrue(Geometry.isMultiVertex(Geometry.Type.Polyline.value()));
		assertTrue(!Geometry.isMultiVertex(Geometry.Type.Envelope.value()));
		assertTrue(!Geometry.isMultiVertex(Geometry.Type.Line.value()));
		assertTrue(!Geometry.isMultiVertex(Geometry.Type.Point.value()));
		assertTrue(Geometry.isMultiVertex(Geometry.Type.MultiPoint.value()));

		assertTrue(Geometry.isMultiPath(Geometry.Type.Polygon.value()));
		assertTrue(Geometry.isMultiPath(Geometry.Type.Polyline.value()));
		assertTrue(!Geometry.isMultiPath(Geometry.Type.Envelope.value()));
		assertTrue(!Geometry.isMultiPath(Geometry.Type.Line.value()));
		assertTrue(!Geometry.isMultiPath(Geometry.Type.Point.value()));
		assertTrue(!Geometry.isMultiPath(Geometry.Type.MultiPoint.value()));

		assertTrue(!Geometry.isSegment(Geometry.Type.Polygon.value()));
		assertTrue(!Geometry.isSegment(Geometry.Type.Polyline.value()));
		assertTrue(!Geometry.isSegment(Geometry.Type.Envelope.value()));
		assertTrue(Geometry.isSegment(Geometry.Type.Line.value()));
		assertTrue(!Geometry.isSegment(Geometry.Type.Point.value()));
		assertTrue(!Geometry.isSegment(Geometry.Type.MultiPoint.value()));
	}

	@Test
	public void testCopy() {
		Point pt = new Point();
		Point copyPt = (Point) pt.copy();
		assertTrue(copyPt.equals(pt));

		pt.setXY(11, 13);
		copyPt = (Point) pt.copy();
		assertTrue(copyPt.equals(pt));
		assertTrue(copyPt.getXY().isEqual(new Point2D(11, 13)));
		
		assertTrue(copyPt.getXY().equals((Object)new Point2D(11, 13)));
	}

	@Test
	public void testEnvelope2D_corners() {
		Envelope2D env = new Envelope2D(0, 1, 2, 3);
		assertFalse(env.equals(null));
		assertTrue(env.equals((Object)new Envelope2D(0, 1, 2, 3)));
		
		Point2D pt2D = env.getLowerLeft();
		assertTrue(pt2D.equals(Point2D.construct(0, 1)));
		pt2D = env.getUpperLeft();
		assertTrue(pt2D.equals(Point2D.construct(0, 3)));
		pt2D = env.getUpperRight();
		assertTrue(pt2D.equals(Point2D.construct(2, 3)));
		pt2D = env.getLowerRight();
		assertTrue(pt2D.equals(Point2D.construct(2, 1)));

		{
			Point2D[] corners = new Point2D[4];
			env.queryCorners(corners);
			assertTrue(corners[0].equals(Point2D.construct(0, 1)));
			assertTrue(corners[1].equals(Point2D.construct(0, 3)));
			assertTrue(corners[2].equals(Point2D.construct(2, 3)));
			assertTrue(corners[3].equals(Point2D.construct(2, 1)));
	
			env.queryCorners(corners);
			assertTrue(corners[0].equals(env.queryCorner(0)));
			assertTrue(corners[1].equals(env.queryCorner(1)));
			assertTrue(corners[2].equals(env.queryCorner(2)));
			assertTrue(corners[3].equals(env.queryCorner(3)));
		}
		
		{
			Point2D[] corners = new Point2D[4];
			env.queryCornersReversed(corners);
			assertTrue(corners[0].equals(Point2D.construct(0, 1)));
			assertTrue(corners[1].equals(Point2D.construct(2, 1)));
			assertTrue(corners[2].equals(Point2D.construct(2, 3)));
			assertTrue(corners[3].equals(Point2D.construct(0, 3)));
			
			env.queryCornersReversed(corners);
			assertTrue(corners[0].equals(env.queryCorner(0)));
			assertTrue(corners[1].equals(env.queryCorner(3)));
			assertTrue(corners[2].equals(env.queryCorner(2)));
			assertTrue(corners[3].equals(env.queryCorner(1)));
		}
		
		assertTrue(env.getCenter().equals(Point2D.construct(1, 2)));
		
		assertFalse(env.containsExclusive(env.getUpperLeft()));
		assertTrue(env.contains(env.getUpperLeft()));
		assertTrue(env.containsExclusive(env.getCenter()));
	}
	
	@Test
	public void testReplaceNaNs() {
		Envelope env = new Envelope();
		Point pt = new Point();
		pt.setXY(1, 2);
		pt.setZ(Double.NaN);
		pt.queryEnvelope(env);
		pt.replaceNaNs(VertexDescription.Semantics.Z, 5);
		assertTrue(pt.equals(new Point(1, 2, 5)));

		assertTrue(env.hasZ());
		assertTrue(env.queryInterval(VertexDescription.Semantics.Z, 0).isEmpty());
		env.replaceNaNs(VertexDescription.Semantics.Z, 5);
		assertTrue(env.queryInterval(VertexDescription.Semantics.Z, 0).equals(new Envelope1D(5, 5)));
	}	

	@Test
	public void testEqualsOn2DPoints() {
		int x = 10;
		int y = 15;

		Point2D base = new Point2D(x,y);
		Point2D comp = new Point2D(x,y);

		boolean isEqual;

		isEqual = base.equals(base);
		assertTrue(isEqual);

		isEqual = base.equals(comp);
		assertTrue(isEqual);

		comp.setCoords(x, y+1);

		isEqual = base.equals(comp);
		assertFalse(isEqual);

		Point3D comp3D = new Point3D(x,y,10);
		isEqual = base.equals(comp3D);
		assertFalse(isEqual);
	}

	@Test
	public void testEqualsOn2DCoordinates() {
		Point2D p = new Point2D(10,10);
		double x = 10;
		double y = 10;
		double z = 15;

		boolean isEqual;

		isEqual = p.isEqual(x, y);
		assertTrue(isEqual);

		isEqual = p.isEqual(x, z);
		assertFalse(isEqual);
	}

	@Test
	public void test2DNorm() {
		Point2D p1 = new Point2D(10,5);
		int metric;
		double d;

		metric = -1;
		d = p1._norm(metric);
		assertTrue(NumberUtils.isNaN(d));

		// L-infinite norm
		metric = 0;
		d = p1._norm(metric);
		assertTrue(d==p1.x);

		// L1, manhattan norm
		metric = 1;
		d = p1._norm(metric);
		assertTrue(d==(p1.x+p1.y));

		// L2, euclidian norm
		metric = 2;
		d = p1._norm(metric);
		assertTrue(d==(Math.sqrt(p1.x*p1.x+p1.y*p1.y)));

		// Default
		metric = 3;
		d = p1._norm(metric);
		assertTrue(d==(
			Math.pow(	Math.pow(p1.x, (double) metric) + 
						Math.pow(p1.y, (double) metric),
						1.0 / (double) metric)

		));
	}

	@Test
	public void test2DOffset() {
		Point2D p1 = new Point2D(0,0);
		Point2D p2 = new Point2D(0,0);
		Point2D base = new Point2D(5,10);
		double offset;
		double expected;

		offset = base.offset(p1, p2);
		expected = Math.sqrt((base.x-p1.x)*(base.x-p1.x)+(base.y-p1.y)*(base.y-p1.y));
		assertEquals(offset, expected);

		p2.setCoords(10, 0);
		offset = base.offset(p1,p2);
		expected = -10; //5*0-10*10
		assertEquals(offset, expected);

		p2.setCoords(0, 10);
		offset = base.offset(p1,p2);
		expected = 5; //5*10-10*0
		assertEquals(offset, expected);


	}

	@Test
	public void testRightPerpendicular() {
		Point2D p = new Point2D(10,5);

		p.rightPerpendicular(p);

		assertEquals(p.x,5.0);

		assertEquals(p.y,-5.0);
	}
}
