package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
/**
 * 分发器：加入状态内容处理  404 505 及首页
 * 
 * @author 
 *
 */
public class Dispatcher implements Runnable {
	private Socket client;
	private Request request;
	private Response response;
	public Dispatcher(Socket client) {
		super();
		this.client = client;
		try {
			//获取请求协议
			//获取响应协议
			this.request = new Request(client);
			this.response = new Response(client);
		} catch (IOException e) {
			release();
		}
	
	}


	public void run() {
		if(null==request.getUrl()||request.getUrl().equals("f")) {
			InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
			byte[] data=new byte[1024*1024];
			try {
				int len=is.read(data);
				response.println(new String(data,0,len));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.pushToBrowser(200);
			release();
			return;
		}
		//��ע������
		try {
			Servlet servlet=WebApp.getServletFromUrl(request.getUrl());
			if(null!=servlet) {
				servlet.service(request,response);
				response.pushToBrowser(200);
			}else {
				InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html");
				byte[] data=new byte[1024*1024];
				try {
					int len=is.read(data);
					response.println(new String(data,0,len));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.pushToBrowser(404);
			}
		}catch(Exception e){
			response.pushToBrowser(505);
		}
		release();
	}
	//释放资源
	private void release() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
