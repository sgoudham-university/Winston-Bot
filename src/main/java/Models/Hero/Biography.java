package Models.Hero;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "realName",
        "age",
        "occupation",
        "baseOfOperations",
        "affiliation"
})

public class Biography {

    @JsonProperty("realName")
    private String realName;
    @JsonProperty("age")
    private String age;
    @JsonProperty("occupation")
    private String occupation;
    @JsonProperty("baseOfOperations")
    private String baseOfOperations;
    @JsonProperty("affiliation")
    private String affiliation;

    @JsonProperty("realName")
    public String getRealName() {
        return realName;
    }

    @JsonProperty("realName")
    public void setRealName(String realName) {
        this.realName = realName;
    }

    @JsonProperty("age")
    public String getAge() {
        return age;
    }

    @JsonProperty("age")
    public void setAge(String age) {
        this.age = age;
    }

    @JsonProperty("occupation")
    public String getOccupation() {
        return occupation;
    }

    @JsonProperty("occupation")
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @JsonProperty("baseOfOperations")
    public String getBaseOfOperations() {
        return baseOfOperations;
    }

    @JsonProperty("baseOfOperations")
    public void setBaseOfOperations(String baseOfOperations) {
        this.baseOfOperations = baseOfOperations;
    }

    @JsonProperty("affiliation")
    public String getAffiliation() {
        return affiliation;
    }

    @JsonProperty("affiliation")
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

}