package cn.com.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {
	/**
	 * 
	 * TODO 方法的描述：。 
	 * <pre>
	 * 启动所有的线程
	 * </pre>
	 */
	public void startAll(){
		 ExecutorService threadPool = Executors.newCachedThreadPool();
		 CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
		 cs.submit(new Callable<Integer>() {
                public Integer call() throws Exception {
//                    new DbTask().executeReqTask("6");
                    return 0;
                }
         });
		 cs.submit(new Callable<Integer>() {
             public Integer call() throws Exception {
//                 new DbTask().executeNRTTask();
                 return 0;
             }
      });
	}
	/**
	 * 
	 * TODO 方法的描述：。 
	 * <pre>
	 * 启动所有的线程 区分优先级
	 * </pre>
	 */
	public void startAllByPriority(){
		 ExecutorService threadPool = Executors.newCachedThreadPool();
		 CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
//		 cs.submit(new ReqTaskThread(6), 6);
//		 cs.submit(new NRTTaskThread(5), 5);
	}
}
