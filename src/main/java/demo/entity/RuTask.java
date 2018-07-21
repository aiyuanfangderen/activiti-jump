package demo.entity;

import java.sql.Timestamp;

import org.joda.time.DateTime;

public class RuTask 
{
   public String getID_() {
		return ID_;
	}
	public void setID_(String iD_) {
		ID_ = iD_;
	}
	public String getNAME_() {
		return NAME_;
	}
	public void setNAME_(String nAME_) {
		NAME_ = nAME_;
	}
	public String getTASK_DEF_KEY_() {
		return TASK_DEF_KEY_;
	}
	public void setTASK_DEF_KEY_(String tASK_DEF_KEY_) {
		TASK_DEF_KEY_ = tASK_DEF_KEY_;
	}
	public String getASSIGNEE_() {
		return ASSIGNEE_;
	}
	public void setASSIGNEE_(String aSSIGNEE_) {
		ASSIGNEE_ = aSSIGNEE_;
	}
	
public Timestamp getCREATE_TIME_() {
		return CREATE_TIME_;
	}
	public void setCREATE_TIME_(Timestamp cREATE_TIME_) {
		CREATE_TIME_ = cREATE_TIME_;
	}

private String ID_;
   private String NAME_;
   private String TASK_DEF_KEY_;
   private String ASSIGNEE_;
   private Timestamp CREATE_TIME_;
   public String getBeforeId() {
	return beforeId;
}
public void setBeforeId(String beforeId) {
	this.beforeId = beforeId;
}

private String beforeId;
   
}
