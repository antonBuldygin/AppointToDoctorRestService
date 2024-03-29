import AppointToDoctorRestService.Main;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isNumber;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isObject;


class RequestForTest {


    private Map<String, Object> properties = new LinkedHashMap<>();



    public RequestForTest(RequestForTest another) {
        this.properties = another.properties.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public RequestForTest() {
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(this.properties);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }


    public RequestForTest setProps(String key, Object value) {
        properties.put(key, value);
        return this;
    }

}

public class AppointmentBookingToDoctorRestServiceTest extends SpringTest {

    private static final String databaseFileName = "\\AppointToDoctorRestService\\AppointToDoctorRestService\\d.mv.db";

     public AppointmentBookingToDoctorRestServiceTest (){
        super(Main.class, "../d.mv.db");
    }

    // public AppointmentBookingToDoctorRestServiceTest() {
    //     super(Main.class, 28852);

    // }

    public String toJsonStatistics(Map<String, Integer> map) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    private String convert(String[] trs) {
        JsonArray jsonArray = new JsonArray();
        for (String tr : trs) {
            JsonElement jsonObject = JsonParser.parseString(tr);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    //quantity of available days for appointment for each doctor
    private final int availableDays = 4;

    //Endpoints list
    private final String setAppointment = "/setAppointment";
    private final String appointments = "/appointments";
    private final String deleteAppointment = "/deleteAppointment?id=";
    private final String newDoctor = "/newDoctor";
    private final String availbleDates = "/availableDatesByDoctor?doc=";

    private final String deleteDoctor = "/deleteDoctor?doc=";
    private final String statisticsDoctor = "/statisticsDoc";
    private final String statisticsDay = "/statisticsDay";

    // Doctors' names
    private final String phillGood = "Phill good";
    private final String leaWong = "Lea Wong";
    private final String director = "director";
    private final String pamelaUpperson = "Pamela Upperson";
    private final String doctorHouse = "Dr. House";
    private final String unknownDoctor = "Unknown";

    //List and maps for corrent information storage about doctors, appointments, available days, statistics
    List<Long> ids = new ArrayList<>();
    List<Long> idsForAppointments = new ArrayList<>();
    Map<String, JsonArray> mapOfAvailableDaysByDoctor = new TreeMap<>();
    Map<String, Integer> statisticsPerDoctor = new TreeMap<>();
    Map<String, Integer> statisticsPerDay;
    List<String> appointmentsCorrectJson = new ArrayList<>();
    List<String> doctorsList = new ArrayList<>();
    List<Long> doctorsIdList = new ArrayList<>();


    LocalDate date = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String directorApp1 = new RequestForTest().setProps("doctor", director)
            .setProps("patient", "Bol it")
            .setProps("date", dateTimeFormatter.format(date)).toJson();
    private final String leaWongApp1 = new RequestForTest().setProps("doctor", leaWong)
            .setProps("patient", "Ay Bolit")
            .setProps("date", dateTimeFormatter.format(date.plusDays(1))).toJson();

    private final String pamelaUppersonApp1 = new RequestForTest().setProps("doctor", "Pamela Upperson")
            .setProps("patient", "John Galt")
            .setProps("date", dateTimeFormatter.format(date.plusDays(1))).toJson();

    private final String leaWongApp2 = new RequestForTest().setProps("doctor", "Lea Wong")
            .setProps("patient", "Indi Grimes")
            .setProps("date", dateTimeFormatter.format(date.plusDays(2))).toJson();

    private final String leaWongApp3 = new RequestForTest().setProps("doctor", "Lea Wong")
            .setProps("patient", "Indi Grimes")
            .setProps("date", dateTimeFormatter.format(date.plusDays(3))).toJson();

    private final String leaWongApp4 = new RequestForTest().setProps("doctor", "Lea Wong")
            .setProps("patient", "Indi Grimes")
            .setProps("date", dateTimeFormatter.format(date.plusDays(4))).toJson();

    private final String pamelaUppersonApp2 = new RequestForTest().setProps("doctor", "Pamela Upperson")
            .setProps("patient", "Ay")
            .setProps("date", dateTimeFormatter.format(date.plusDays(2))).toJson();

    private final String pamelaUppersonApp3 = new RequestForTest().setProps("doctor", "Pamela Upperson")
            .setProps("patient", "Ay")
            .setProps("date", dateTimeFormatter.format(date.plusDays(3))).toJson();

    private final String pamelaUppersonApp4 = new RequestForTest().setProps("doctor", "Pamela Upperson")
            .setProps("patient", "Ay")
            .setProps("date", dateTimeFormatter.format(date.plusDays(4))).toJson();

    private final RequestForTest newDocLeaWong = new RequestForTest().setProps("doctor", leaWong)
            .setProps("patient", "Bol it")
            .setProps("date", dateTimeFormatter.format(date.plusDays(4)));


    private final String doctorNameEmpty = new RequestForTest(newDocLeaWong).setProps("doctor", "").toJson();
    private final String doctorNameSpaces = new RequestForTest(newDocLeaWong).setProps("doctor", "   ").toJson();
    private final String noPatientName = new RequestForTest(newDocLeaWong).setProps("patient", null).toJson();
    private final String patientNameEmpty = new RequestForTest(newDocLeaWong).setProps("patient", "").toJson();
    private final String patientSpaces = new RequestForTest(newDocLeaWong).setProps("patient", "      ").toJson();
    private final String noDoctorName = new RequestForTest(newDocLeaWong).setProps("doctor", null).toJson();
    private final String dateEmpty = new RequestForTest(newDocLeaWong).setProps("date", "").toJson();
    private final String noDate = new RequestForTest(newDocLeaWong).setProps("date", null).toJson();
    private final String wrongDateFormat = new RequestForTest(newDocLeaWong).setProps("date", "2021-10-11 22:00").toJson();


    private final String doctorPhilGood = new RequestForTest().setProps("doctorName", phillGood).toJson();
    private final String doctorLeaWong = new RequestForTest().setProps("doctorName", leaWong).toJson();
    private final String doctorPamelaUpperson = new RequestForTest().setProps("doctorName", pamelaUpperson).toJson();
    private final String doctorDrHouse = new RequestForTest().setProps("doctorName", doctorHouse).toJson();
    private final String docDirector = new RequestForTest().setProps("doctorName", director).toJson();
    private final String doctorAddEmptyName = new RequestForTest().setProps("doctorName", "").toJson();
    private final String doctorAddNull = new RequestForTest().toJson();
    private final String doctorAddEmptySpaces = new RequestForTest().setProps("doctorName", "         ").toJson();

    CheckResult testPostApi(String api, String body, int status, String message) {
        HttpResponse response = post(api, body).send();
        if (response.getStatusCode() != status) {
            return CheckResult.wrong("POST " + api + " should respond with "
                    + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
                    + message + "\n"
                    + "Response body:\n" + response.getContent() + "\n"
                    + "Request body:\n" + body);
        }
        return CheckResult.correct();
    }

    CheckResult testGetApi(String api, int status, String message) {
        HttpResponse response = get(api).send();
        if (response.getStatusCode() != status) {
            return CheckResult.wrong("GET " + api + " should respond with "
                    + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
                    + message + "\n"
                    + "Response body:\n" + response.getContent() + "\n"
            );
        }
        System.out.println(response.getContent());
        return CheckResult.correct();
    }

    @DynamicTest(order = -1)
    DynamicTesting[] dt = new DynamicTesting[]{

            // negative tests for newDoctor, available days Api
            () -> testGetApi(availbleDates + leaWong.trim().replaceAll("[\\s]+", "%20"), 404, "Wrong Status code"),//#1
            () -> testPostApi(newDoctor, doctorAddEmptyName, 400, "Empty doctorName field!"),//#2
            () -> testPostApi(newDoctor, doctorAddNull, 400, "doctorName field is absent!"),//#3
            () -> testPostApi(newDoctor, doctorAddEmptySpaces, 400, "doctorName field is absent!"),//#4


            // negative tests  for appointments, setAppointment Api
            () -> testGetApi(appointments, 204, "Wrong Status code"),//#5
            () -> testPostApi(setAppointment, doctorNameEmpty, 400, "Empty doctorName field!"),//#6
            () -> testPostApi(setAppointment, noDoctorName, 400, "doctorName field is absent!"),//#7
            () -> testPostApi(setAppointment, doctorNameSpaces, 400, "doctorName field is absent!"),//#8

            // negative tests for  available days Api
            () -> testGetApi(availbleDates + unknownDoctor.trim().replaceAll("[\\s]+", "%20"),
                    404, "should answer status 204 - no available time for unknown doctor "),//#9


            // negative tests for  available days Api
            () -> testAvailableDatesByDoctor(leaWong, availableDays, 404),//#10

            //checking Doctors endpoints (Lea Wong)
            () -> newDoctorEndpointCheck(doctorLeaWong),//#11
            () -> testPostApi(newDoctor, doctorLeaWong, 400, "Should not add new doctor with the same name"),//#12
            () -> testAvailableDatesByDoctor(leaWong, availableDays, 200), //#13
            () -> getAllDoctorslist(),//#14

            // negative tests for appointments, setAppointment Api (patients and dates)
            () -> testPostApi(setAppointment, patientNameEmpty, 400, "Empty patientName field!"),//#15
            () -> testPostApi(setAppointment, noPatientName, 400, "patientName field is absent!"),//#16
            () -> testPostApi(setAppointment, patientSpaces, 400, "patientName field is absent!"),//#17
            () -> testPostApi(setAppointment, dateEmpty, 400, "Empty date field!"),//#18
            () -> testPostApi(setAppointment, noDate, 400, "date field is absent!"),//#19
            () -> testGetApi(appointments, 204, "Wrong Status code"),//#20
            () -> testPostApi(setAppointment, wrongDateFormat, 400, "patientName field is absent!"),//#21

            () -> testAvailableDatesByDoctor(director, availableDays, 404),//#22
            () -> testAvailableDatesByDoctor(leaWong, availableDays, 200),//#23

            () -> testPostApi(newDoctor, doctorLeaWong, 400, "Should not add new doctor with the same name"),//#24
            () -> testAvailableDatesByDoctor(leaWong, availableDays, 200),//#25

            //checking Doctors endpoints (Pamela Upperson)
            () -> newDoctorEndpointCheck(doctorPamelaUpperson),//#26
            () -> testPostApi(newDoctor, doctorPamelaUpperson, 400, "Should not add new doctor with the same name"),//#27
            () -> getAllDoctorslist(), //#28


            //checking Doctors endpoints (Phil Good)
            () -> newDoctorEndpointCheck(doctorPhilGood),//#29
            () -> testAvailableDatesByDoctor(phillGood, availableDays, 200),//#30
            () -> testPostApi(newDoctor, doctorPhilGood, 400, "Should not add new doctor with the same name"),//#31
            this::reloadServer,//#32

            () -> getAllDoctorslist(), //#33
            this::reloadServer,//#34

            // negative tests for  available days Api for (Pamela Upperson)
            () -> testPostApi(newDoctor, doctorPamelaUpperson, 400, "Should not add new doctor with the same name"),//#35

            //checking Doctors endpoints (Dr. House)
            () -> newDoctorEndpointCheck(doctorDrHouse),//#36
            () -> testPostApi(newDoctor, doctorDrHouse, 400, "Should not add new doctor with the same name"),//#37
            () -> testPostApi(newDoctor, doctorDrHouse, 400, "Should not add new doctor with the same name"),//#38
            () -> getAllDoctorslist(),//#39

            //checking setAppointment and Appointments  endPoints for Lea Wong
            () -> testPostSetAppointments(leaWongApp1),//#40
            () -> testPostSetAppointments(leaWongApp2),//#41
            () -> testPostSetAppointments(leaWongApp3),//#42
            () -> testGetAllappointments(),//#43
            this::reloadServer,//#44
            //checking update of available days
            () -> testAvailableDatesByDoctor(leaWong, availableDays, 200),//#45
            () -> testAvailableDatesByDoctor(pamelaUpperson, availableDays, 200),//#46

            //checking setAppointment and Appointments  endPoints for Pamela Upperson
            () -> testPostSetAppointments(pamelaUppersonApp1),//#47
            () -> testPostSetAppointments(pamelaUppersonApp2),//#48
            () -> testPostSetAppointments(pamelaUppersonApp3),//#49

            //checking update of available days for Dr. House
            () -> testAvailableDatesByDoctor(doctorHouse, availableDays, 200),//#50

            //checking 4th setAppointment and Appointments  endPoints for Pamela Upperson and Lea Wong
            () -> testPostSetAppointments(leaWongApp4),//#51
            () -> testPostSetAppointments(pamelaUppersonApp4),//#52
            () -> testGetAllappointments(),//#53

            //checking deleteAppointment endPoints
            () -> testDeleteAppointment(),//#54
            () -> testGetApi(appointments, 204, "Wrong Status code"),//#55
            () -> testDeleteAppointmentApi(400, "Wrong Status code"),//#56

            () -> testGetApi(appointments, 204, "Wrong Status code"),//#57

            //checking update of available days for doctors after  deleteAppointments
            () -> testAvailableDatesByDoctor(leaWong, availableDays, 200),//#58
            () -> testAvailableDatesByDoctor(pamelaUpperson, availableDays, 200),//#59

            //checking setAppointment  endPoint
            () -> testPostSetAppointments(pamelaUppersonApp1),//#60
            () -> testPostSetAppointments(pamelaUppersonApp2),//#61
            () -> testPostSetAppointments(pamelaUppersonApp3),//#62
            () -> testPostSetAppointments(leaWongApp1),//#63

            //checking update of available days for doctor Lea Wong
            () -> testAvailableDatesByDoctor(leaWong, availableDays, 200),//#64


            () -> newDoctorEndpointCheck(docDirector),//#65

            () -> testAvailableDatesByDoctor(director, availableDays, 200),//#66


            () -> testAvailableDatesByDoctor(pamelaUpperson, availableDays, 200),//#67

    };

    //Test newDoctor POST endPoint
    CheckResult newDoctorEndpointCheck(String docName) {
        HttpResponse response = post("/newDoctor", docName).send();
        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("POST /newDoctor should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }
        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("POST /newDoctor should return a valid JSON");
        }

        JsonObject responseJson = getJson(docName).getAsJsonObject();
        String doctorName = responseJson.get("doctorName").getAsString().trim().toLowerCase();
        System.out.println(response.getContent());
        expect(String.valueOf(json)).asJson().check(
                isObject()
                        .value("id", isNumber())
                        .value("doctorName", doctorName)
        );

        long id = getJson(response.getContent()).getAsJsonObject().get("id").getAsLong();
        if (doctorsIdList.contains(id)) {
            return CheckResult.wrong("id should be unique. This id " + id + " exist");
        }
        doctorsList.add(response.getContent());
        doctorsIdList.add(id);

        return CheckResult.correct();
    }


    //Test AllDoctorslist GET endPoint
    CheckResult getAllDoctorslist() {
        HttpResponse response = get("/allDoctorslist").send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("GET /allDoctorslist should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("GET /allDoctorslist should return a valid JSON");
        }

        if (!response.getJson().isJsonArray()) {
            return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
                    response.getContent().getClass());
        }

        System.out.println(response.getContent());
        System.out.println(doctorsList.toString());

        String correctJsonToString = convert(doctorsList.toArray(new String[doctorsList.size()]));
        JsonArray correctJson = getJson(correctJsonToString).getAsJsonArray();
        JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();

        if (responseJson.size() != correctJson.size()) {
            return CheckResult.wrong("Correct json array size should be" +
                    correctJson.size() + "\n\n" +
                    "but found:\n" + responseJson.size());
        }

        for (int i = 0; i < responseJson.size(); i++) {
            long id = correctJson.get(i).getAsJsonObject().get("id").getAsLong();
//            System.out.println(id);

            expect(responseJson.get(i).getAsJsonObject().toString()).asJson()
                    .check(isObject()
                            .value("id", id)
                            .value("doctorName", correctJson.get(i).getAsJsonObject().get("doctorName").getAsString()));


        }
        return CheckResult.correct();
    }

    //Test availableDatesByDoctor GET endPoint
    CheckResult testAvailableDatesByDoctor(String doctorName, int avalableDays, int status) {
        HttpResponse response = get("/availableDatesByDoctor?doc=" + doctorName.replaceAll("[\\s]+", "%20")).send();

        if (response.getStatusCode() != status) {
            return CheckResult.wrong("GET /availableDatesByDoctor?doc=" + doctorName + " should respond with " +
                    "status code " + status + " , responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        if (status == 200) {
            try {
                json = response.getJson();
            } catch (Exception ex) {
                return CheckResult.wrong("GET /availableDatesByDoctor?doc= " + doctorName + " should return a valid JSON");
            }

            if (!response.getJson().isJsonArray()) {
                return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
                        response.getContent().getClass());
            }


            System.out.println(response.getContent());


            JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();


            if (doctorsList.stream().filter(s -> s.contains(doctorName.trim().toLowerCase())).findAny().isPresent()) {


                if (responseJson.size() != avalableDays) {
                    return CheckResult.wrong("Correct json array size should be " +
                            avalableDays + "\n" +
                            "but found:\n" + responseJson.size());
                }

                //
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String, String>>>() {
                }.getType();
                List<Map<String, String>> myMap = gson.fromJson(responseJson, type);



                for (int i = 0; i < myMap.size(); i++) {
                    if(myMap.get(i).size()!=2){ return CheckResult.wrong("Wrong. " +
                            "Response should contain 2 names in each JSON object\n");}

                    for (Map.Entry<String, String> entry : myMap.get(i).entrySet()) {
                        int countB = 0;
                        int countA = 0;

                        if (!entry.getKey().equals("booked")) {
                            countA++;
                            System.out.println(entry.getKey());
                            if (!entry.getKey().equals("availabletime")) {
                                return CheckResult.wrong("Wrong name in JSON object \n"
                                        + "Expected response: " + "availabletime, " + " responded: " + entry.getKey());
                            }
                            if (countA > 1) {
                                return CheckResult.wrong("Wrong name in JSON object \n"
                                        + "Expected response: " + "booked, " + " responded: " + entry.getKey());
                            }

                        }

                        if (!entry.getKey().equals("availabletime")) {

                            countB++;
                            System.out.println(entry.getKey());

                            if (!entry.getKey().equals("booked")) {
                                return CheckResult.wrong("Wrong name in JSON object \n"
                                        + "Expected response: " + "booked, " + " responded: " + entry.getKey());
                            }
                            if (countB > 1) {
                                return CheckResult.wrong("Wrong name in JSON object \n"
                                        + "Expected response: " + "availabletime, " + " responded: " + entry.getKey());
                            }

                        }

                    }

                }
                //


                if (mapOfAvailableDaysByDoctor.isEmpty() || mapOfAvailableDaysByDoctor.get(doctorName.trim().toLowerCase()) == null) {

                    mapOfAvailableDaysByDoctor.put(doctorName.trim().toLowerCase(), responseJson);
                }

                if (!mapOfAvailableDaysByDoctor.isEmpty() && !mapOfAvailableDaysByDoctor.get(doctorName.trim().toLowerCase()).equals(responseJson)) {
                    return CheckResult.wrong("Wrong object in response, expected array of JSON " + mapOfAvailableDaysByDoctor.get(doctorName.trim().toLowerCase()).toString() +
                            " but it was" + response.getContent());
                }
                if (!mapOfAvailableDaysByDoctor.isEmpty() && mapOfAvailableDaysByDoctor.get(doctorName.trim().toLowerCase()).equals(responseJson)) {
                    JsonArray correctJson = mapOfAvailableDaysByDoctor.get(doctorName.trim().toLowerCase());

                    for (int i = 0; i < responseJson.size(); i++) {
                        String date = correctJson.get(i).getAsJsonObject().get("availabletime").getAsString();
                        boolean booked = correctJson.get(i).getAsJsonObject().get("booked").getAsBoolean();
//            System.out.println(id);

                        expect(responseJson.get(i).getAsJsonObject().toString()).asJson()
                                .check(isObject()
                                        .value("availabletime", date)
                                        .value("booked", booked));
                    }


                }
            } else if (!doctorsList.stream().filter(s -> s.contains(doctorName.trim().toLowerCase())).findAny().isPresent()) {
                if (responseJson.size() != 0) {
                    return CheckResult.wrong("Correct json array size should be 0 " +
                            "\n" +
                            "but found:\n" + responseJson.size());
                }
            }

            System.out.println(mapOfAvailableDaysByDoctor.entrySet());
            System.out.println(doctorsList.toString());
        }

        return CheckResult.correct();
    }


    // Test Post setAppointment
    CheckResult testPostSetAppointments(String appBody) {
        HttpResponse response = post("/setAppointment", appBody).send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("POST /setAppointment should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("POST /setAppointment should return a valid JSON");
        }

        JsonObject userJson = getJson(appBody).getAsJsonObject();
        String patient = userJson.get("patient").getAsString().trim().toLowerCase();
        String doctor = userJson.get("doctor").getAsString().toLowerCase().trim();
        String date = userJson.get("date").getAsString().toLowerCase().trim();
        System.out.println(date + "   " + doctor + " " + patient);

        expect(String.valueOf(json)).asJson().check(

                isObject()
                        .value("idApp", isNumber())
                        .value("doctor", doctor)
//                        .value("specialization", "physician")
//                        .value("doctorId", isNumber())
                        .value("patient", patient)
                        .value("date", date)

        );


        long id = getJson(response.getContent()).getAsJsonObject().get("idApp").getAsLong();
        if (idsForAppointments.contains(id)) {
            return CheckResult.wrong("id should be unique. This id " + id + " exist");
        }
        appointmentsCorrectJson.add(response.getContent());
        idsForAppointments.add(id);

        if (statisticsPerDoctor != null && statisticsPerDoctor.containsKey(doctor)) {
            statisticsPerDoctor.put(doctor, statisticsPerDoctor.get(doctor) + 1);
        }
        if (statisticsPerDay != null && statisticsPerDay.containsKey(date)) {
            statisticsPerDay.put(date, statisticsPerDay.get(date) + 1);
        }

        if (statisticsPerDoctor != null && !statisticsPerDoctor.containsKey(doctor)) {
            statisticsPerDoctor.put(doctor, 1);
        }
        if (statisticsPerDay != null && !statisticsPerDay.containsKey(date)) {
            statisticsPerDay.put(date, 1);
        }
        if (statisticsPerDoctor == null || !statisticsPerDoctor.containsKey(doctor)) {
            statisticsPerDoctor = new TreeMap<>();
            statisticsPerDoctor.put(doctor, 1);
        }
        if (statisticsPerDay == null || !statisticsPerDay.containsKey(date)) {
            statisticsPerDay = new TreeMap<>();
            statisticsPerDay.put(date, 1);
        }

        for (Map.Entry<String, JsonArray> entry : mapOfAvailableDaysByDoctor.entrySet()
        ) {
            if (entry.getKey().equals(doctor)) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    String avalabletime = entry.getValue().get(i).getAsJsonObject().get("availabletime").toString().replaceAll("\"", "");
                    if (avalabletime.equals(date)) {

                        entry.getValue().get(i).getAsJsonObject().addProperty("availabletime", avalabletime);
                        entry.getValue().get(i).getAsJsonObject().addProperty("booked", true);
                        System.out.println(entry.getValue().get(i).getAsJsonObject().get("booked"));
                        System.out.println(entry.getValue().get(i).getAsJsonObject().get("availabletime"));
                    }
                }
            }
        }
        System.out.println(mapOfAvailableDaysByDoctor.entrySet());
        return CheckResult.correct();
    }

    CheckResult testGetAllappointments() {

        HttpResponse response = get("/appointments").send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("GET /appointments should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("GET /appointments should return a valid JSON");
        }

        if (!response.getJson().isJsonArray()) {
            return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
                    response.getContent().getClass());
        }

        System.out.println(response.getContent());
        System.out.println(appointmentsCorrectJson.toString());

        String correctJsonToString = convert(appointmentsCorrectJson.toArray(new String[appointmentsCorrectJson.size()]));
        JsonArray correctJson = getJson(correctJsonToString).getAsJsonArray();
        JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();


        if (responseJson.size() != correctJson.size()) {
            return CheckResult.wrong("Correct json array size should be" +
                    correctJson.size() + "\n\n" +
                    "but found:\n" + responseJson.size());
        }


        for (int i = 0; i < responseJson.size(); i++) {


            expect(responseJson.get(i).getAsJsonObject().toString()).asJson()
                    .check(isObject()
                            .value("idApp", correctJson.get(i).getAsJsonObject().get("idApp").getAsLong())
                            .value("doctor", correctJson.get(i).getAsJsonObject().get("doctor").getAsString())
//                            .value("specialization", correctJson.get(i).getAsJsonObject().get("specialization").getAsString())
//                            .value("doctorId", correctJson.get(i).getAsJsonObject().get("doctorId").getAsLong())
                            .value("patient", correctJson.get(i).getAsJsonObject().get("patient").getAsString())
                            .value("date", correctJson.get(i).getAsJsonObject().get("date").getAsString()));
//            ids.add(correctJson.get(i).getAsJsonObject().get("idApp").getAsLong());

        }

        return CheckResult.correct();
    }


    CheckResult testDeleteDoctor(String api, String param, int status, String message) {
        HttpResponse response = delete(api + param.trim().replaceAll("[\\s]+", "%20")).send();
        if (response.getStatusCode() != status) {
            return CheckResult.wrong("DELETE " + api + " should respond with "
                    + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
                    + message + "\n"
                    + "Response body:\n" + response.getContent() + "\n"
                    + "Parameter:\n" + param);
        }

        if (status == 200) {
            JsonElement json;
            try {
                json = response.getJson();
            } catch (Exception ex) {
                return CheckResult.wrong("DELETE " + api + param + "should return a valid JSON");
            }
            if (doctorsList.isEmpty()) {
                return CheckResult.correct();
            }
            String correctJsonToString = convert(doctorsList.toArray(new String[doctorsList.size()]));
            JsonArray correctJson = getJson(correctJsonToString).getAsJsonArray();
            String correctnameToBedeleted = "";
            Long correctId = -1L;

            for (int i = 0; i < correctJson.size(); i++) {
                if (correctJson.get(i).getAsJsonObject().get("doctorName").getAsString().equals(param.trim().toLowerCase())) {

                    correctnameToBedeleted = correctJson.get(i).getAsJsonObject().get("doctorName").getAsString();
                    correctId = correctJson.get(i).getAsJsonObject().get("id").getAsLong();

                }
            }

            expect(String.valueOf(json)).asJson().check(

                    isObject()
                            .value("id", correctId)
                            .value("doctorName", correctnameToBedeleted)

            );

            final String forFilter = correctnameToBedeleted;
            doctorsList = doctorsList.stream().filter(s -> !s.contains(forFilter)).collect(Collectors.toList());
            Optional<String> director1 = doctorsList.stream().filter(s -> s.contains("director")).findAny();
            if (!director1.isPresent()) {
                String director = new RequestForTest().setProps("id", 10000).setProps("doctorName", "director").toJson();
                doctorsList.add(director);
            }
            String keyToRemove = "";
            for (Map.Entry<String, JsonArray> entry : mapOfAvailableDaysByDoctor.entrySet()) {
                if (entry.getKey().equals(forFilter)) {

                    keyToRemove = entry.getKey();
                }
            }
            mapOfAvailableDaysByDoctor.remove(keyToRemove);
            Integer doctorAppointmentsCount = 0;
            Integer directorAppoinementsCount = 0;
            String key = "";
            for (Map.Entry<String, Integer> entry : statisticsPerDoctor.entrySet()) {
                if (entry.getKey().equals(forFilter)) {
                    key = entry.getKey();
                    doctorAppointmentsCount = entry.getValue();
                    statisticsPerDoctor.remove(entry.getKey());

                }
                if (entry.getKey().equals("director")) {

                    directorAppoinementsCount = entry.getValue();

                }

            }
            if (!statisticsPerDoctor.isEmpty() && !key.equals("")) {
                statisticsPerDoctor.put("director", doctorAppointmentsCount + directorAppoinementsCount);
            }


        }
        return CheckResult.correct();
    }


    CheckResult testGetStatisticPerDay(String api) {

        HttpResponse response = get(api).send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("GET " + api + " should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("GET " + api + " should return a valid JSON");
        }

        if (!response.getJson().isJsonArray()) {
            return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
                    response.getContent().getClass());
        }


        JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<TreeMap<String, Integer>>>() {
        }.getType();
        List<TreeMap<String, Integer>> listOfCountsPreday =
                gson.fromJson(responseJson, listType);
        Map<String, Integer> result = new TreeMap<>();

        for (int i = 0; i < listOfCountsPreday.size(); i++) {

            Map<String, Integer> collect = listOfCountsPreday.get(i)
                    .entrySet().stream()
                    .collect(Collectors
                            .toMap(Map.Entry::getKey, Map.Entry::getValue));
            collect.forEach((k, v) -> result.putIfAbsent(k, v));
        }


        System.out.println(listOfCountsPreday.toString());
        System.out.println(result);

        System.out.println(statisticsPerDay);
        if (!result.entrySet().stream()
                .allMatch(e -> e.getValue().equals(statisticsPerDay.get(e.getKey())))) {
            return CheckResult.wrong("incorrect statistics");

        }

        return CheckResult.correct();
    }


    CheckResult testGetStatisticPerDoctor(String api) {

        HttpResponse response = get(api).send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("GET " + api + " should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("GET " + api + " should return a valid JSON");
        }
        if (!response.getJson().isJsonArray()) {
            return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
                    response.getContent().getClass());
        }

        JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<HashMap<String, Integer>>>() {
        }.getType();
        List<HashMap<String, Integer>> listOfCountsPerDoc =
                gson.fromJson(responseJson, listType);
        Map<String, Integer> result = new TreeMap<>();

        for (int i = 0; i < listOfCountsPerDoc.size(); i++) {

            Map<String, Integer> collect = listOfCountsPerDoc.get(i)
                    .entrySet().stream()
                    .collect(Collectors
                            .toMap(Map.Entry::getKey, Map.Entry::getValue));
            collect.forEach((k, v) -> result.putIfAbsent(k, v));
        }


        System.out.println(listOfCountsPerDoc.toString());
        System.out.println(result);
        System.out.println(statisticsPerDoctor);
        if (!result.entrySet().stream()
                .allMatch(e -> e.getValue().equals(statisticsPerDoctor.get(e.getKey())))) {
            return CheckResult.wrong("incorrect statistics");

        }

        return CheckResult.correct();
    }


    CheckResult testDeleteAppointment() {
        int size = idsForAppointments.size();
        for (int i = 0; i < size; i++) {


            HttpResponse response = delete("deleteAppointment?id=" + idsForAppointments.get(i)).send();


            if (response.getStatusCode() == 409 && response.getContent().contains("The appointment does not exist or one of the fields is null!")) {
                System.out.println(response.getContent());
                return CheckResult.correct();
            } else if (response.getStatusCode() == 400 && response.getContent().contains("The appointment does not exist or was already cancelled")) {
                System.out.println(response.getContent());
                return CheckResult.correct();
            } else if (response.getStatusCode() == 500 && response.getContent().contains("The date field is null!")) {
                System.out.println(response.getContent());
                return CheckResult.correct();
            }

            if (response.getStatusCode() != 200) {
                return CheckResult.wrong("DELETE /deleteAppointment?id= should respond with " +
                        "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                        "Response body:\n" + response.getContent());
            }

            JsonElement json;
            try {
                json = response.getJson();
            } catch (Exception ex) {
                return CheckResult.wrong("DELETE /deleteAppointment?id= should return a valid JSON");
            }

//            if (!response.getJson().isJsonArray()) {
//                return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
//                        response.getContent().getClass());
//            }

            String correctJsonToString = convert(appointmentsCorrectJson.toArray(new String[appointmentsCorrectJson.size()]));
            JsonArray correctJson = getJson(correctJsonToString).getAsJsonArray();
            JsonObject responseJson = getJson(response.getContent()).getAsJsonObject();

            expect(responseJson.toString()).asJson()
                    .check(isObject()
                            .value("idApp", correctJson.get(i).getAsJsonObject().get("idApp").getAsLong())
                            .value("doctor", correctJson.get(i).getAsJsonObject().get("doctor").getAsString())
//                            .value("specialization", correctJson.get(i).getAsJsonObject().get("specialization").getAsString())
//                            .value("doctorId", correctJson.get(i).getAsJsonObject().get("doctorId").getAsLong())
                            .value("patient", correctJson.get(i).getAsJsonObject().get("patient").getAsString())
                            .value("date", correctJson.get(i).getAsJsonObject().get("date").getAsString()));

            for (Map.Entry<String, JsonArray> entry : mapOfAvailableDaysByDoctor.entrySet()
            ) {
                if (entry.getKey().equals(correctJson.get(i).getAsJsonObject().get("doctor").getAsString())) {
                    for (int m = 0; m < entry.getValue().size(); m++) {
                        String avalabletime = entry.getValue().get(m).getAsJsonObject().get("availabletime").toString().replaceAll("\"", "");
                        if (avalabletime.equals(correctJson.get(i).getAsJsonObject().get("date").getAsString())) {

                            entry.getValue().get(m).getAsJsonObject().addProperty("availabletime", avalabletime);
                            entry.getValue().get(m).getAsJsonObject().addProperty("booked", false);
                            System.out.println(entry.getValue().get(m).getAsJsonObject().get("booked"));
                            System.out.println(entry.getValue().get(m).getAsJsonObject().get("availabletime"));
                        }
                    }
                }
            }
            for (Map.Entry<String, Integer> entry : statisticsPerDoctor.entrySet()) {
                if (entry.getKey().equals(correctJson.get(i).getAsJsonObject().get("doctor").getAsString())) {
                    statisticsPerDoctor.put(entry.getKey(), entry.getValue() - 1);
                }
            }
            String keyToRemove = "";
            for (Map.Entry<String, Integer> entry : statisticsPerDay.entrySet()) {
                if (entry.getKey().equals(correctJson.get(i).getAsJsonObject().get("date").getAsString())) {
                    statisticsPerDay.put(entry.getKey(), entry.getValue() - 1);
                }
                if (entry.getValue() == 0) {
                    keyToRemove = entry.getKey();
                }
            }
            statisticsPerDay.remove(keyToRemove);

            System.out.println(mapOfAvailableDaysByDoctor.entrySet());

        }

        for (int i = 0; i < size; i++) {

            appointmentsCorrectJson.remove(0);
            idsForAppointments.remove(0);
        }
        return CheckResult.correct();
    }

    CheckResult testDeleteAppointmentApi(int status, String message) {
        HttpResponse response = delete("deleteAppointment?id=" + 11).send();
        if (response.getStatusCode() != status) {
            return CheckResult.wrong("DELETE " + "deleteAppointment?id= 11" + " should respond with "
                    + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
                    + message + "\n"
                    + "Response body:\n" + response.getContent() + "\n"
            );
        }

        if (response.getStatusCode() == 400 && !response.getContent().contains("The appointment does not exist or was already cancelled")) {
            return CheckResult.wrong("Expected  response : \"The appointment does not exist or was already cancelled\" but received " +
                    response.getContent());
        }


        return CheckResult.correct();
    }

    private CheckResult reloadServer() {
        try {
            reloadSpring();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return CheckResult.correct();
    }
    // @Before
    // public  void deleteDatabaseFile() {
    //     File file = new File(databaseFileName);

    //     if (!file.exists()) {
    //         return;
    //     }

    //     if (!file.delete()) {
    //         throw new WrongAnswer("Can't delete database file before starting your program.\n" +
    //                 "Make sure you close all the connections with the database file!");
    //     }
    // }
}
