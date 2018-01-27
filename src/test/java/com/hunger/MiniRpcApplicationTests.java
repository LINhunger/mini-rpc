package com.hunger;

import com.hunger.rpc.serialize.protostuff.ProtostuffCodecUtil;
import com.hunger.service.CalculateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.logging.Level;
import java.util.logging.Logger;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MiniRpcApplicationTests {


	@Test
	public void contextLoads() {
		//并行度10000
//		int parallel = 1;
//		CalculateService calc1 = (CalculateService) context.getBean("addCalc");
//		CalculateService calc2 = (CalculateService) context.getBean("multiCalc");
//		AddCalcParallelRequestThread client1 = new AddCalcParallelRequestThread(calc1,3);
//		new Thread(client1).start();
//		MultiCalcParallelRequestThread client2 = new MultiCalcParallelRequestThread(calc2, 3);
//		new Thread(client2).start();
//
//		Thread.sleep(10000);
//		context.close();
//		Thread.sleep(10000);
	}



}
class MultiCalcParallelRequestThread implements Runnable {

	private int taskNumber = 0;
	private CalculateService calc;

	public MultiCalcParallelRequestThread(CalculateService calc, int taskNumber) {
		this.taskNumber = taskNumber;
		this.calc = calc;
	}

	public void run() {
		try {
			int multi = calc.multi(taskNumber, taskNumber);
			System.out.println("calc multi result:[" + multi + "]");
		} catch (Exception ex) {
			Logger.getLogger(MultiCalcParallelRequestThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
class AddCalcParallelRequestThread implements Runnable {


	private int taskNumber = 0;
	private CalculateService calc;

	public AddCalcParallelRequestThread(CalculateService calc, int taskNumber) {

		this.taskNumber = taskNumber;
		this.calc = calc;
	}

	public void run() {
		try {
			int add = calc.add(taskNumber, taskNumber);
			System.out.println("calc add result:[" + add + "]");
		} catch (Exception ex) {
			Logger.getLogger(AddCalcParallelRequestThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}