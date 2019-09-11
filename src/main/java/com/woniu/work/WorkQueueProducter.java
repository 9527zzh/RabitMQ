package com.woniu.work;

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
		String queueName = "work_queue";
		
		//�õ�����
		Connection conn =  ConnectionUtil.getConnection();
		
		//����Ƶ��
		Channel channel = conn.createChannel();
		
		//Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
				//Map<String, Object> arguments) throws IOException;
		//������Ϣ����
		boolean durable = true;//��Ϣ�Ƿ�־û�
		channel.queueDeclare(queueName, durable, false, false, null);
		
		//void basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;
		//������Ϣ
		
		for (int i = 0; i < 100; i++) {
			String message = "iloveyou "+i;
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}
		
		channel.close();
		conn.close();
	}
}
