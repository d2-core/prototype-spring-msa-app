package com.d2.notification.adapter.`in`.kafka

import lombok.extern.slf4j.Slf4j
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Slf4j
@Component
class NotificationConsumer {

    @KafkaListener(topics = ["test"], groupId = "notification")
    fun test(record: ConsumerRecord<String, String>) {
        
    }
}
