package cn.tedu.queue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueDemo2 {

	public static void main(String[] args) throws InterruptedException {

		// ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

		// 队列为空
		// 抛出异常
		// System.out.println(queue.remove());
		// 返回null
		// System.out.println(queue.poll());
		// 产生阻塞
		// System.out.println(queue.take());
		// 定时阻塞
		System.out.println(queue.poll(5, TimeUnit.SECONDS));
	}

}
