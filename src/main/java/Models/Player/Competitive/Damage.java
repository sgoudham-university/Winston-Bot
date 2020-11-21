package Models.Player.Competitive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "skillRating",
        "rank"
})
public class Damage {

    @JsonProperty("skillRating")
    private Integer skillRating;
    @JsonProperty("rank")
    private String rank;

    @JsonProperty("skillRating")
    public Integer getSkillRating() {
        return skillRating;
    }

    @JsonProperty("skillRating")
    public void setSkillRating(Integer skillRating) {
        this.skillRating = skillRating;
    }

    @JsonProperty("rank")
    public String getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(String rank) {
        this.rank = rank;
    }

}
