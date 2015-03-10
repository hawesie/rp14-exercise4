package rp.robotics;

import lejos.robotics.RangeScanner;
import lejos.robotics.localization.PoseProvider;

/**
 * Defines an interface for a range scanner that has a pose. The angles for the range scanner are relative to the heading of the pose.
 *
 * @author Nick Hawes
 */
public interface LocalisedRangeScanner extends PoseProvider, RangeScanner {

}
