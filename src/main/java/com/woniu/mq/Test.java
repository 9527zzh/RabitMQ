package com.woniu.mq;

	import com.rabbitmq.client.Channel;
	import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

	public class Test {
		private final static String QUEUE_NAME = "ni hao";
		
		public static void main(String[] args) throws Exception {
			// 获取到连接以及mq通道
			Connection connection = ConnectionUtils.getConnection();
			// 从连接中创建通道
			Channel channel = connection.createChannel();
			// 接下来，我们创建一个channel，绝大部分API方法需要通过调用它来完成。
			// 发送之前，我们必须声明消息要发往哪个队列，然后我们可以向队列发一条消息：
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			 // 消息内容
			String message = "ni hao aaa";
			//路由，队列名称，文本式的消息，字节数组
			channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			//关闭通道和连接
			channel.close();
			connection.close();
		}
	}
