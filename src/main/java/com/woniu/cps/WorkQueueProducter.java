package com.woniu.cps;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Queue;

public class WorkQueueProducter {
	public static void main(String[] args) throws IOException, TimeoutException {
		//不需要声明队列，交换机的级别比队列要搞的多
		final String EXECHANGENAME = "publicsubscrible";
		
		//得到连接
		Connection conn =  ConnectionUtil.getConnection();
		
		//创建频道
		Channel channel = conn.createChannel();
		
		//声明交换机Exchange交换方式为fanout
		//fanout 意味着消息要发给所有的消费者
		channel.exchangeDeclare(EXECHANGENAME, "fanout");
		
		for (int i = 0; i < 100; i++) {
			String message = "i love laohan "+i;
			//生产者端发布消息到交换机，使用“fanout”方式发送，
			//即广播消息，不需要使用queue，发送端不需要关心谁接收。
			channel.basicPublish(EXECHANGENAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}
		System.out.println("发布者订阅模式发布消息成功");
		
		channel.close();
		conn.close();
	}
}
