package Models.Hero;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Biography{" +
                "realName='" + realName + '\'' +
                ", age='" + age + '\'' +
                ", occupation='" + occupation + '\'' +
                ", baseOfOperations='" + baseOfOperations + '\'' +
                ", affiliation='" + affiliation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Biography biography = (Biography) o;
        return Objects.equals(realName, biography.realName) &&
                Objects.equals(age, biography.age) &&
                Objects.equals(occupation, biography.occupation) &&
                Objects.equals(baseOfOperations, biography.baseOfOperations) &&
                Objects.equals(affiliation, biography.affiliation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realName, age, occupation, baseOfOperations, affiliation);
    }
}