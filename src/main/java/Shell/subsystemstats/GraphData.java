package Shell.subsystemstats;

public class GraphData {
    private Double data;
    private long time;


    public GraphData(Double data, long time){
        this.data = data;
        this.time = time;
    }

    public Double getData(){
        return data;
    }

    public void setData(Double data){
        this.data = data;
    }

    public long getTime(){
        return time;
    }
}
