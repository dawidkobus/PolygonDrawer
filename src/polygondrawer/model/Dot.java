package polygondrawer.model;

import javafx.scene.shape.Circle;

public class Dot extends Circle {

	public Dot(double centerX, double centerY, double radius) {
		super(centerX, centerY, radius);
	}

	@Override
	public boolean equals(Object object) {
		boolean isEqual = false;
		if (object != null && object instanceof Dot) {
			isEqual = (this.centerXProperty().toString().equals(((Dot) object).centerXProperty().toString()))
					&& (this.centerYProperty().toString().equals(((Dot) object).centerYProperty().toString()));
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		return centerXProperty().hashCode() + centerYProperty().hashCode();
	}
}
