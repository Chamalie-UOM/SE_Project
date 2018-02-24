package authentication;

public class User {
	private String username;
	private String email;
	private String tpnum;
	private String recovPass;
	Password pass;
	
	public void setName(String name) {
		this.username = name;
	}
	public String getName() {
		return username;
	}
	public void setMail(String mail) {
		this.email = mail;
	}
	public String getMail() {
		return email;
	}
	public void setTpnum(String tpnum) {
		this.tpnum = tpnum;
	}
	public String getTpnum() {
		return tpnum;
	}
	public void setRecovPass(String recP) {
		this.recovPass = recP;
	}
	public String getRecovPass() {
		return recovPass;
	}
}
