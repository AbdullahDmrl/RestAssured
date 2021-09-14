package Basqar.Model;

public class City {
   private String id;
   private String countryId;
   private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", countryId='" + countryId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
