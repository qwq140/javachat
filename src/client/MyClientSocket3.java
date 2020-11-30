package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket3 {
	
	private Socket socket;
	private Scanner sc;
	private BufferedWriter writer;
	private BufferedReader reader;
	
	public MyClientSocket3() {
		try {
			socket = new Socket("127.0.0.1", 20000);
			
			// 키보드로 부터 입력 받아서
			sc = new Scanner(System.in);
			//소켓에 버퍼달기(쓰기)
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String input=null;
					try {
						while((input = reader.readLine())!=null) {
							System.out.println("서버로부터 온 메시지 : "+input);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
			
			
			//메인쓰레드는 키보드로부터 입력을 받은 뒤 클라이언트 소켓에 전송
			while(true) {
				String input =sc.nextLine();//문자열 받기
				// 서버에 전송
				writer.write(input+"\n");
				writer.flush();
			}
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new MyClientSocket3();

	}

}
