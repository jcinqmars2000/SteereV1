package com.steereengineering.utils;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface EmailChannel {
    @Input
    SubscribableChannel emailInput();
}
