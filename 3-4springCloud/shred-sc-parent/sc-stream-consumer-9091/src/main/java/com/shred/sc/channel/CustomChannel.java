package com.shred.sc.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannel {
    String INPUT_LOG="inputLog";
    String OUTPUT_LOG="outputLog";

    @Input(INPUT_LOG)
    SubscribableChannel inputLog();

    @Output(OUTPUT_LOG)
    MessageChannel outputLog();
}
