package utility;

import server.ServerMain;
import java.util.List;
import java.util.Set;

public class RequestHandler {

    private User selectedRecordUser = null;
    private User selectedNurse = null;
    private Record selectedRecord = null;


    public void processRequest(Request request, User user, Communicator communicator) {

        String type = request.type;
        String actionType = request.actionType;
        String data = request.data;

        if (type.equals("records") && actionType.equals("get")) {
            System.out.println(selectedRecordUser);
            List<Record> records = getRecordsOfUser(this.selectedRecordUser);

            communicator.send(new Request("records", "get", true, "start"));
            records.forEach(a -> {
                if (usersIsAllowedToReadRecord(user, a)) {
                    Log log = new Log(user.getPersonalNumber(), a.getRecordId(), ActionType.LIST_RECORD.toString(), user.getName() + " listed this record.");
                    ServerMain.databaseHandler.addLog(log);
                    communicator.send(new Request("records", "get", true, a.getRecordId().toString()));
                }
            });

            communicator.send(new Request("records", "get", true, null));
        } else if (type.equals("users") && actionType.equals("get") && data.equals("patients")) {
            // Returns a list of all patients.
            List<User> patients = getPatients();
            Log log = new Log(user.getPersonalNumber(), -1, ActionType.LIST_ALL_USERS.toString(), user.getName() + " listed all users he have access to.");


            ServerMain.databaseHandler.addLog(log);
            communicator.send(new Request("users", "get", true, "start"));
            patients.forEach(a -> {

                boolean send = false;
                switch (user.getRole()) {
                    case "Doctor":
                        send = true;
                        break;
                    case "Nurse":
                        send = true;
                        break;
                    case "Patient":
                        if (a.getPersonalNumber().equals(user.getPersonalNumber()))
                            send = true;
                        break;
                    case "Government":
                        send = true;
                        break;
                    default:
                        break;
                }
                if (send) {
                    communicator.send(new Request("users", "get", true, a.getPersonalNumber()));
                }
            });
            communicator.send(new Request("users", "get", true, null));
        } else if (type.equals("record") && actionType.equals("get")) {
            if (!usersIsAllowedToReadRecord(user, selectedRecord)) {
                communicator.send(new Request("record", "get", true, "Not allowed"));
                return;
            }
            List<User> patients = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getPatientPersonalNumber());
            List<User> doctors = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getDoctorPersonalNumber());
            List<User> nurses = ServerMain.databaseHandler.findUsers("personal_number", selectedRecord.getNursePersonalNumber());

            Log log = new Log(user.getPersonalNumber(), selectedRecord.getRecordId(), ActionType.READ_RECORD.toString(), user.getName() + " viewed a record.");
            ServerMain.databaseHandler.addLog(log);

            String patientName = "";
            String doctorName = "";
            String nurseName = "";

            if (patients.size() == 1 && doctors.size() == 1 && nurses.size() == 1) {
                patientName = patients.get(0).getName();
                doctorName = doctors.get(0).getName();
                nurseName = nurses.get(0).getName();
            }

            communicator.send(new Request("record", "get", true, selectedRecord.getPatientPersonalNumber()));
            communicator.send(new Request("record", "get", true, patientName));

            communicator.send(new Request("record", "get", true, selectedRecord.getDoctorPersonalNumber()));
            communicator.send(new Request("record", "get", true, doctorName));

            communicator.send(new Request("record", "get", true, selectedRecord.getNursePersonalNumber()));
            communicator.send(new Request("record", "get", true, nurseName));

            communicator.send(new Request("record", "get", true, selectedRecord.getRecord()));
        } else if (type.equals("record") && actionType.equals("post")) {

            if (isUsersAllowedToWriteToRecord(user, selectedRecord)) {
                Log log = new Log(user.getPersonalNumber(), selectedRecord.getRecordId(), ActionType.MODIFY_RECORD.toString(), user.getName() + " modified a record.");

                ServerMain.databaseHandler.updateRecord("record", data, "record_id", selectedRecord.getRecordId().toString());
                communicator.send(new Request("success", "get", true, "Success"));
                ServerMain.databaseHandler.addLog(log);
            } else {
                communicator.send(new Request("error", "get", true, "Not allowed"));
            }

        } else if (type.equals("record") && actionType.equals("delete")) {
            if (user.getRole().equals("Government")) {
                Log log = new Log(user.getPersonalNumber(), selectedRecord.getRecordId(), ActionType.REMOVE_RECORD.toString(), user.getName() + " deleted record.");
                ServerMain.databaseHandler.addLog(log);
                ServerMain.databaseHandler.deleteRecord(selectedRecord.getRecordId());
                selectedRecord = null;
                communicator.send(new Request("success", "get", true, "success"));
            } else {
                communicator.send(new Request("error", "get", true, "Not allowed"));
            }

        } else if (type.equals("add_record") && actionType.equals("post")) {
            System.out.println(user.getPersonalNumber());
            Set<String> patients = ServerMain.databaseHandler.findPatients(user.getPersonalNumber());
            System.out.println(patients);


            if (user.getRole().equals("Doctor") && patients.contains(selectedRecordUser.getPersonalNumber())) {
                Record record = new Record(selectedRecordUser.getPersonalNumber(), user.getPersonalNumber(), selectedNurse.getPersonalNumber(), user.getDivision(), data);
                ServerMain.databaseHandler.addRecord(record);

                int record_id = ServerMain.databaseHandler.getLatestRecordId();
                Log log = new Log(user.getPersonalNumber(), record_id, ActionType.CREATE_RECORD.toString(), user.getName() + " created record with id=" + record_id + ".");
                ServerMain.databaseHandler.addLog(log);
                communicator.send(new Request("success", "get", true, "Success"));
            } else {
                communicator.send(new Request("error", "get", true, "Not allowed"));
            }
        } else if (type.equals("selectRecordUser") && actionType.equals("post")) {
            System.out.println("SELECTED RECORD USeR");
            List<User> users = ServerMain.databaseHandler.findUsers("personal_number", data);
            if (users.size() != 1) {
                communicator.send(new Request("selectedRecordUser", "post", true, "failed"));
            }
            selectedRecordUser = users.get(0);
            communicator.send(new Request("selectedRecordUser", "post", true, "success"));
        } else if (type.equals("selectRecord") && actionType.equals("post")) {
            List<Record> records = ServerMain.databaseHandler.findRecords("record_id", data);
            if (records.size() != 1) {
                communicator.send(new Request("selectRecord", "post", true, "failed"));
            }
            selectedRecord = records.get(0);
            communicator.send(new Request("selectRecord", "post", true, "success"));
        } else if (type.equals("selectNurse") && actionType.equals("post")) {
            List<User> nurses = ServerMain.databaseHandler.findUsers("personal_number", data);
            if (nurses.size() != 1) {
                communicator.send(new Request("selectedRecordUser", "post", true, "failed"));
            } else {
                selectedNurse = nurses.get(0);
                communicator.send(new Request("selectedRecordUser", "post", true, "success"));
            }
        }
    }

    private static List<User> getPatients() {
        System.out.println("getPatients");
        return ServerMain.databaseHandler.findUsers("role", "Patient");
    }

    private static List<Record> getRecordsOfUser(User user) {
        System.out.println("Records:");
        System.out.println("User:" + user);
        List<Record> records = null;
        if (user.getRole().equals("Patient")) {
            records = ServerMain.databaseHandler.findRecords("patient_personal_number", user.getPersonalNumber());
            System.out.println(records);
        }
        return records;
    }

    private boolean usersIsAllowedToReadRecord(User user, Record record) {
        String role = user.getRole();
        String division = user.getDivision();
        switch (role) {
            case "Doctor":
                return user.getPersonalNumber().equals(record.getDoctorPersonalNumber()) || division.equals(record.getDivision());
            case "Nurse":
                return user.getPersonalNumber().equals(record.getNursePersonalNumber()) || division.equals(record.getDivision());
            case "Patient":
                return user.getPersonalNumber().equals(record.getPatientPersonalNumber());

            default:
                return role.equals("Government");
        }
    }

    private boolean isUsersAllowedToWriteToRecord(User user, Record record) {
        String role = user.getRole();
        if (role.equals("Doctor") || role.equals("Nurse")) {
            return record.getDoctorPersonalNumber().equals(user.getPersonalNumber()) || record.getNursePersonalNumber().equals(user.getPersonalNumber());
        }
        return false;
    }


}
