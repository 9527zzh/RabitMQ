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
		//����Ҫ�������У��������ļ���ȶ���Ҫ��Ķ�
		final String EXECHANGENAME = "publicsubscrible";
		
		//�õ�����
		Connection conn =  ConnectionUtil.getConnection();
		
		//����Ƶ��
		Channel channel = conn.createChannel();
		
		//����������Exchange������ʽΪfanout
		//fanout ��ζ����ϢҪ�������е�������
		channel.exchangeDeclare(EXECHANGENAME, "fanout");
		
		for (int i = 0; i < 100; i++) {
			String message = "i love laohan "+i;
			//�����߶˷�����Ϣ����������ʹ�á�fanout����ʽ���ͣ�
			//���㲥��Ϣ������Ҫʹ��queue�����Ͷ˲���Ҫ����˭���ա�
			channel.basicPublish(EXECHANGENAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}
		System.out.println("�����߶���ģʽ������Ϣ�ɹ�");
		
		channel.close();
		conn.close();
	}
}
