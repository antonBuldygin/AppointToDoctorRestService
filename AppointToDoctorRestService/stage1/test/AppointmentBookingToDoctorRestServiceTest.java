import AppointmentBookingToDoctorRestService.Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.UnexpectedError;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.expect.base.checker.StringChecker;

import static java.util.regex.Pattern.compile;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

public class AppointmentBookingToDoctorRestServiceTest extends SpringTest {
    public AppointmentBookingToDoctorRestServiceTest() {
        super(Main.class, 28852);
    }


    @DynamicTest
    DynamicTesting[] dt = new DynamicTesting[]{
            () -> testPost(),
            () -> testPost(),
            () -> testGet(),
            () -> testDelete(),
            () -> testGet1(),
            () -> testDelete1(),
            () -> testGet2(),
            () -> testDelete1(),
    };

    CheckResult testGet() {
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

        System.out.println(response.getContent());

        expect(String.valueOf(json)).asJson().check(

                isObject()
                        .value("1", isObject()
                                .value("idApp", 1)
                                .value("doctorName", compile("\\w+\\s*\\w*"))
                                .value("patientName", compile("\\w+\\s*\\w*"))
                                .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")))
                        .value("2", isObject()
                                .value("idApp", 2)
                                .value("doctorName", compile("\\w+\\s*\\w*"))
                                .value("patientName", compile("\\w+\\s*\\w*"))
                                .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))
                        ));


        return CheckResult.correct();
    }

    CheckResult testGet1() {
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

        System.out.println(response.getContent());

        expect(String.valueOf(json)).asJson().check(

                isObject()

                        .value("2", isObject()
                                .value("idApp", 2)
                                .value("doctorName", compile("\\w+\\s*\\w*"))
                                .value("patientName", compile("\\w+\\s*\\w*"))
                                .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))
                        ));


        return CheckResult.correct();
    }

    CheckResult testGet2() {
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

        System.out.println(response.getContent());

        if (String.valueOf(json).equals("{}")) {
            return CheckResult.correct();
        } else {
            CheckResult.wrong("GET /appointments should return " +
                    "empty {}, but responded: " + response.getContent());
        }


        return CheckResult.correct();
    }

    private String newUser = """
                    {
                        "idApp": 3,
                        "doctorName": "Phill good",
                        "patientName": "Ay Bolit",
                        "date": "2021-12-01 22:00"
                    
            }""";


    CheckResult testPost() {
        HttpResponse response = post("/setAppointment", newUser).send();

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


        expect(String.valueOf(json)).asJson().check(

                isObject()
                        .value("idApp", isNumber())
                        .value("doctorName", compile("\\w+\\s*\\w*"))
                        .value("patientName", compile("\\w+\\s*\\w*"))
                        .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))

        );


        return CheckResult.correct();
    }

    CheckResult testDelete() {
        HttpResponse response = delete("deleteAppointment?id=1").send();

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

//        if (!json.isJsonArray()) {
//            return CheckResult.wrong("POST /setAppointment should return an array of objects");
//        }

        if (response.getContent().contains("The appointment was already cancelled or does not exist!")) {
            return CheckResult.correct();
        } else {
            expect(String.valueOf(json)).asJson().check(

                    isObject()
                            .value("idApp", isNumber())
                            .value("doctorName", compile("\\w+\\s*\\w*"))
                            .value("patientName", compile("\\w+\\s*\\w*"))
                            .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))

            );
        }


        return CheckResult.correct();
    }

    CheckResult testDelete1() {
        HttpResponse response = delete("deleteAppointment?id=2").send();

        if (response.getStatusCode() != 200 && response.getStatusCode() != 400) {
            return CheckResult.wrong("DELETE /deleteAppointment?id=1 should respond with " +
                    "status code 200 or 400, responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }

        JsonElement json;
        try {
            json = response.getJson();
        } catch (Exception ex) {
            return CheckResult.wrong("DELETE /deleteAppointment?id=1 should return a valid JSON");
        }

//        if (!json.isJsonArray()) {
//            return CheckResult.wrong("POST /setAppointment should return an array of objects");
//        }
        if (response.getContent().contains("The appointment was already cancelled or does not exist!")) {
            return CheckResult.correct();
        } else {
            expect(String.valueOf(json)).asJson().check(

                    isObject()
                            .value("idApp", isNumber(2))
                            .value("doctorName", compile("\\w+\\s*\\w*"))
                            .value("patientName", compile("\\w+\\s*\\w*"))
                            .value("date", compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"))

            );
        }


        return CheckResult.correct();
    }
}
