package models.Player.Competitive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Damage{" +
                "skillRating=" + skillRating +
                ", rank='" + rank + '\'' +
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
        Damage damage = (Damage) o;
        return Objects.equals(skillRating, damage.skillRating) &&
                Objects.equals(rank, damage.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillRating, rank);
    }
}