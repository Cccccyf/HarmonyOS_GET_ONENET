package com.example.fuckyou.Bean;

import java.util.List;

public class DataStream {
    public class Datastreams {
        private List<Datapoints> datapoints;
        private String id;
        public void setDatapoints(List<Datapoints> datapoints) {
            this.datapoints = datapoints;
        }
        public List<Datapoints> getDatapoints() {
            return datapoints;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
    }
}
