package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

//양방향 통신
public class MyServerSocket2 {

	private ServerSocket serverSocket;
	private Socket socket;
	private Scanner sc;
	private BufferedWriter writer;
	private BufferedReader reader;
	
	public MyServerSocket2() {
		try {
			serverSocket = new ServerSocket(10000);
			System.out.println("클라이언트 대기 중....");
			socket = serverSocket.accept(); // 데몬(리스닝)
			System.out.println("요청이 성공함");

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 방향 결정
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			sc = new Scanner(System.in);
			
			String input = null;
			
			Thread t1 = new Thread(new WriteThread());
			t1.start();
			
			while ((input = reader.readLine()) != null) {
				System.out.println("클라이언트 메시지 : " + input);
			}

			reader.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 키보드로부터 입력받는 스레드 , socket 변수에 접근하기 위해 내부클래스로 만듬
	class WriteThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				String input = sc.nextLine();
				try {
					writer.write(input + "\n");
					writer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new MyServerSocket2();
	}
}
