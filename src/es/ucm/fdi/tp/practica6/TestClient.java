package es.ucm.fdi.tp.practica6;

public class TestClient {
	public static void main(String[] args) {
		String[] as = { "-am", "client", "-sh", "192.168.1.13", "-aialg", "minmaxab", "-md", "3"};
		Main.main(as);
	}
}
