package demo.dto;

public class ForSelect
{
	
	//执行对象id
	private String EXECUTION_ID_;
	

	public String getEXECUTION_ID_() {
		return EXECUTION_ID_;
	}


	public void setEXECUTION_ID_(String eXECUTION_ID_) {
		EXECUTION_ID_ = eXECUTION_ID_;
	}


	public String getNAME_() {
		return NAME_;
	}


	public void setNAME_(String nAME_) {
		NAME_ = nAME_;
	}


	public String getID_() {
		return ID_;
	}


	public void setID_(String iD_) {
		ID_ = iD_;
	}


	public String getTASK_DEF_KEY_() {
		return TASK_DEF_KEY_;
	}


	public void setTASK_DEF_KEY_(String tASK_DEF_KEY_) {
		TASK_DEF_KEY_ = tASK_DEF_KEY_;
	}


//对应历史任务表的NAME_
   private String NAME_;

	//上一个任务的id
  private String ID_;
    
    
    //对应历史任务表的TASK_DEF_KEY_
	private String TASK_DEF_KEY_;
	
	//任务执行人
	private String ASSIGNEE_;


	public String getASSIGNEE_() {
		return ASSIGNEE_;
	}


	public void setASSIGNEE_(String aSSIGNEE_) {
		ASSIGNEE_ = aSSIGNEE_;
	}
	
}
