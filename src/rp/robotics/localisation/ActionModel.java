package rp.robotics.localisation;

import rp.robotics.mapping.Heading;

/**
 * An interface for an action model to reason about movements over a grid map. This interface ignores rotation actions as a simplification.
 *
 * @author Nick Hawes
 */
public interface ActionModel {

	/***
	 * Update the given distribution assuming the robot has successfully moved one grid position in the direction given by the heading. This heading is in the global coordinate frame.
	 *
	 * @param _dist The distribution over robot position before the move was made
	 * @param _heading The direction of the successful move
	 * @return The distribution over robot position after the application of the action model
	 */
	public GridPositionDistribution updateAfterMove(GridPositionDistribution _dist, Heading _heading);

}
