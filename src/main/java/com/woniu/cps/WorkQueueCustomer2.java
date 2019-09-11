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

		// �õ�����
		Connection conn = ConnectionUtil.getConnection();

		// ����Ƶ��
		Channel channel = conn.createChannel();
		
		//����������Exchange����Ϊfanout
		channel.exchangeDeclare(EXECHANGENAME, "fanout");

		// ������Ϣ����
		boolean durable = true;
		channel.queueDeclare(queueName, durable, false, false, null);
		
		//�󶨶��е�������
		channel.queueBind(queueName, EXECHANGENAME, "");
		//ע��binding queue��ʱ��channel.queueBind()�ĵ���������Routing keyΪ�գ�
		//�����е���Ϣ�����ա�������ֵ��Ϊ�գ���exchange typeΪ��fanout����ʽ�¸�ֵ�����ԣ�
		
		
		//ͬһʱ�̷�����ֻ����1����Ϣ�������ߣ����߶��ͣ�������Ϣ��ģ������Ѹ������Ϣ��
		//��֤�ڽ��ն�һ����Ϣû�д�����ʱ���������һ����Ϣ���������߶˷�����ack��Ż������һ����Ϣ��
		//����������������߶˻᳢�԰���Ϣ���͸���һ�����е������ߡ�
		channel.basicQos(1);
		
		//����������
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body,"UTF-8");
				System.out.println("����======================2��Ϣ "+message);
				//�ֶ�������Ϣ
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		//����������߼������� autoAck��true�Զ����ؽ����false�ֶ�����
		boolean autoAck = false;
		channel.basicConsume(queueName,autoAck,consumer);
		//ע�⣬�˴���Ҫ����Ƶ��������
	}
}