package utility;

public class Record {

    private Integer record_id;
    private final String patient_personal_number;
    private final String doctor_personal_number;
    private final String nurse_personal_number;
    private final String division;
    private final String record;

    public Record(String patient_personal_number, String doctor_personal_number, String nurse_personal_number,
                  String division, String record) {
        this.patient_personal_number = patient_personal_number;
        this.doctor_personal_number = doctor_personal_number;
        this.nurse_personal_number = nurse_personal_number;
        this.division = division;
        this.record = record;
    }

    public Record(String patient_personal_number, String doctor_personal_number, String nurse_personal_number,
                  String division, String record, int record_id) {
        this.patient_personal_number = patient_personal_number;
        this.doctor_personal_number = doctor_personal_number;
        this.nurse_personal_number = nurse_personal_number;
        this.division = division;
        this.record = record;
        this.record_id = record_id;
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

    public Integer getRecordId() {
        if (record_id == null) {
            return -1;
        } else {
            return record_id;
        }
    }

    @Override
    public String toString() {
        if (record_id == null) {
            return "[RECORD] Doctor=" + doctor_personal_number + " Nurse=" + nurse_personal_number + " Patient="
                    + patient_personal_number + "\nDivision=" + division + "\nRecord Description: " + record;
        } else {
            return "[RECORD] ID=" + record_id + "\nDoctor=" + doctor_personal_number + " Nurse=" + nurse_personal_number + " Patient="
                    + patient_personal_number + "\nDivision=" + division + "\nRecord Description: " + record;
        }

    }

}
