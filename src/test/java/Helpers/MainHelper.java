package Helpers;

import Helpers.Utils.Coordinate;
import Helpers.Utils.Order;
import Helpers.Utils.TimeWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainHelper
{

    public static final String PATH_TO_TEST_DATA= "/src/test/resources/TestData.txt";

    public static double travelTime[]; //travelTime[i] = time required to travel from i order to i+1 order
    public static ArrayList<Order> Orders = new ArrayList<Order>();
    public static void initData(){
        File dataFile=new File(System.getProperty("user.dir")+PATH_TO_TEST_DATA);
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(dataFile);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        assert scanner != null;
        DistributionCenter.ResourceSpeed= Integer.parseInt(scanner.nextLine().split("\\s+")[1]);
        scanner.nextLine();
        String line = null;
        while(scanner.hasNextLine()) {
            line=scanner.nextLine();
            if (line.isEmpty())
            {
                break;
            }
            System.out.println(line);
            parseDataString(line);
        }
        if (DistributionCenter.timeWindow ==null) {
            throw new IllegalArgumentException("Dc data was not set");
        }
        initTravelTimes();

    }

    private static void initTravelTimes() {
        travelTime=new double[Orders.size()+1];
        travelTime[0]= distanceBetween(Orders.get(1).coordinate, DistributionCenter.coordinate)/ DistributionCenter.ResourceSpeed;
        for (int i=1;i<Orders.size();i++) {
            travelTime[i]=distanceBetween(Orders.get(i-1),Orders.get(i))/ DistributionCenter.ResourceSpeed;
        }
        travelTime[Orders.size()]=distanceBetween(Orders.get(Orders.size()-1).coordinate, DistributionCenter.coordinate);

    }

    private static void parseDataString(String dataString) { //CUST_ID.   XCOORD.   YCOORD.    READY_TIME   DUE_DATE   LOAD_TIME UNLOAD_TIME
        String[] parseData = dataString.split("\\s+");
        double x=Double.parseDouble(parseData[1]);
        double y=Double.parseDouble(parseData[2]);
        double beginTime = Integer.parseInt(parseData[3]);
        double endTime= Integer.parseInt(parseData[4]);
        if (parseData[0].equals("0")) { //DC id=0
            DistributionCenter.coordinate =new Coordinate(x,y);
            DistributionCenter.timeWindow =new TimeWindow(beginTime,endTime);
        }
        else {
            int loadTime=Integer.parseInt(parseData[5]);
            int unloadTime=Integer.parseInt(parseData[6]);
            Orders.add(new Order(new Coordinate(x,y),new TimeWindow(beginTime,endTime),loadTime,unloadTime));
        }
    }
    public static double distanceBetween(Order order1, Order order2) {
        return distanceBetween(order1.coordinate,order2.coordinate);
    }
    public static double distanceBetween(Coordinate coord1, Coordinate coord2) {
        return Math.sqrt(Math.pow(coord1.x-coord2.x,2)+Math.pow(coord1.y-coord2.y,2));//Based on Pythagorean theorem
    }
}
