package com.liu;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Liush
 * @description
 * @date 2020/8/5 11:22
 **/
public class ProductOnce {




    public static void main(String[] args) {

        CountDownLatch countDownLatch=new CountDownLatch(50000);
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.159.128:9092");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //去幂等
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ThreadPoolExecutor threadPoolExecutor=new MyThreadPool().createThreadPoolExecutor();
        AtomicInteger id=new AtomicInteger();
        threadPoolExecutor.execute(new MyTask(id,producer,countDownLatch));
        threadPoolExecutor.execute(new MyTask(id,producer,countDownLatch));
        threadPoolExecutor.execute(new MyTask(id,producer,countDownLatch));
        try {
            countDownLatch.await();
            System.out.println("执行结束"+id.get());
            System.out.println("countdown_____"+countDownLatch.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    	}

}


        class MyTask implements Runnable{


            private AtomicInteger id;

            private KafkaProducer<String, String> producer;

            private CountDownLatch countDownLatch;

            public MyTask(AtomicInteger id, KafkaProducer<String, String> producer, CountDownLatch countDownLatch) {
                this.id = id;
                this.producer = producer;
                this.countDownLatch = countDownLatch;
            }

            @Override
            public void run() {
                int num=id.incrementAndGet();
                System.out.println("任务开始..................");
                while (num<1000000){
                    num=id.incrementAndGet();
                    ProducerRecord<String, String> record = new ProducerRecord<>("test", "name", id.incrementAndGet()+"");
                    producer.send(record);
                    countDownLatch.countDown();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public String toString() {
                return "任务id是 "+id;
            }
        }



    class MyThreadPool{


        public ThreadPoolExecutor createThreadPoolExecutor(){

            BlockingDeque blockingDeque=new LinkedBlockingDeque(1);
            ThreadPoolExecutor pool=new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS,blockingDeque);
                pool.setRejectedExecutionHandler((r,executor)->{
                    System.out.println(r.toString());
                    System.out.println("队列已满,丢弃任务");

                });

            return pool;

    }








}
