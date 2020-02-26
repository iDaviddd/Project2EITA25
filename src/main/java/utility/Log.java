package utility;

import java.sql.Timestamp;

public class Log {
	
	private String personal_number;
	private int record_id;
	private Integer log_id;
	private String action_type;
	private String action;
	private Timestamp timestamp;
	
	public Log(String personal_number, int record_id, String action_type, String action) {
		this.personal_number = personal_number;
		this.record_id = record_id;
		this.log_id = null;
		this.action_type = action_type;
		this.action = action;
		this.timestamp = null;
	}
	
	public Log(String personal_number, int record_id, int log_id, String action_type, String action, Timestamp timestamp) {
		this.personal_number = personal_number;
		this.record_id = record_id;
		this.log_id = log_id;
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
	
	public Integer getLogId() {
		return log_id;
	}
	
	public ActionType getActionType() {
		
		ActionType at = null;
		
		switch (action_type.toLowerCase()) {
		case "add_user":
			
			break;
		case "modify_user":
			at = ActionType.MODIFY_USER;
			break;
		case "remove_user":
			at = ActionType.REMOVE_USER;
			break;
		case "lookup_user":
			at = ActionType.LOOKUP_USER;
			break;
		case "create_record":
			at = ActionType.CREATE_RECORD;
			break;
		case "read_record":
			at = ActionType.READ_RECORD;
			break;
		case "modify_record":
			at = ActionType.MODIFY_RECORD;
			break;
		case "remove_record":
			at = ActionType.REMOVE_RECORD;
			break;
		default:
			break;
		}
		return at;
	}
	
	public String getAction() {
		return action;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

}
