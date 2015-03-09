package rp.robotics.visualisation;

import rp.robotics.mapping.IGridMap;
import search.Node;

import lejos.geom.Point;
import lejos.robotics.mapping.LineMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

/***
 * Visualise an IGridMap on top of a LineMap.
 *
 * @author Nick Hawes
 */
public class GridMapVisualisation extends LineMapVisualisation {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected IGridMap m_gridMap;
	protected List<Node<Point>> path;

	public GridMapVisualisation(IGridMap _gridMap, LineMap _lineMap, float _scaleFactor) {
		super(_lineMap, _scaleFactor);
		m_gridMap = _gridMap;
	}

	private void connectToNeighbour(Graphics2D _g2, int _x, int _y, int _dx, int _dy) {
		if (m_gridMap.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			Point p1 = m_gridMap.getCoordinatesOfGridPosition(_x, _y);
			Point p2 = m_gridMap.getCoordinatesOfGridPosition(_x + _dx, _y + _dy);
			renderLine(p1, p2, _g2);
		}
	}

	@Override
	protected void renderMap(Graphics2D _g2) {
		// render lines first
		super.renderMap(_g2);

		_g2.setStroke(new BasicStroke(1));
		_g2.setPaint(Color.BLUE);

		// add grid
		for (int x = 0; x < m_gridMap.getXSize(); x++)
			for (int y = 0; y < m_gridMap.getYSize(); y++)
				if (!m_gridMap.isObstructed(x, y)) {
					Point gridPoint = m_gridMap.getCoordinatesOfGridPosition(x, y);
					renderPoint(gridPoint, _g2);
				}

		// and visualise valid connections
		for (int x = 0; x < m_gridMap.getXSize(); x++)
			for (int y = 0; y < m_gridMap.getXSize(); y++)
				if (m_gridMap.isValidGridPosition(x, y)) {
					connectToNeighbour(_g2, x, y, 1, 0);
					connectToNeighbour(_g2, x, y, 0, 1);
					connectToNeighbour(_g2, x, y, -1, 0);
					connectToNeighbour(_g2, x, y, 0, -1);
				}

		if (path != null)
			drawPath(_g2);

	}

	public void setPath(List<Node<Point>> path) {
		this.path = path;
	}
	public void drawPath(Graphics2D g2) {
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.RED);
		Point a, b;
		a = m_gridMap.getCoordinatesOfGridPosition((int) path.get(0).contents.x, (int) path.get(0).contents.y);
		for (int i = 1; i < path.size(); i++) {
			Point bp = path.get(i).contents;
			b = m_gridMap.getCoordinatesOfGridPosition((int) bp.x, (int) bp.y);
			renderLine(a, b, g2);
			a = b;
		}
	}
}
