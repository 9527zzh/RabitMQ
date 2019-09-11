package com.woniu.cps;

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
		final String EXECHANGENAME = "publicsubscrible";
		final String queueName = "publicsubscrible_queue2";

		// 得到连接
		Connection conn = ConnectionUtil.getConnection();

		// 创建频道
		Channel channel = conn.createChannel();
		
		//声明交换机Exchange类型为fanout
		channel.exchangeDeclare(EXECHANGENAME, "fanout");

		// 声明消息队列
		boolean durable = true;
		channel.queueDeclare(queueName, durable, false, false, null);
		
		//绑定队列到交换机
		channel.queueBind(queueName, EXECHANGENAME, "");
		//注意binding queue的时候，channel.queueBind()的第三个参数Routing key为空，
		//即所有的消息都接收。如果这个值不为空，在exchange type为“fanout”方式下该值被忽略！
		
		
		//同一时刻服务器只发送1条消息给消费者（能者多劳，消费消息快的，会消费更多的消息）
		//保证在接收端一个消息没有处理完时不会接收另一个消息，即消费者端发送了ack后才会接收下一个消息。
		//在这种情况下生产者端会尝试把消息发送给下一个空闲的消费者。
		channel.basicQos(1);
		
		//申明消费者
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body,"UTF-8");
				System.out.println("消费======================2消息 "+message);
				//手动返回消息
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		//定义的消费者监听队列 autoAck：true自动返回结果，false手动返回
		boolean autoAck = false;
		channel.basicConsume(queueName,autoAck,consumer);
		//注意，此处不要管理频道和连接
	}
}