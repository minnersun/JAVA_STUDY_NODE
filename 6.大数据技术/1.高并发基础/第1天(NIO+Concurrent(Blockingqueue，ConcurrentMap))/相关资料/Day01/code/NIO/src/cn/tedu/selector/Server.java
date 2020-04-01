package cn.tedu.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

	public static void main(String[] args) throws IOException {

		// 开启服务器端的通道
		ServerSocketChannel ssc = ServerSocketChannel.open();
		// 绑定要监听的端口
		ssc.bind(new InetSocketAddress(8090));
		// 设置为非阻塞
		ssc.configureBlocking(false);
		// 开启选择器
		Selector selc = Selector.open();
		// 将通道注册到选择器上
		ssc.register(selc, SelectionKey.OP_ACCEPT);

		while (true) {

			// 进行选择
			selc.select();

			// 获取这次选择出来的事件类型
			Set<SelectionKey> keys = selc.selectedKeys();
			// 获取迭代器遍历
			// 根据不同的事件类型来进行不同的处理
			Iterator<SelectionKey> it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				// 可接受
				if (key.isAcceptable()) {
					// 从这个事件中将通道取出来
					ServerSocketChannel sscx = (ServerSocketChannel) key.channel();
					// 接受连接
					SocketChannel sc = sscx.accept();
					System.out.println("连接成功~~~");
					// 注册可读以及可写事件
					sc.configureBlocking(false);
					sc.register(selc, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				}
				// 可读
				if (key.isReadable()) {
					// 从事件中获取到通道
					SocketChannel sc = (SocketChannel) key.channel();
					// 读取数据
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					sc.read(buffer);
					// 解析数据
					byte[] data = buffer.array();
					System.out.println(new String(data, 0, buffer.position()));
					// 注销掉read事件，目的是为了防止重复读取
					// 获取这个通道上的所有的事件
					// sc.register(selc, key.interestOps() -
					// SelectionKey.OP_READ);
					sc.register(selc, key.interestOps() ^ SelectionKey.OP_READ);
				}
				// 可写
				if (key.isWritable()) {
					// 从事件中获取通道
					SocketChannel sc = (SocketChannel) key.channel();
					// 写出数据
					sc.write(ByteBuffer.wrap("hello client".getBytes()));
					// 注销掉可写事件
					sc.register(selc, key.interestOps() - SelectionKey.OP_WRITE);
				}
				it.remove();
			}
		}
	}

}
