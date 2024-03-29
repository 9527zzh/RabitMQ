package com.woniu.work;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class WorkQueueCustomer2 {
	public static void main(String[] args) throws IOException, TimeoutException {
		String queueName = "work_queue";

		// 得到连接
		Connection conn = ConnectionUtil.getConnection();

		// 创建频道
		Channel channel = conn.createChannel();

		// Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean
		// exclusive, boolean autoDelete,
		// Map<String, Object> arguments) throws IOException;
		// 声明消息队列
		boolean durable = true;
		channel.queueDeclare(queueName, durable, false, false, null);
		
		
		//同一时刻服务器只发送1条消息给消费者（能者多劳，消费消息快的，会消费更多的消息）
		//保证在接收端一个消息没有处理完时不会接收另一个消息，即消费者端发送了ack后才会接收下一个消息。
		//在这种情况下生产者端会尝试把消息发送给下一个空闲的消费者。
		channel.basicQos(1);
	
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body,"UTF-8");
				System.out.println("消费======================2消息 "+message);
				//手动返回消息
				channel.basicAck(envelope.getDeliveryTag(), false);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		//定义的消费者监听队列 autoAck：true自动返回结果，false手动返回
		boolean autoAck = false;
		channel.basicConsume(queueName,autoAck,consumer);
		//注意，此处不要管理频道和连接
	}
}
