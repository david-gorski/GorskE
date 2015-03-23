package gorskE;

public class Main {

	static GorskE engine;

	public static void main(String[] args) {
		engine = new GorskE();
		engine.run();
	}
	
	public static GorskE getEngine(){
		return engine;
	}

}
