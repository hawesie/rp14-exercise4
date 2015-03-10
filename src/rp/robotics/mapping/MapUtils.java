package rp.robotics.mapping;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;

import java.util.ArrayList;

/**
 * @author Nick Hawes
 */
public class MapUtils {

	private static final float BOARD_WIDTH = 1.7f;

	/**
	 * Create a rectangular map with 0,0 at the top left and the given width and height
	 *
	 * @param _width
	 * @param _height
	 * @return
	 */
	public static RPLineMap createRectangularMap(float _width, float _height) {

		// these are the walls for the world outline
		Line[] lines = new Line[] { new Line(0f, 0f, _width, 0f), new Line(_width, 0f, _width, _height), new Line(_width, _height, 0f, _height), new Line(0f, _height, 0f, 0f) };

		RPLineMap map = new RPLineMap(lines, new Rectangle(0, 0, _width, _height));

		return map;
	}

	/**
	 * Turns a straight line into a box with 4 walls around the line at the given width
	 *
	 * @return
	 */
	public static ArrayList<Line> lineToBox(float _x1, float _y1, float _x2, float _y2) {

		ArrayList<Line> box = new ArrayList<Line>(4);

		float expand = Math.round(BOARD_WIDTH / 2f);

		// extend in x direction
		if (_x1 == _x2) {
			box.add(new Line(_x1 - expand, _y1, _x1 + expand, _y1));
			box.add(new Line(_x1 + expand, _y1, _x2 + expand, _y2));
			box.add(new Line(_x2 + expand, _y2, _x2 - expand, _y2));
			box.add(new Line(_x2 - expand, _y2, _x1 - expand, _y1));
		}
		else if (_y1 == _y2) {
			box.add(new Line(_x1, _y1 + expand, _x2, _y2 + expand));
			box.add(new Line(_x2, _y2 - expand, _x2, _y2 + expand));
			box.add(new Line(_x1, _y1 - expand, _x2, _y2 - expand));
			box.add(new Line(_x1, _y1 - expand, _x1, _y1 + expand));

		}
		else
			throw new RuntimeException("can only use this with axis-aligned lines");

		return box;
	}

	/**
	 * Creates a grid map to match the training map as of 6/3/2013.
	 *
	 * @return
	 */
	public static RPLineMap createTrainingMap() {
		float height = 238;
		float width = 366;

		ArrayList<Line> lines = new ArrayList<Line>();

		// these are the walls for the world outline
		lines.add(new Line(0f, 0f, width, 0f));
		lines.add(new Line(width, 0f, width, height));
		lines.add(new Line(width, height, 0f, height));
		lines.add(new Line(0f, height, 0f, 0f));

		lines.add(new Line(75.0f, 133f, 100f, 133f));
		lines.add(new Line(75.0f, 193.0f, 100f, 193.0f));
		lines.add(new Line(100f, 133f, 100f, 193.0f));
		lines.add(new Line(75.0f, 133f, 75.0f, 193.0f));

		lines.addAll(lineToBox(42f, 67f, 287f, 67f));
		lines.add(new Line(287f, 0, 287f, 67f));
		lines.add(new Line(257f, 0, 257f, 67f));

		lines.addAll(lineToBox(135f, 129f, 255f, 129f));
		lines.addAll(lineToBox(135f, 129f, 135f, height));

		lines.addAll(lineToBox(194f, 191f, 254f, 191f));
		lines.addAll(lineToBox(194f, 191f, 194f, height));

		lines.add(new Line(width - 42f, 99f, width, 99f));
		lines.add(new Line(width - 42f, 159f, width, 159f));
		lines.add(new Line(width - 42f, 99f, width - 42f, 159f));
		Line[] lineArray = new Line[lines.size()];
		lines.toArray(lineArray);

		return new RPLineMap(lineArray, new Rectangle(0, 0, width, height));

	}

	public static RPLineMap create2014Map1() {

		float height = 241f;

		float width = 324f;

		ArrayList<Line> lines = new ArrayList<Line>();

		// these are the walls for the world outline

		lines.add(new Line(0f, 0f, width, 0f));

		lines.add(new Line(width, 0f, width, height));

		lines.add(new Line(width, height, 0f, height));

		lines.add(new Line(0f, height, 0f, 0f));

		// 1

		// down

		lines.addAll(lineToBox(27, 78, 27, 108));

		// right

		lines.addAll(lineToBox(27, 108, 60, 108));

		// up

		lines.addAll(lineToBox(60, 108, 60, 76));

		// right

		lines.addAll(lineToBox(60, 76, 92, 76));

		// up

		lines.addAll(lineToBox(92, 76, 92, 42));

		// left

		lines.addAll(lineToBox(92, 42, 62, 42));

		// 2

		// right

		lines.addAll(lineToBox(126, 45, 206, 45));

		// right

		lines.addAll(lineToBox(126, 78, 206, 78));

		// 3

		// left

		lines.addAll(lineToBox(240, 45, 270, 45));

		// down

		lines.addAll(lineToBox(240, 45, 240, 79));

		// right

		lines.addAll(lineToBox(240, 79, 272, 79));

		// down

		lines.addAll(lineToBox(272, 79, 272, 113));

		// right

		lines.addAll(lineToBox(272, 113, 306, 113));

		// up

		lines.addAll(lineToBox(306, 113, 306, 83));

		// 4 center

		// down

		lines.addAll(lineToBox(149, 108, 149, 140));

		// right

		lines.addAll(lineToBox(149, 140, 179, 140));

		// down

		lines.addAll(lineToBox(179, 140, 179, 108));

		// 5

		// up

		lines.addAll(lineToBox(28, 168, 28, 138));

		// right

		lines.addAll(lineToBox(28, 138, 62, 138));

		// down

		lines.addAll(lineToBox(62, 138, 62, 169));

		// right

		lines.addAll(lineToBox(62, 169, 94, 169));

		// down

		lines.addAll(lineToBox(94, 169, 94, 203));

		// left

		lines.addAll(lineToBox(94, 203, 63, 203));

		// 6

		// right

		lines.addAll(lineToBox(126, 169, 206, 169));

		// right

		lines.addAll(lineToBox(126, 198, 206, 198));

		// 7

		// left

		lines.addAll(lineToBox(267, 198, 237, 198));

		// up

		lines.addAll(lineToBox(237, 198, 237, 168));

		// right

		lines.addAll(lineToBox(237, 168, 269, 168));

		// up

		lines.addAll(lineToBox(269, 168, 269, 138));

		// right

		lines.addAll(lineToBox(269, 138, 303, 138));

		// down

		lines.addAll(lineToBox(303, 138, 303, 168));

		Line[] lineArray = new Line[lines.size()];

		lines.toArray(lineArray);

		return new RPLineMap(lineArray, new Rectangle(0, 0, width, height));
	}

	public static RPLineMap create2014Map2() {
		float height = 242;
		float width = 306;

		ArrayList<Line> lines = new ArrayList<Line>();

		lines.add(new Line(0f, 0f, width, 0f));
		lines.add(new Line(width, 0f, width, height));
		lines.add(new Line(width, height, 0f, height));
		lines.add(new Line(0f, height, 0f, 0f));

		// Left line
		lines.addAll(lineToBox(30, 71, 30, 171));

		// Top box
		lines.add(new Line(131, 0, 131, 37));
		lines.add(new Line(131, 37, 171, 37));
		lines.add(new Line(171, 37, 171, 0));

		// Middle
		lines.addAll(lineToBox(93, 108, 218, 108));
		lines.add(new Line(134, 108, 134, 168));
		lines.add(new Line(177, 108, 177, 168));
		lines.addAll(lineToBox(94, 168, 219, 168));

		// Bottom box
		lines.add(new Line(134, 242, 134, 206));
		lines.add(new Line(134, 206, 176, 206));
		lines.add(new Line(176, 206, 176, 242));

		// Right box
		lines.addAll(lineToBox(278, 71, 278, 171));
		// lines.addAll(lineToBox(268,71,268,171));

		Line[] lineArray = new Line[lines.size()];
		lines.toArray(lineArray);

		return new RPLineMap(lineArray, new Rectangle(0, 0, width, height));
	}

	
	public static RPLineMap create2015Map1() {

		float height = 244f;
		float width = 368f;
		
		final boolean createBoxLinesAsBoxes = false;

		ArrayList<Line> lines = new ArrayList<Line>();

		// Rectangular outer wall edges
		lines.addAll(createBox(0, 0, width, height, false));
		

		// Top left box
		lines.addAll(createBox(0, 30, 
							   32, 30+30,
							   createBoxLinesAsBoxes));
		// Bottom left box
		lines.addAll(createBox(0, height-65, 
							   30, height-31,
							   createBoxLinesAsBoxes));
		// Top right box
		lines.addAll(createBox(width-32, 30, 
							   width, 30+30,
							   createBoxLinesAsBoxes));
		// Bottom right box
		lines.addAll(createBox(width-32, height-61, 
							   width, height-31,
							   createBoxLinesAsBoxes));
		
		
		// Top-edge box
		lines.addAll(createBox(122, 0, 
							   122+90, 31,
							   createBoxLinesAsBoxes));
		// Bottom-edge box
		lines.addAll(createBox(121, height-31, 
							   121+90, height,
							   createBoxLinesAsBoxes));
		
		
		// Mid-left box
		lines.addAll(createBox(119, 90, 
							   119+32, 90+62,
							   createBoxLinesAsBoxes));
		// Mid-right box
		lines.addAll(createBox(119+32+62, 90, 
							   119+32+62+32, 90+62,
							   createBoxLinesAsBoxes));

		lines.add(new Line(119+32, 90+62, 119+32+62, 90+62));
		

		Line[] lineArray = new Line[lines.size()];

		lines.toArray(lineArray);

		return new RPLineMap(lineArray, new Rectangle(0, 0, width, height));
	}

	/**
	 * Create a box of lines.
	 * @param left The x-coordinate of the left side.
	 * @param top The y-coordinate of the top side.
	 * @param right The x-coordinate of the right side.
	 * @param bottom The y-coordinate of the bottom side.
	 * @return An arraylist of lines that make up that box.
	 */
	private static ArrayList<Line> createBox(float left, float top, float right, float bottom, boolean asLineBoxes)
	{
		ArrayList<Line> boxLines = new ArrayList<Line>();

		if (asLineBoxes)
		{
			boxLines.addAll(lineToBox(left, top, left, bottom)); // Left
			boxLines.addAll(lineToBox(left, top, right, top)); // Top
			boxLines.addAll(lineToBox(right, top, right, bottom)); // Right
			boxLines.addAll(lineToBox(left, bottom, right, bottom)); // Bottom
		}
		else
		{
			boxLines.add(new Line(left, top, left, bottom)); // Left
			boxLines.add(new Line(left, top, right, top)); // Top
			boxLines.add(new Line(right, top, right, bottom)); // Right
			boxLines.add(new Line(left, bottom, right, bottom)); // Bottom
		}
		
		return boxLines;
	}
	/**
	 * To string representation for a pose.
	 *
	 * @param _pose
	 * @return
	 */
	public static String toString(Pose _pose) {
		StringBuilder sb = new StringBuilder("Pose: ");
		sb.append(_pose.getX());
		sb.append(", ");
		sb.append(_pose.getY());
		sb.append(", ");
		sb.append(_pose.getHeading());
		return sb.toString();
	}

	/**
	 * Calculate the change in X coordinate to pose from move.
	 *
	 * @param _previousPose
	 * @param _move
	 * @return
	 */
	public static float changeInX(Pose _previousPose, Move _move) {
		return (_move.getDistanceTraveled() * ((float) Math.cos(Math.toRadians(_previousPose.getHeading()))));
	}

	/**
	 * Calculate the change in Y coordinate to pose from move.
	 *
	 * @param _previousPose
	 * @param _move
	 * @return
	 */
	public static float changeInY(Pose _previousPose, Move _move) {
		return (_move.getDistanceTraveled() * ((float) Math.sin(Math.toRadians(_previousPose.getHeading()))));
	}
}
