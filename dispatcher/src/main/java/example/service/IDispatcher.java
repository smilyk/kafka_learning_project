package main.java.example.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;

public interface IDispatcher extends Sink {
    @Output
    MessageChannel bigNumbers();

    @Output
    MessageChannel smallNumbers();

    String BIG_NUMBERS = "bigNumbers";
    String SMALL_NUMBERS = "smallNumbers";
}
