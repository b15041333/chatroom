package ChatRoom;

import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {
	ArrayList clientOutputStreams;
	
	public class ClientHandler implements Runnable{
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket){
			try{
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		
		public void run(){
			String message;
			try{
				while((message = reader.readLine()) != null){
					System.out.println("read:"+message);
					tellEveryone(message);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}// 关闭内部类
	
	public void go(){
		clientOutputStreams = new ArrayList();
		try{
			ServerSocket serverSock = new ServerSocket(5000);
			while(true){
				Socket clientSocket = serverSock.accept();// 当有一个客户端连接上来时，获取其socket
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket));// 为每一个用户的处理程序创建一个线程
				t.start();
				System.out.println("got a connection.");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void tellEveryone(String message){
		Iterator it = clientOutputStreams.iterator();
		while(it.hasNext()){
			try{
				PrintWriter writer = (PrintWriter)it.next();
				writer.println(message);
				writer.flush();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		new VerySimpleChatServer().go();
	}
}
