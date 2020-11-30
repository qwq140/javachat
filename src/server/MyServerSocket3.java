package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MyServerSocket3 {

	private ServerSocket serverSocket;
	private Vector<SocketThread> vc;
	
	public MyServerSocket3() {
		try {
			// 서버 소켓 생성 65536번 중에 0~1023(well known port)를 제외한 모든 포트
			serverSocket = new ServerSocket(20000);
			vc = new Vector<>();
			
			//메인쓰레드는 소켓을 accept()하고 vector에 담는 역할을 함.
			while(true) {
				System.err.println("요청 대기");
				Socket socket = serverSocket.accept(); // 클라이언트 요청을 받음
				System.out.println("요청 받음");
				SocketThread st = new SocketThread(socket);
				st.start();
				vc.add(st);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	//소켓정보 + 타켓(run) + 식별자(id)
	class SocketThread extends Thread{
		Socket socket;
		String id;
		BufferedReader reader;
		PrintWriter writer;
		
		public SocketThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(),true);
				String line = null;
				while ((line = reader.readLine() )!= null) {
					System.out.println("from client : "+line);
					for (SocketThread socketThread : vc) {
						socketThread.writer.println(line);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}

	public static void main(String[] args) {
		new MyServerSocket3();
	}
}
