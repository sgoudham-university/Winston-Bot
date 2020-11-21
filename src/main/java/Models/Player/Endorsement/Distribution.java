package Models.Player.Endorsement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "shotcaller",
        "teammate",
        "sportsmanship"
})
public class Distribution {

    @JsonProperty("shotcaller")
    private Double shotcaller;
    @JsonProperty("teammate")
    private Double teammate;
    @JsonProperty("sportsmanship")
    private Double sportsmanship;

    @JsonProperty("shotcaller")
    public Double getShotcaller() {
        return shotcaller;
    }

    @JsonProperty("shotcaller")
    public void setShotcaller(Double shotcaller) {
        this.shotcaller = shotcaller;
    }

    @JsonProperty("teammate")
    public Double getTeammate() {
        return teammate;
    }

    @JsonProperty("teammate")
    public void setTeammate(Double teammate) {
        this.teammate = teammate;
    }

    @JsonProperty("sportsmanship")
    public Double getSportsmanship() {
        return sportsmanship;
    }

    @JsonProperty("sportsmanship")
    public void setSportsmanship(Double sportsmanship) {
        this.sportsmanship = sportsmanship;
    }

}
