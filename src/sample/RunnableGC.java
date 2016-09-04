package sample;

import javafx.scene.canvas.GraphicsContext;
import sample.Stroke;

public class RunnableGC implements Runnable {

    private GraphicsContext gc = null;
    private Stroke stroke = null;

    public RunnableGC(GraphicsContext gc, Stroke stroke) {
        this.gc = gc;
        this.stroke = stroke;
    }

    public void run() {
        gc.strokeOval(stroke.getStrokeX(), stroke.getStrokeY(), stroke.strokeSize, stroke.strokeSize); // <---- this is the actual work we need to do
    }
}


