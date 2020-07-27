package xdclass_websocket.controller.v6;

/**
 * 
 * 功能描述：用户模型
 *
 * <p> 创建时间：Jan 6, 2018 </p> 
 * <p> 贡献者：小D学院, 官网：www.xdclass.net </p>
 *
 * @author <a href="mailto:xd@xdclass.net">小D老师</a>
 * @since 0.0.1
 */
public class User {

	private String username;
	private String pwd;
	
	
	
	public User() {}
	
	public User(String username, String pwd) {
		super();
		this.username = username;
		this.pwd = pwd;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
}
