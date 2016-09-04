package sample;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by RdDvls on 9/4/16.
 */
public class Stroke {
 double strokeX;
    double strokeY;
    double strokeSize;

    public Stroke (double strokeX, double strokeY, double strokeSize){
        this.strokeX = strokeX;
        this.strokeY = strokeY;
        this.strokeSize = strokeSize;
    }

    public double getStrokeX() {
        return strokeX;
    }

    public void setStrokeX(double strokeX) {
        this.strokeX = strokeX;
    }

    public double getStrokeY() {
        return strokeY;
    }

    public void setStrokeY(double strokeY) {
        this.strokeY = strokeY;
    }

    public double getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }

    public Stroke() {

    }
}
