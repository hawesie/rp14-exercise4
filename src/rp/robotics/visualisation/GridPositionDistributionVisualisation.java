package rp.robotics.visualisation;

import rp.robotics.localisation.GridPositionDistribution;

import lejos.geom.Point;
import lejos.robotics.mapping.LineMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class GridPositionDistributionVisualisation extends GridMapVisualisation {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final float BIGGEST_POINT_RADIUS = 60;

	protected GridPositionDistribution m_gridDistribution;

	public GridPositionDistributionVisualisation(GridPositionDistribution _distribution, LineMap _lineMap, float _scaleFactor, boolean _flip) {
		super(_distribution.getGridMap(), _lineMap, _scaleFactor, _flip);
		m_gridDistribution = _distribution;
	}

	/**
	 * Set distribution to be visualised. Does not update linemap part.
	 *
	 * @param _gridDistribution
	 */
	public void setDistribution(GridPositionDistribution _gridDistribution) {
		m_gridDistribution = _gridDistribution;
	}

	@Override
	protected void renderMap(Graphics2D _g2) {
		// render lines first
		super.renderMap(_g2);

		_g2.setStroke(new BasicStroke(1));
		_g2.setPaint(Color.BLUE);

		// then add grid
		for (int x = 0; x < m_gridDistribution.getGridWidth(); x++)
			for (int y = 0; y < m_gridDistribution.getGridHeight(); y++)
				if (!m_gridDistribution.isObstructed(x, y)) {
					Point gridPoint = m_gridDistribution.getGridMap().getCoordinatesOfGridPosition(x, y);

					float radius = BIGGEST_POINT_RADIUS * m_gridDistribution.getProbability(x, y);
					if (radius > 0) {
						if (radius < 1)
							radius = 1;
						renderPoint(gridPoint, _g2, (int) radius);
					}
				}

	}
}
