import AppointToDoctorRestService.Main;
import com.google.gson.JsonElement;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.regex.Pattern.compile;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

public class AppointmentBookingToDoctorRestServiceTest extends SpringTest {

    public AppointmentBookingToDoctorRestServiceTest() {
        super(Main.class, 28852);


    }

    private String newUser = """
                    {
                        "doctorName": "Phill good",
                        "patientName": "Ay Bolit",
                        "date": "2021-12-01 22:00"
                    
            }""";

    private String newUser1 = """
                    {
                        "doctorName": "Pamela Upperson",
                        "patientName": "John Galt",
                        "date": "2021-12-01 12:00"
                    
            }""";

    private String newUser2 = """
                    {
                        "doctorName": "Lea Wong",
                        "patientName": "Indi Grimes",
                        "date": "2021-12-01 13:00"
                    
            }""";

    private String errorUser = """
            {
              "doctorName": "null",
              "patientName": "fdf",
              "date": "2021-12-01 23:00"
             }""";

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
        String results = m.replaceAll("[\"}]*", "");

        String arr[] = results.split("[:,]");
        System.out.println(arr[1] + "+ " + arr[3] + "+ " + arr[5] + ":" + arr[6]);
        expect(String.valueOf(json)).asJson().check(

                isObject()
                        .value("idApp", isNumber())
                        .value("doctorName", arr[1].trim())
                        .value("patientName", arr[3].trim())
                        .value("date", compile((arr[5] + ":" + arr[6]).trim())

                        ));

        String resToCheck = "\"" + x + "\":{\"idApp\":" + x + ",\"doctorName\":\"" + arr[1].trim() + "\",\"patientName\":\"" + arr[3].trim() + "\",\"date\":\"" + (arr[5] + ":" + arr[6]).trim() + "\"}".trim();
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
        String resToCheck = "{\"idApp\":" + x + ",\"doctorName\":\"" + arr[1].trim() + "\",\"patientName\":\"" + arr[3].trim() + "\",\"date\":\"" + (arr[5] + ":" + arr[6]).trim() + "\"}".trim();


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
