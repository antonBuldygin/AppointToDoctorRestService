import AppointToDoctorRestService.Main;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;


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

    public AppointmentBookingToDoctorRestServiceTest() {
        super(Main.class, 28852);

    }
    private  final String setAppointment = "/setAppointment";
    private  final String appointments = "/appointments";
    private  final String deleteAppointment = "/deleteAppointment?id=";
    private  final String newDoctor = "/newDoctor";

    private final String newUser = new RequestForTest().setProps("doctor", "Phill good")
            .setProps("patient", "Ay Bolit")
            .setProps("date", "2021-12-01 22:00").toJson();

    private final String newUser1 = new RequestForTest().setProps("doctorName", "Pamela Upperson")
            .setProps("patientName", "John Galt")
            .setProps("date", "2021-12-01 22:00").toJson();
    private final String newUser2 = new RequestForTest().setProps("doctorName", "Lea Wong")
            .setProps("patientName", "Indi Grimes")
            .setProps("date", "2021-12-01 22:00").toJson();

    private final String errorUser = new RequestForTest().setProps("doctorName", "good")
            .setProps("patientName", "Ay")
            .setProps("date", "2021-12-21 22:00").toJson();

    private final RequestForTest user = new RequestForTest(). setProps("doctorName", "Phill")
            .setProps("patientName", "Bol it")
            .setProps("date", "2021-12-21 22:00");

    private final String doctorNameEmpty = new RequestForTest(user).setProps("doctorName", "").toJson();
    private final String NoPatientName = new RequestForTest(user).setProps("patientName", null).toJson();
    private final String patientNameEmpty =new RequestForTest(user).setProps("patientName", "").toJson();
    private final String NoDoctorName = new RequestForTest(user).setProps("doctorName", null).toJson();
    private final String dateEmpty =  new RequestForTest(user).setProps("date", "").toJson();
    private final String NoDate=  new RequestForTest(user).setProps("date", null).toJson();
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
        return CheckResult.correct();
    }

    @DynamicTest(order = -1)
    DynamicTesting[] dt = new DynamicTesting[]{

            // Test wrong POST request for POST apies

            () -> testPostApi(setAppointment, doctorNameEmpty, 400, "Empty doctorName field!"),
            () -> testPostApi(setAppointment, NoPatientName, 400, "patientName field is absent!"),
            () -> testPostApi(setAppointment, patientNameEmpty, 400, "Empty patientName field!"),
            () -> testPostApi(setAppointment,NoDoctorName, 400, "doctorName field is absent!"),
            () -> testPostApi(setAppointment, dateEmpty, 400, "Empty date field!"),
            () -> testPostApi(setAppointment, NoDate, 400, "date field is absent!"),
            () -> testGetApi(appointments,  204, "Wrong Status code"),


    };



    Object[][] array = {
            {1, newUser},
            {2, newUser1},
            {3, newUser2},
            {4, newUser1},
            {5, newUser1},
            {6, errorUser},
            {7, newUser}

    };
    List<String> resToCheck1 = new ArrayList<>();


    @DynamicTest(order = 0, data = "array")
    CheckResult testPost1(int x, String m) {
        HttpResponse response = post("/setAppointment", m).send();

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
        String results = m.replaceAll("[\"}{]*", "");
        String arr[] = results.split("[:,]");
        Arrays.stream(arr).forEach(System.out::println);
        if (arr.length < 6) {
            return CheckResult.wrong("Check request parameters. They all should be present");
        }
        System.out.println(arr[1] + "+ " + arr[3] + "+ " + arr[5] + ":" + arr[6]);
        expect(String.valueOf(json)).asJson().check(

                isObject()
                        .value("idApp", isNumber())
                        .value("doctorName", arr[1].trim())
                        .value("patientName", arr[3].trim())
                        .value("date", compile((arr[5] + ":" + arr[6]).trim())

                        ));

        String resToCheck = "\"" +x + "\":{\"idApp\":" +  json.getAsJsonObject().get("idApp").getAsInt() + ",\"doctorName\":\"" + arr[1].trim() + "\",\"patientName\":\"" + arr[3].trim() + "\",\"date\":\"" + (arr[5] + ":" + arr[6]).trim() + "\"}".trim();
        resToCheck1.add(resToCheck);
        StringBuilder toPrint = new StringBuilder();

        for (int i = 0; i < resToCheck1.size(); i++) {
            toPrint.append(resToCheck1.get(i));
            if (i < resToCheck1.size() - 1) {
                toPrint.append(",");
            }
        }


        System.out.println("{" + toPrint + "}");

        response = get("/appointments").send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("GET /appointments should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }


        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("GET /appointments should return a valid JSON");
        }

        System.out.println(response.getContent());
        if (!response.getContent().equals("{" + toPrint + "}")) {

            return CheckResult.wrong("Response with mistake . It is like this:" + response.getContent()
                    + "\n but should be like this: " + "{" + toPrint + "}");
        }
        return CheckResult.correct();
    }


    @DynamicTest(order = 2, data = "array")
    CheckResult testDelete1(int x, String m) {
        HttpResponse response = delete("deleteAppointment?id=" + x).send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("DELETE /deleteAppointment?id=1 should respond with " +
                    "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("DELETE /deleteAppointment?id=1 should return a valid JSON");
        }


        if (response.getContent().contains("The appointment was already cancelled or does not exist!")) {
            return CheckResult.wrong("If DELETE /deleteAppointment?id=1 responded with " +
                    "status code" + response.getStatusCode() + "\n\n" +
                    "Response body:\n should not be: " + response.getContent());
        } else {
            expect(String.valueOf(json)).asJson().check(

                    isObject()
                            .value("idApp", isNumber())
                            .value("doctorName", compile("\\w+\\s*\\w*"))
                            .value("patientName", compile("\\w+\\s*\\w*"))
                            .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))

            );
        }
        System.out.println(response.getContent());

        String results1 = m.replaceAll("[\"}]*", "");
        String results = results1.replace("}", "");

        String arr[] = results.split("[:,]");
        System.out.println(arr[1] + "+ " + arr[3] + "+ " + arr[5] + ":" + arr[6]);
        String resToCheck = "{\"idApp\":" +  json.getAsJsonObject().get("idApp").getAsInt() + ",\"doctorName\":\"" + arr[1].trim() + "\",\"patientName\":\"" + arr[3].trim() + "\",\"date\":\"" + (arr[5] + ":" + arr[6]).trim() + "\"}".trim();


        if (!response.getContent().equals(resToCheck)) {

            return CheckResult.wrong("Response with mistake . It is like this:" + response.getContent()
                    + "\n but should be like this:" + resToCheck);
        }
        return CheckResult.correct();
    }


    int[] iteration = {
            1, 2, 3, 4, 5
    };

    @DynamicTest(order = 3, data = "iteration")
    CheckResult testDelete5(int x) {
        HttpResponse response = delete("deleteAppointment?id=" + resToCheck1.size()).send();


        if (response.getStatusCode() == 409 && response.getContent().contains("The appointment does not exist or one of the fields is null!")) {
            System.out.println(response.getContent());
            return CheckResult.correct();
        } else if (response.getStatusCode() == 400 && response.getContent().contains("The appointment does not exist or was already cancelled")) {
            System.out.println(response.getContent());
            return CheckResult.correct();
        } else if (response.getStatusCode() == 500 && response.getContent().contains("The date field is null!")) {
            System.out.println(response.getContent());
            return CheckResult.correct();
        } else {
            if (response.getStatusCode() != 400) {
                return CheckResult.wrong("DELETE /deleteAppointment?id=" + (resToCheck1.size() - 1 + x) + " should respond with " +
                        "status code 400, responded: " + response.getStatusCode() + "\n\n" +
                        "Response body:\n" + response.getContent());
            }

            JsonElement json;
            try {
                json = response.getJson();
            } catch (Exception ex) {
                return CheckResult.wrong("DELETE /deleteAppointment?id=" + (resToCheck1.size() - 1 + x) + " should return a valid JSON");
            }
            expect(String.valueOf(json)).asJson().check(

                    isObject()
                            .value("idApp", isNumber(+resToCheck1.size()))
                            .value("doctorName", compile("\\w+\\s*\\w*"))
                            .value("patientName", compile("\\w+\\s*\\w*"))
                            .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))

            );
        }
        return CheckResult.correct();
    }

}
