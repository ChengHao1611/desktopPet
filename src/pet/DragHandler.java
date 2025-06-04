package pet;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class DragHandler {

    private final Stage stage;
    private boolean dragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;
    private long dragStartTime = 0;
    private long dragEndTime = 0;

    public DragHandler(Stage stage) {
        this.stage = stage;
    }

    public void attachToScene(Scene scene) {
        scene.setOnMousePressed(e -> {
            dragging = true;
            dragOffsetX = e.getScreenX() - stage.getX();
            dragOffsetY = e.getScreenY() - stage.getY();
            dragStartTime = System.currentTimeMillis();
        });

        scene.setOnMouseDragged(e -> {
            if (dragging) {
                stage.setX(e.getScreenX() - dragOffsetX);
                stage.setY(e.getScreenY() - dragOffsetY);
            }
        });

        scene.setOnMouseReleased(e -> {
            if (dragging) {
                dragging = false;
                dragEndTime = System.currentTimeMillis();
            }
        });
    }

    public boolean isDragging() {
        return dragging;
    }

    public long getDragStartTime() {
        return dragStartTime;
    }

    public long getDragEndTime() {
        return dragEndTime;
    }

    public long getLastDragDuration() {
        return dragEndTime - dragStartTime;
    }
}
