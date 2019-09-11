package com.woniu.work;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
// ������������������������£���Ϣ������������ͷƽ���֣�����һ�Ѱ������Լ���ȫ���ߣ���ѯƽ�֣�
public class WorkQueueCustomer {
	public static void main(String[] args) throws IOException, TimeoutException {
		String queueName = "work_queue";

		// �õ�����
		Connection conn = ConnectionUtil.getConnection();

		// ����Ƶ��
		Channel channel = conn.createChannel();

		// ������Ϣ����
		boolean durable = true;
		channel.queueDeclare(queueName, durable, false, false, null);
		
		
		//ͬһʱ�̷�����ֻ����1����Ϣ�������ߣ����߶��ͣ�������Ϣ��ģ������Ѹ������Ϣ��
		//��֤�ڽ��ն�һ����Ϣû�д�����ʱ���������һ����Ϣ���������߶˷�����ack��Ż������һ����Ϣ��
		//����������������߶˻᳢�԰���Ϣ���͸���һ�����е������ߡ�
		//Qos������ÿ��ֻ��ȡ��һ����Ϣ������ȫ������
		channel.basicQos(1);
	
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body,"UTF-8");
				System.out.println("����======================1��Ϣ "+message);
				//�ֶ�������Ϣ����Ϣȷ�ϣ����ֻ������Ϣ�������Ļ���Unackedδȷ��
				channel.basicAck(envelope.getDeliveryTag(), false);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		//ACK��Ϣȷ��
		//����������߼������� autoAck�� true�Զ����ؽ����false�ֶ�����
		//һ����ȡ���Զ����ؽ��������������Ϣ��ʧ 
		boolean autoAck = false;
		channel.basicConsume(queueName,autoAck,consumer);
		//ע�⣬�˴���Ҫ�ر�Ƶ��������
	}
}
