package practice2;

public class Worker extends Thread{

	private int workState;
	private Data data;
	
	public Worker(int id, Data d){
		this.workState = id;
		data = d;
		this.start();
	}
	
	@Override
	public void run(){
		try {
			for (int i = 0; i < 5; i++) {
				synchronized (data) {
					while (true) {
						if (workState != data.getState()) {
							data.notifyAll();
							data.wait();
						} else {
							break;
						}
					}
					if (workState == 1) {
						data.Tic();
					} else {
						data.Tak();
					}
					data.notifyAll();
				}
			}
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}
	
}
