package main.java.example.dto;

public class Sensor {
    public int id;
    public long timestamp;
    public int data; //number for the sake of an example


    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getData() {
        return data;
    }

    public Sensor() {
    }

    public Sensor(int id, long timestamp, int data) {
        this.id = id;
        this.timestamp = timestamp;
        this.data = data;
    }
}
