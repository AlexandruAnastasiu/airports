package ro.siit.airports.model;

public class AirportSearch {

    private String iataOrIcao;

    private String name;

    public String getIataOrIcao() {
        return iataOrIcao;
    }

    public void setIataOrIcao(String iataOrIcao) {
        this.iataOrIcao = iataOrIcao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasIataOrIcao() {
        return iataOrIcao != null && !iataOrIcao.isEmpty();
    }

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }
}
