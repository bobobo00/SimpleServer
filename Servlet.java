package server;
/**
 * 服务器小脚本接口
 * 
 * @author 
 *
 */
public interface Servlet {
	public void service(Request request,Response response);
}
