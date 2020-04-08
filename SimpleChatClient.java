package ChatRoom;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class SimpleChatClient {
	JTextArea incoming;// 流入
	JTextField outgoing;// 流出
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public void go(){
		JFrame frame = new JFrame("Simple Chat Client");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15,50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		// 调用setUpNetworking
		setUpNetworking();
		
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		// 注册按钮的监听者
		sendButton.addActionListener(new SendButtonListener());
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.setSize(400,500);
		frame.setVisible(true);
	}
	
	public void setUpNetworking(){
		// 建立Socket、PrintWriter
		try{
			sock = new Socket("127.0.0.1",5000);
			// InputStreamReader是将字节转换为字符的桥梁
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			// BufferedReader用于缓冲读取
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established.");
		}catch(IOException ex){
			ex.printStackTrace();
		}
		// 赋值PrintWriter给实例变量
	}
	
	public class SendButtonListener implements ActionListener {
		// 内部类
		@Override
		public void actionPerformed(ActionEvent ev) {
			try{
				writer.println(outgoing.getText());// println()将信息送到服务器
				writer.flush();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}// 关闭SendButtonListener内部类
	
	public class IncomingReader implements Runnable{
		public void run(){
			String message;
			try{
				while((message = reader.readLine()) != null){
					System.out.println("read:"+message);
					incoming.append(message+"\n");
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}// 关闭run()
		}// 关闭内部类
	}
	
	public static void main(String[] args){
		SimpleChatClient client = new SimpleChatClient();
		client.go();
	}
}
