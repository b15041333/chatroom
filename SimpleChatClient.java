package ChatRoom;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class SimpleChatClient {
	JTextArea incoming;// ����
	JTextField outgoing;// ����
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
		// ����setUpNetworking
		setUpNetworking();
		
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		// ע�ᰴť�ļ�����
		sendButton.addActionListener(new SendButtonListener());
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.setSize(400,500);
		frame.setVisible(true);
	}
	
	public void setUpNetworking(){
		// ����Socket��PrintWriter
		try{
			sock = new Socket("127.0.0.1",5000);
			// InputStreamReader�ǽ��ֽ�ת��Ϊ�ַ�������
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			// BufferedReader���ڻ����ȡ
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established.");
		}catch(IOException ex){
			ex.printStackTrace();
		}
		// ��ֵPrintWriter��ʵ������
	}
	
	public class SendButtonListener implements ActionListener {
		// �ڲ���
		@Override
		public void actionPerformed(ActionEvent ev) {
			try{
				writer.println(outgoing.getText());// println()����Ϣ�͵�������
				writer.flush();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}// �ر�SendButtonListener�ڲ���
	
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
			}// �ر�run()
		}// �ر��ڲ���
	}
	
	public static void main(String[] args){
		SimpleChatClient client = new SimpleChatClient();
		client.go();
	}
}
