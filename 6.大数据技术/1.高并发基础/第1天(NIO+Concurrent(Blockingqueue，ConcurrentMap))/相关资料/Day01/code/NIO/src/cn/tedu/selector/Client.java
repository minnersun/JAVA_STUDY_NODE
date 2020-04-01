package cn.tedu.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

	public static void main(String[] args) throws IOException {

		SocketChannel sc = SocketChannel.open();
		sc.connect(new InetSocketAddress("localhost", 8090));
		sc.write(ByteBuffer.wrap("hello server".getBytes()));
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		sc.read(buffer);
		System.out.println(new String(buffer.array(), 0, buffer.position()));
		sc.close();
	}

}
