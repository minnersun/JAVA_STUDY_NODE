package cn.tedu.queue;

import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueDemo {

	public static void main(String[] args) throws InterruptedException {

		// PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(7);
		//
		// queue.put("g");
		// queue.put("a");
		// queue.put("r");
		// queue.put("h");
		// queue.put("d");
		// queue.put("t");
		// queue.put("u");

		PriorityBlockingQueue<Student> queue = new PriorityBlockingQueue<>(5);

		queue.put(new Student("≤‹—Û", 80, 59));
		queue.put(new Student("¿ÓÀß", 30, 61));
		queue.put(new Student("√œœÈ±˘", 40, 19));
		queue.put(new Student("–§–ÒŒ∞", 40, 70));
		queue.put(new Student("π»∑·À∂", 18, 30));

		// for (int i = 0; i < 7; i++) {
		// System.out.println(queue.take());
		// }
		for (Student s : queue) {
			System.out.println(s);
		}
	}

}

class Student implements Comparable<Student> {

	private String name;
	private int age;
	private int score;

	public Student(String name, int age, int score) {
		this.name = name;
		this.age = age;
		this.score = score;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + ", score=" + score + "]";
	}

	// ∞¥’’ƒÍ¡‰Ω¯––…˝–Ú≈≈–Ú
	// this - o ===> …˝–Ú
	// o - this ===> Ωµ–Ú
	@Override
	public int compareTo(Student o) {
		return this.age - o.age;
	}

}
