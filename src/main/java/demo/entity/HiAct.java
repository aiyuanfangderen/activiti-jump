package demo.entity;
import java.sql.Timestamp;
import java.math.BigInteger;

import org.joda.time.DateTime;

public class HiAct
{
	private String ID_;
    public String getID_() {
		return ID_;
	}
	public void setID_(String iD_) {
		ID_ = iD_;
	}
	public BigInteger getDURATION_() {
		return DURATION_;
	}
	public void setDURATION_(BigInteger dURATION_) {
		DURATION_ = dURATION_;
	}
	
	private BigInteger DURATION_;
    public Timestamp getEND_TIME_() {
		return END_TIME_;
	}
	public void setEND_TIME_(Timestamp eND_TIME_) {
		END_TIME_ = eND_TIME_;
	}

	private Timestamp END_TIME_;
}
