package com.woniu.cps;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
	public static Connection getConnection() throws IOException, TimeoutException {
		// connection是socket连接的抽象，并且为我们管理协议版本协商（protocol version negotiation），
		// 认证（authentication ）等等事情�?�这里我们要连接的消息代理在本地，因此我们将host设为“localhost”�??
		// 如果我们想连接其他机器上的代理，只需要将这里改为特定的主机名或IP地址�?
		//定义连接工厂
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("localhost");//设置服务地址
		factory.setPort(5672);//定义端口。AMQP的端口，RabbitMq是AMQP协议的实�?
		//设置账号信息，用户名、密码�?�vhost
		factory.setVirtualHost("zzh");
		factory.setUsername("zhang");
		factory.setPassword("0000");
		// 创建connection�?  // 通过工程获取连接
		Connection connection=factory.newConnection();
		return connection;
		
	}
}
