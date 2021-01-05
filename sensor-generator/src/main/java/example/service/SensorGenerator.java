package main.java.example.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.example.dto.Sensor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;

@EnableBinding(Source.class)
public class SensorGenerator {

    ObjectMapper mapper = new ObjectMapper();
    @Value("${min_id:1}")
    private int minId;
    @Value("${max_id:10}")
    private int maxId;

    @Value("${min_data:100}")
    private int minData;
    @Value("${min_data:200}")
    private int maxData;


    @InboundChannelAdapter(Source.OUTPUT)
    String sendSensorData() throws JsonProcessingException {
        String sensorData = null;
        int id = getRandomId();
        long timestamp = System.currentTimeMillis();
        int data = getRandomData();
        Sensor sensor = new Sensor(id, timestamp, data);
        sensorData = mapper.writeValueAsString(sensor);
        return sensorData;
    }

    private int getRandomData() {
        return getRandomNumber(minData, maxData);
    }

    private int getRandomId() {
        return getRandomNumber(minId, maxId);
    }

    private int getRandomNumber(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }
}
