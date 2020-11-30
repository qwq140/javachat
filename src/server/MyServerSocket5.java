package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import protocol.Chat;

public class MyServerSocket5 {

	private ServerSocket serverSocket;
	Vector<SocketThread> vc; // 대기열(큐)

	public MyServerSocket5() {
		try {
			serverSocket = new ServerSocket(10000);
			vc = new Vector<>();

			while (true) {
				System.out.println("요청 대기중 ...");
				Socket socket = serverSocket.accept(); // socket에 상태를
				// 1. 새로운 소켓 생성 socket
				// 2. 새로운 스레드 생성
				// 3. 새로운 스레드한테 socket 변수 넘기기
				// 4. 새로운 스레드 자체를 vc에 담기
				System.out.println("요청 받음 ...");
				SocketThread st = new SocketThread(socket); // socket, id 상태를 들고있음
				st.start();
				vc.add(st); // 다른 상태를 저장하기 위해 socket을 안넣고 class를 넣음
			}
		} catch (Exception e) {
			System.out.println("서버 연결 오류");
			e.printStackTrace();
		}
	}

	class SocketThread extends Thread {

		private Socket socket;
		private String id;
		private BufferedReader reader;
		private PrintWriter writer;

		public SocketThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());

				String input = null;

				while ((input = reader.readLine()) != null) {
					// Routing (라우팅 하기)
					routing(input);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void routing(String input) {
			String gubun[] = input.split(":"); // ALL인지 MSG인지 구분


			if (id == null) {
				if(gubun[0].equals(Chat.ID)) {
					id = gubun[1];
					writer.println("당신의 아이디는 "+id+"입니다.");
					writer.flush();
				} else {
					writer.println("아이디를 먼저 입력하세요!");
					writer.flush();
					return;
				}
				
			}

			if (gubun[0].equals(Chat.ALL)) { // 0 : ALL, 1 : 내용
				for (int i = 0; i < vc.size(); i++) {
					if (vc.get(i) != this) { // id로 구분해도 됨
						vc.get(i).writer.println(id + "-->" + gubun[1]);
						vc.get(i).writer.flush();
					}
				}
			} else if (gubun[0].equals(Chat.MSG)) {
				String tempId = gubun[1];
				String tempMsg = gubun[2];
				for (int i = 0; i < vc.size(); i++) {
					if (vc.get(i).id.equals(tempId) && vc.get(i).id != null) { // id로 구분해도 됨
						vc.get(i).writer.println(id + "-->" + tempMsg);
						vc.get(i).writer.flush();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new MyServerSocket5();
	}
}
