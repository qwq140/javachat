package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.google.gson.Gson;


import protocol.RequestDto;
public class MyClientSocket5 {

	private Socket socket;
	private BufferedReader reader;
	
	private Scanner sc;
	private PrintWriter writer;
	
	public MyClientSocket5() {
		try {
			socket = new Socket("localhost",10000);
			
			SocketThread st = new SocketThread();
			st.start();
			
			sc = new Scanner(System.in);
			writer = new PrintWriter(socket.getOutputStream());
			
			while(true) {
				// ALL: ¾È³ç, MSG:ssar1:¾È³ç
				String keyboard = sc.nextLine();
				//jsonÀ¸·Î ÆÄ½Ì
				RequestDto dto = new RequestDto();
				String gubun[] = keyboard.split(":");
				if(gubun.length == 2) {
					dto.setGubun(gubun[0]);
					dto.setMsg(gubun[1]);
				}else if(gubun.length == 3) {
					dto.setGubun(gubun[0]);
					dto.setId(gubun[1]);
					dto.setMsg(gubun[2]);
				}
				Gson gson = new Gson();
				String jsonData = gson.toJson(dto);
			
				writer.println(jsonData);
				writer.flush();
			}
		} catch (Exception e) {
			System.out.println("¼­¹ö ¿¬°á ½ÇÆÐ");
			e.printStackTrace();
		}
	}
	
	class SocketThread extends Thread{
		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String input = null;
				while((input=reader.readLine())!=null) {
					System.out.println(input);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new MyClientSocket5();

	}

}
