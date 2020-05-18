import Helpers.DistributionCenter;
import Helpers.MainHelper;
import Helpers.Utils.Order;
import org.junit.BeforeClass;
import org.junit.Test;
import static Helpers.MainHelper.travelTime;
import static Helpers.MainHelper.Orders;

public class FindOptimalSchedule
{

    @BeforeClass
    public static void setup(){
        MainHelper.initData();
    }

    @Test
    public void myOwnAlgorithm(){
        double Solution[] = new double[Orders.size()+1];//array for optimal timestamps of beginning orders, where zero index is a DC.
        double currentTime = DistributionCenter.timeWindow.beginTime;
        double lastOrderTime=0;
        double comebackTime=0;
        for (Order Order1 : Orders) {//load all orders
            currentTime += Order1.loadTime;
        }
        for (int i=0;i<Orders.size();i++) {//delivering and unloading orders
            Order currentOrder = Orders.get(i);
            currentTime+=travelTime[i];
            currentTime=Math.max(currentTime,currentOrder.timeWindow.beginTime);
            if (i==Orders.size()-1){
                lastOrderTime=currentTime;
            }
            currentTime+=currentOrder.unloadTime;
            if(currentTime>currentOrder.timeWindow.endTime){
                System.out.println("Data Error: Impossible to commit order "+(i+1)+"\nCurrent time: "+ currentTime+"\nOrder end time: "+ currentOrder.timeWindow.endTime);
                throw new IllegalArgumentException("Wrong test data");
            }
        }
        currentTime+=travelTime[Orders.size()];//coming back to DC
        comebackTime=currentTime;
        if(currentTime>DistributionCenter.timeWindow.endTime){
            System.out.println("Data Error: it's impossible to come back to Distribution Center on time\nCurrent Time: "+currentTime+"\nDistribution Center end time: "+DistributionCenter.timeWindow.endTime);
            throw new IllegalArgumentException("Wrong test data");
        }
        //Reversal order
        if (lastOrderTime==0||comebackTime==0)
        {
            throw new IllegalArgumentException("last Order or come back  Time was not initialized");
        }
        currentTime=lastOrderTime;
        System.out.println(currentTime);
        for(int i=Orders.size()-1;i>0;i--){
            Solution[i+1]=currentTime;
            currentTime-=travelTime[i];
            currentTime=Math.min(currentTime,Orders.get(i-1).timeWindow.endTime);
            currentTime-=Orders.get(i-1).unloadTime;
        }
        Solution[1]=currentTime;
        currentTime-=travelTime[0];
        for (Helpers.Utils.Order Order : Orders) {
            currentTime -= Order.loadTime;
        }
        Solution[0]=currentTime;
        //Print solution
        System.out.println("Loading at Distribution Center. Time: "+Solution[0]);
        for(int i =0;i<Orders.size();i++){
            System.out.println("Unloading at order №"+(i+1)+". Time: "+ Solution[i+1]);
        }
        System.out.println("Coming back to Distribution Center. Time: "+comebackTime);
    }
}
