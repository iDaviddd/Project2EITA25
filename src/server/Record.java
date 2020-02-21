package server;

public class Record {
	
	private String patient_personal_number;
	private String doctor_personal_number;
	private String nurse_personal_number;
	private String division;
	private String record;
	
	public Record(String patient_personal_number, String doctor_personal_number, String nurse_personal_number, String division, String record) {
		this.patient_personal_number = patient_personal_number;
		this.doctor_personal_number = doctor_personal_number;
		this.nurse_personal_number = nurse_personal_number;
		this.division = division;
		this.record = record;
	}

	public String getDoctorPersonalNumber() {
		return doctor_personal_number;
	}
	
	public String getPatientPersonalNumber() {
		return patient_personal_number;
	}
	
	public String getNursePersonalNumber() {
		return nurse_personal_number;
	}
	
	public String getDivision() {
		return division;
	}
	
	public String getRecord() {
		return record;
	}

}
