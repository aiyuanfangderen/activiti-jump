package demo.entity;

import java.security.cert.CertPathValidatorException.Reason;
import java.util.Date;

public class User
{
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	private int id;
    private String title;
    private String processinstance;
    public String getProcessinstance() {
		return processinstance;
	}
	public void setProcessinstance(String processinstance) {
		this.processinstance = processinstance;
	}
	private String reason;
    private String userid;
    public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	private String state;
    
    
    
    
    
}
