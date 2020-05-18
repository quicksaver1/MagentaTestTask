package Helpers.Utils;

public class Order{

    public Coordinate coordinate;
    public TimeWindow timeWindow;
    public int loadTime;
    public int unloadTime;

        public Order(Coordinate coordinate,TimeWindow timeWindow, int loadTime, int unloadTime){

            this.timeWindow=timeWindow;
            this.coordinate=coordinate;
            this.loadTime=loadTime;
            this.unloadTime=unloadTime;

        }
}
