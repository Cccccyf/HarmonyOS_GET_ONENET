package com.example.fuckyou.Bean;

import java.util.List;

public class Data {
    private int count;
    private List<DataStream.Datastreams> datastreams;
    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }

    public void setDatastreams(List<DataStream.Datastreams> datastreams) {
        this.datastreams = datastreams;
    }
    public List<DataStream.Datastreams> getDatastreams() {
        return datastreams;
    }
}
