package com.liu;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author Liush
 * @description
 * @date 2020/8/5 11:22
 **/
public class ProductOnce {




    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.159.128:9092");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //去幂等
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<>("test", "name", "liu10");
        ProducerRecord<String, String> record1 = new ProducerRecord<>("test", "name", "liu10");
        ProducerRecord<String, String> record2 = new ProducerRecord<>("test", "name", "liu11");
        try {
            RecordMetadata metadata = producer.send(record).get();
            producer.send(record1);
            producer.send(record2);
            System.out.println(metadata);
        } catch (Exception e) {
            e.printStackTrace();


        }



    	}




}
