package polygondrawer;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import polygondrawer.model.Dot;

public class Controller {

	public static final String TITLE = "Polygon Drawer";
	public static final String ICONPATH = "file:res/icon.png";

	@FXML
	private AnchorPane pane;
	@FXML
	private Slider dotSizeSlider;
	@FXML
	private Slider gridSizeSlider;
	@FXML
	private CheckBox fillCheckBox;

	private List<Dot> dots = new ArrayList<>();
	private List<Double> coordinates = new ArrayList<>();
	private Polygon polygon = new Polygon();
	private Tooltip tooltip = new Tooltip();

	private int gridSize = 20;
	private int dotSize = 6;
	private boolean filled = true;

	@FXML
	private void initialize() {
		drawGrid();
		Tooltip.install(pane, tooltip);

		dotSizeSlider.valueProperty().addListener(event -> {
			changeDotSize();
		});

		gridSizeSlider.valueProperty().addListener(event -> {
			changeGridSize();
		});

		pane.setOnMouseMoved(event -> {
			tooltip.setText(getX(event.getX()) + ", " + getY(event.getY()));
		});

		pane.setOnMousePressed(event -> {
			checkMouseButton(event.getButton(), new Dot(getX(event.getX()), getY(event.getY()), dotSize));
		});
	}

	@FXML
	private void clearAll() {
		pane.getChildren().clear();
		polygon.getPoints().clear();
		coordinates.clear();
		dots.clear();
		drawGrid();
	}

	@FXML
	private void fillPolygon() {
		filled = fillCheckBox.isSelected();
		drawAll();
	}

	@FXML
	private void scale() {
		double smallestX = pane.getMaxWidth();
		double smallestY = pane.getMaxHeight();
		for (Dot dot : dots) {
			if (dot.getCenterX() < smallestX) {
				smallestX = dot.getCenterX();
			}
			if (dot.getCenterY() < smallestY) {
				smallestY = dot.getCenterY();
			}
		}
		for (Dot dot : dots) {
			dot.setCenterX(dot.getCenterX() - smallestX);
			dot.setCenterY(dot.getCenterY() - smallestY);
		}
		drawAll();
	}

	@FXML
	public void getPoints() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
		content.putString(buildPointsString(new StringBuilder()));
		clipboard.setContent(content);
		notifyUser(new Alert(AlertType.INFORMATION));
	}

	private void notifyUser(Alert alert) {
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(ICONPATH));
		alert.setTitle(TITLE);
		alert.setHeaderText(null);
		alert.setContentText("Polygon coordinates has been copied to your clipboard.");
		alert.showAndWait();
	}

	private String buildPointsString(StringBuilder builder) {
		for (Dot dot : dots) {
			builder.append(dot.getCenterX() + ", " + dot.getCenterX() + ",\n");
		}
		if (dots.size() > 2) {
			builder.delete(builder.length() - 2, builder.length());
		}
		return builder.toString();
	}

	private void drawAll() {
		pane.getChildren().remove(polygon);
		polygon.getPoints().clear();
		coordinates.clear();
		convertDotsToCoordinates();
		polygon.getPoints().addAll(coordinates);
		if (filled && !coordinates.isEmpty()) {
			pane.getChildren().add(polygon);
		}
	}

	private void convertDotsToCoordinates() {
		for (Dot dot : dots) {
			coordinates.add(dot.getCenterX());
			coordinates.add(dot.getCenterY());
		}
	}

	private void drawDots() {
		for (Dot dot : dots) {
			dot.setRadius(dotSize);
			pane.getChildren().remove(dot);
			pane.getChildren().add(dot);
		}
	}

	private void drawGrid() {
		for (int i = 0; i < pane.getMaxWidth(); i += gridSize) {
			pane.getChildren().add(new Line(i, 0, i, pane.getMaxHeight()));
		}
		for (int i = 0; i < pane.getMaxHeight(); i += gridSize) {
			pane.getChildren().add(new Line(0, i, pane.getMaxWidth(), i));
		}
	}

	private void changeDotSize() {
		dotSize = (int) dotSizeSlider.getValue();
		drawDots();
	}

	private void changeGridSize() {
		gridSize = (int) gridSizeSlider.getValue();
		clearAll();
	}

	private void checkMouseButton(MouseButton button, Dot dot) {
		if (button.equals(MouseButton.SECONDARY)) {
			if (dots.contains(dot)) {
				dots.remove(dot);
				pane.getChildren().remove(dot);
			}
			drawAll();
		} else if (button.equals(MouseButton.PRIMARY)) {
			if (!dots.contains(dot)) {
				dots.add(dot);
				pane.getChildren().add(dot);
			}
			drawAll();
			addDraggedListners();
		}
	}

	private void addDraggedListners() {
		for (Dot dot : dots) {
			dot.setOnMouseDragged(event -> {
				dot.setCenterX(getX(event.getX()));
				dot.setCenterY(getY(event.getY()));
				drawAll();
			});
		}
	}

	private int getX(double x) {
		int result = (int) Math.round(x / gridSize) * gridSize;
		if (result > pane.getMaxWidth()) {
			result = (int) pane.getMaxWidth();
		} else if (result < 0) {
			result = 0;
		}
		return result;
	}

	private int getY(double y) {
		int result = (int) Math.round(y / gridSize) * gridSize;
		if (result > pane.getMaxHeight()) {
			result = (int) pane.getMaxHeight();
		} else if (result < 0) {
			result = 0;
		}
		return result;
	}

}
