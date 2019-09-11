package com.woniu.mq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;

public class RecvMq {
	 private final static String QUEUE_NAME = "ni hao";

	    public static void main(String[] argv) throws Exception {

	        // 获取到连接以及mq通道
	        Connection connection = ConnectionUtils.getConnection();
	        // 从连接中创建通道
	        Channel channel = connection.createChannel();
	        // 声明队列
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

	        // 定义队列的消费者
	        //该类是从amqp-client包中获取的
	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	        Consumer consumer = new DefaultConsumer(channel) {
	    	    @Override
	    	    //消息的标签头，级别，属性配置文件，消息主体
	    	    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
	    		    byte[] body) throws IOException {
	    		String message = new String(body, "UTF-8");
	    		System.out.println(" [x] Received '" + message + "'");
	    	    }
	    	};
	    	channel.basicConsume(QUEUE_NAME, true, consumer);
	       
	    }
	
}
