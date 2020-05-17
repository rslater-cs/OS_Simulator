package Shell.subsystemstats;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class SubSystemGraph {
    private ArrayList<GraphData> data;
    private long initTime = System.currentTimeMillis();
    private int maxSize = 100;

    public SubSystemGraph(ArrayList<GraphData> data){
        this.data = data;
    }

    public LineChart<Number, Number> render(String title){
        averageData();
        ArrayList<XYChart.Data<Number, Number>> chartData = getAxis();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (s)");
        yAxis.setLabel("Percentage Fill (%)");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().addAll(chartData);
        lineChart.getData().add(series);
        return lineChart;
    }

    private void averageData(){
        while(data.size() > maxSize){
            data.remove(0);
        }
        int x = 0;
        int numberOfItems = 1;
        while(x < data.size()-1){
            if(data.get(x).getTime() == data.get(x+1).getTime()){
                data.get(x).setData(data.get(x+1).getData() + data.get(x).getData());
                data.remove(x+1);
                numberOfItems++;
            }else{
                data.get(x).setData(data.get(x).getData() / numberOfItems);
                numberOfItems = 1;
                x++;
            }
        }
    }

    private ArrayList<XYChart.Data<Number, Number>> getAxis(){
        ArrayList<XYChart.Data<Number, Number>> axis = new ArrayList<>();
        for(GraphData graphData : data){
            axis.add(new XYChart.Data<>(graphData.getTime(), graphData.getData()));
        }
        return axis;
    }
}
