package main.java.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.example.dto.Sensor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.io.IOException;

@EnableBinding(Sink.class)
public class SensorReciever {
    ObjectMapper mapper = new ObjectMapper();
    @StreamListener(Sink.INPUT)
    public void receiveSensorData(String sensorData) throws  IOException  {
        Sensor sensor = mapper.readValue(sensorData, Sensor.class);
        System.out.printf("delay:%d, id:%d, data:%d\n",
                System.currentTimeMillis() - sensor.timestamp,
                sensor.id, sensor.data
        );
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

