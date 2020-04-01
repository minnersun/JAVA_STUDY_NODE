package cn.tedu.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

	public static void main(String[] args) throws IOException {

		// 开启一个客户端通道
		SocketChannel sc = SocketChannel.open();

		// 可以将这个通道手动设置为非阻塞
		sc.configureBlocking(false);

		// 发起连接
		sc.connect(new InetSocketAddress("localhost", 8090));

		// 因为设置为了非阻塞，所以需要保证连接建立之后才能写出数据
		// 先去判断连接是否建立
		while (!sc.isConnected())
			// 在这个方法中自动进行计数，如果连接多次未连接上
			// 那么认为这个连接无法建立
			// 此时会抛出异常
			sc.finishConnect();

		// 写出数据
		sc.write(ByteBuffer.wrap("hello server".getBytes()));

		// 关流
		sc.close();
	}

}
