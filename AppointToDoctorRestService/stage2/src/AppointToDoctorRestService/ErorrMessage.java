package AppointToDoctorRestService;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErorrMessage {
    String error =  "The appointment was already cancelled or does not exist!";

    public ErorrMessage(String error) {
        this.error = error;
    }

    public ErorrMessage() {

    }


    @JsonProperty("error")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
