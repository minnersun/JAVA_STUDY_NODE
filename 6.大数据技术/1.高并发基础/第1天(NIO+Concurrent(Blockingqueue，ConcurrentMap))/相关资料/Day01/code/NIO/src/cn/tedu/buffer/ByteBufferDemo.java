package cn.tedu.buffer;

import java.nio.ByteBuffer;

public class ByteBufferDemo {

	public static void main(String[] args) {

		// 创建ByteBuffer对象
		// ByteBuffer底层是基于了字节数组来存储
		// 所以在使用的时候需要指定字节数组的容量
		ByteBuffer buffer = ByteBuffer.allocate(10);

		// 添加数据
		buffer.put("abc".getBytes());
		buffer.put("def".getBytes());

		// 需要将position挪到指定的位置上
		// buffer.position(0);

		// 获取数据
		// 表示获取一个字节
		// System.out.println(buffer.get());
		// System.out.println(buffer.get());
		// 如果需要进行缓冲区的遍历
		// 需要先将limit挪到position上
		// 然后将position归零
		// 翻转缓冲区
		// buffer.limit(buffer.position());
		// buffer.position(0);
		// 等价于上述两步操作
		buffer.flip();
		// 如果position和limit重合，则说明所有的数据已经遍历完毕
		// while (buffer.position() < buffer.limit()) {
		while (buffer.hasRemaining()) {
			System.out.println(buffer.get());
		}

	}

}
