package main.java.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.example.dto.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@EnableBinding(IDispatcher.class)
public class SensorDispatcher {
    @Autowired
    IDispatcher dispatcher;
    ObjectMapper mapper = new ObjectMapper();
    @Value("${min:120}")
    int min;
    @Value("${max:180}")
    int max;
    @Value("${period:30}")
    int period; // period in seconds for computing the average values
    @ManagedAttribute
    public int getPeriod() {
        return period;
    }
    @ManagedAttribute
    public void setPeriod(int period) {
        this.period = period;
    }

    ArrayList<Sensor> sensorsForPeriod = new ArrayList<>();
    Instant current = null;

    @StreamListener(IDispatcher.INPUT)
    public void getSensorData(String sensorJson) throws  JsonProcessingException {
        Sensor sensor = mapper.readValue(sensorJson,  Sensor.class);
        if(sensor.data < min) {
            dispatcher.smallNumbers()
                    .send(MessageBuilder.withPayload(sensorJson).build());
			return;
        }
        else if(sensor.data > max) {
            dispatcher.bigNumbers()
                    .send(MessageBuilder.withPayload(sensorJson).build());
			return;
        }

//		System.out.printf("delay:%d, id:%d, data:%d\n",
//				System.currentTimeMillis() - sensor.timestamp,
//				sensor.id, sensor.data
//				);

        if(current == null)
            current = Instant.now();
        else {
            long actualPeriod = ChronoUnit.SECONDS.between(current, Instant.now());
            if(actualPeriod > period) {
                avgComputing();
                sensorsForPeriod.clear();
                current = null;
            }
        }
        sensorsForPeriod.add(sensor);
    }
    private void avgComputing() {
        Map<Integer, Double> mapAvgValues = sensorsForPeriod.parallelStream()
                .collect(Collectors.groupingBy(Sensor::getId, TreeMap::new, Collectors.averagingInt(Sensor::getData)));
        mapAvgValues.forEach((k,v)->System.out.printf("sensor id:%d, avg value:%f, %s\n", k, v, getDateTime()));
    }
    private String getDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy:hh.mm.ss");
        return LocalDateTime.now().format(dtf);
    }

}
