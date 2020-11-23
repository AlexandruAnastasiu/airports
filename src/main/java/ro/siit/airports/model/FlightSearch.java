package ro.siit.airports.model;

public class FlightSearch {

    private String flight_no;

    public String getFlight_no() {
        return flight_no;
    }

    public void setFlight_no(String flight_no) {
        this.flight_no = flight_no;
    }

    public boolean hasFlightNo() {
        return flight_no != null && !flight_no.isEmpty();
    }
}
