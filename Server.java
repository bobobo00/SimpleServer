package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * 
 * 
 *
 */
public class Server {
	private ServerSocket serverSocket ;
	private boolean isRunning;
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
		
	}
	//启动服务
	public void start() {
		try {
			serverSocket =  new ServerSocket(8888);
			this.isRunning=true;
			receive();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("服务器错误....");
			stop();
		}
	}
	//接受连接处理
	public void receive() {
		while(isRunning) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("第一个客户端建立连接");
				//多线程处理
				new Thread(new Dispatcher(client)).start();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("客户端错误");
			}
		}
	}
	//停止服务
	public void stop() {
		this.isRunning=false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
