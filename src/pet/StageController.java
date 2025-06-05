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
    private boolean tendToMoveLeft = false;

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

    public double screenWidth() {
        return javafx.stage.Screen.getPrimary().getVisualBounds().getWidth();
    }
    public double screenHeight() {
        return javafx.stage.Screen.getPrimary().getVisualBounds().getHeight();
    }

    public boolean isAtLeftEdge() {
        return window.getX() <= 0 + 1;
    }

    public boolean isAtRightEdge() {
        return window.getX() + window.getWidth() >= screenWidth() - 1;
    }

    public boolean isAtTopEdge() {
        return window.getY() <= 0 + 1;
    }

    public boolean isAtBottomEdge() {
        return window.getY() >= screenHeight() - window.getHeight() - 1;
    }

    private boolean isTimeChangeStage() {
        long elapsedMillis = System.currentTimeMillis() - currentStageStartTime;
        if (currentStage == PetStage.STUMBLE) return elapsedMillis >= 500;
        return elapsedMillis >= 5000 && Math.random() < 0.3;
    }

    private PetStage decideNextStage() {
        if (dragHandler != null && dragHandler.isDragging()) {
            return PetStage.DRAG;
        }
        if (isAtTopEdge() && (isAtLeftEdge() || isAtRightEdge())) { // 左上角或右上角
            if(isTimeChangeStage()){
                switch (currentStage) {
                    case PetStage.LEFT_CLIMB_UP:
                        tendToMoveLeft = false;
                        return PetStage.RIGHT_SUSPENSION;
                    case PetStage.RIGHT_CLIMB_UP:
                        tendToMoveLeft = true;
                        return PetStage.LEFT_SUSPENSION;
                    case PetStage.LEFT_SUSPENSION:
                        return PetStage.LEFT_CLIMB_DOWN;
                    case PetStage.RIGHT_SUSPENSION:
                        return PetStage.RIGHT_CLIMB_DOWN;
                    default:
                        break;
                }
            }
            if (currentStage == PetStage.DRAG){
                if (isAtLeftEdge()){ // 左上
                    return (Math.random() < 0.5)? PetStage.LEFT_CLIMB_DOWN : PetStage.RIGHT_SUSPENSION;
                }else{ // 右上
                    return (Math.random() < 0.5)? PetStage.RIGHT_CLIMB_DOWN : PetStage.LEFT_SUSPENSION;
                }
            }
        }else if (isAtBottomEdge() && (isAtLeftEdge() || isAtRightEdge())) { // 左下角或右下角
            if(isTimeChangeStage()){
                switch (currentStage) {
                    case PetStage.LEFT_CLIMB_DOWN:
                        return PetStage.RIGHT_WALK;
                    case PetStage.RIGHT_CLIMB_DOWN:
                        return PetStage.LEFT_WALK;
                    case PetStage.LEFT_WALK:
                        return PetStage.LEFT_CLIMB_UP;
                    case PetStage.RIGHT_WALK:
                        return PetStage.RIGHT_CLIMB_UP;
                    default:
                        break;
                }
            }
            if (currentStage == PetStage.DRAG){
                if (isAtLeftEdge()){ // 左下
                    return (Math.random() < 0.5)? PetStage.LEFT_CLIMB_UP : PetStage.RIGHT_WALK;
                }else{ // 右下
                    return (Math.random() < 0.5)? PetStage.RIGHT_CLIMB_UP : PetStage.LEFT_WALK;
                }
            }
        }else if (isAtLeftEdge()) { // 左界
            switch (currentStage){
                case PetStage.LEFT_CLIMB_DOWN:
                case PetStage.LEFT_CLIMB_UP:
                    return currentStage;
                default:
                    return (Math.random() < 0.5)? PetStage.LEFT_CLIMB_DOWN : PetStage.LEFT_CLIMB_UP;
            }
        }else if (isAtRightEdge()) { // 右界
            switch (currentStage){
                case PetStage.RIGHT_CLIMB_DOWN:
                case PetStage.RIGHT_CLIMB_UP:
                    return currentStage;
                default:
                    return (Math.random() < 0.5)? PetStage.RIGHT_CLIMB_DOWN : PetStage.RIGHT_CLIMB_UP;
            }
        }else if (isAtTopEdge()) { // 上界
            if (isTimeChangeStage() || currentStage == PetStage.DRAG) {
                double left_p = (tendToMoveLeft)? 0.7 : 0.3;
                return (Math.random() < left_p)? PetStage.LEFT_SUSPENSION : PetStage.RIGHT_SUSPENSION;
            }
        }else if (!isAtBottomEdge()) { // 中間 (沒接觸到邊界)
            return PetStage.FALL;
        }else{ // 下界
            if (currentStage == PetStage.FALL) return PetStage.STUMBLE;
            if (isTimeChangeStage() || currentStage == PetStage.DRAG) {
                double r = Math.random();
                if (Math.random() < 0.5){
                    return (Math.random() < 0.5)? PetStage.SIT : PetStage.IDLE;
                }else{
                    double left_p = (tendToMoveLeft)? 0.7 : 0.3;
                    return (Math.random() < left_p)? PetStage.LEFT_WALK : PetStage.RIGHT_WALK;
                }
            }
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
