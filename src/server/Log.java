package server;

public class Log {
	
	private String personal_number;
	private int record_id;
	private ActionType action_type;
	private String action;
	private String timestamp;
	
	public Log(String personal_number, int record_id, ActionType action_type, String action) {
		this.personal_number = personal_number;
		this.record_id = record_id;
		this.action_type = action_type;
		this.action = action;
		this.timestamp = null;
	}
	
	public Log(String personal_number, int record_id, ActionType action_type, String action, String timestamp) {
		this.personal_number = personal_number;
		this.record_id = record_id;
		this.action_type = action_type;
		this.action = action;
		this.timestamp = timestamp;
	}
	
	public String getPersonalNumber() {
		return personal_number;
	}
	
	public int getRecordId() {
		return record_id;
	}
	
	public ActionType getActionType() {
		return action_type;
	}
	
	public String getAction() {
		return action;
	}

}
