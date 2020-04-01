package cn.tedu.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {

	public static void main(String[] args) throws IOException {

		// 开启服务器端的通道
		ServerSocketChannel ssc = ServerSocketChannel.open();

		// 绑定要监听端口
		ssc.bind(new InetSocketAddress(8090));

		// 设置为非阻塞
		ssc.configureBlocking(false);

		// 接收连接
		SocketChannel sc = ssc.accept();

		// 判断是否接收到了连接
		while (sc == null)
			sc = ssc.accept();

		// 读取数据
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		sc.read(buffer);
		buffer.flip();
		// 获取字节数组
		byte[] data = buffer.array();
		System.out.println(new String(data, 0, buffer.limit()));
		// while(buffer.hasRemaining())
		// System.out.println(buffer.get());

		// 关流
		ssc.close();
	}

}
