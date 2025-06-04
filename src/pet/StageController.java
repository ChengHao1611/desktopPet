package pet;

import javafx.application.Platform;
import javafx.stage.Stage;

public class StageController {

    private final Stage window;
    private volatile PetStage currentStage;
    private Thread stateThread;
    private volatile boolean running;
    private volatile boolean paused;
    private long currentStageStartTime;
    private long timeStep = 100;
    private DragHandler dragHandler;

    public StageController(Stage window) {
        this.window = window;
        this.currentStage = PetStage.IDLE;
        
        Platform.runLater(() -> {
            if (window.getScene() != null) {
                dragHandler = new DragHandler(window);
                dragHandler.attachToScene(window.getScene());
            }
        });
    }

    public PetStage getCurrentStage() {
        return currentStage;
    }

    public void start() {
        if (stateThread != null && stateThread.isAlive()) return; // 避免重複啟動
        running = true;
        paused = false;
        stateThread = new Thread(() -> {
            while (running) {
                try {
                    if (!paused) {
                        updateStage();
                    }
                    Thread.sleep(timeStep); // 每?毫秒更新一次狀態
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        stateThread.setDaemon(true);
        stateThread.start();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void stop() {
        running = false;
        if (stateThread != null) {
            stateThread.interrupt();
        }
    }

    private double screenWidth() {
        return javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
    }
    private double screenHeight() {
        return javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();
    }

    private boolean isAtLeftEdge() {
        return window.getX() <= 0;
    }

    private boolean isAtRightEdge() {
        return window.getX() + window.getWidth() >= screenWidth();
    }

    private boolean isAtTopEdge() {
        return window.getY() <= 0;
    }

    private boolean isAtBottomEdge() {
        return window.getY() + window.getHeight() >= screenHeight();
    }

    private boolean isTimeChangeStage() {
        long elapsedMillis = System.currentTimeMillis() - currentStageStartTime;
        return elapsedMillis >= 5000 && Math.random() < 0.3;
    }

    private PetStage decideNextStage() {
        if (dragHandler != null && dragHandler.isDragging()) {
            return PetStage.DRAG;
        }
        if (isAtTopEdge() && (isAtLeftEdge() || isAtRightEdge())) {
            if(isTimeChangeStage() && currentStage == PetStage.LEFT_CLIMB_UP) return PetStage.SUSPENSION;
            if(isTimeChangeStage() && currentStage == PetStage.SUSPENSION) return PetStage.LEFT_CLIMB_UP;
            if(currentStage != PetStage.LEFT_CLIMB_UP && currentStage != PetStage.SUSPENSION){
                double r = Math.random();
                return (r < 0.5)? PetStage.LEFT_CLIMB_UP : PetStage.SUSPENSION;
            }
        }else if (isAtBottomEdge() && (isAtLeftEdge() || isAtRightEdge())) {
            if(isTimeChangeStage() && currentStage == PetStage.LEFT_CLIMB_UP) return PetStage.LEFT_WALK;
            if(isTimeChangeStage() && currentStage == PetStage.LEFT_WALK) return PetStage.LEFT_CLIMB_UP;
            if(currentStage != PetStage.LEFT_CLIMB_UP && currentStage != PetStage.LEFT_WALK){
                double r = Math.random();
                return (r < 0.5)? PetStage.LEFT_CLIMB_UP : PetStage.LEFT_WALK;
            }
        }else if (isAtLeftEdge() || isAtRightEdge()) {
            return PetStage.LEFT_CLIMB_UP;
        }else if (isAtTopEdge()) {
            return PetStage.SUSPENSION;
        }else if (!isAtBottomEdge()) {
            return PetStage.FALL;
        }else if (isTimeChangeStage() || currentStage == PetStage.DRAG) {
            double r = Math.random();
            if (r < 0.1) return (currentStage == PetStage.LEFT_WALK)? PetStage.STUMBLE : PetStage.LEFT_WALK;
            else if (r < 0.4) return PetStage.SIT;
            else if(r < 0.7) return PetStage.IDLE;
            else return PetStage.LEFT_WALK;
        }
        return currentStage;
    }

    private void updateStage() {
        Platform.runLater(() -> {
            double x = window.getX();
            double y = window.getY();
            double width = window.getWidth();
            double height = window.getHeight();
            double screenWidth = javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();

            PetStage nextStage = decideNextStage();
            if (nextStage != currentStage){
                currentStageStartTime = System.currentTimeMillis();
                currentStage = nextStage;
            }
        });
    }
}
