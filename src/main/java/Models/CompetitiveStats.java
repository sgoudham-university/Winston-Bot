package Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "eliminationsAvg",
        "damageDoneAvg",
        "deathsAvg",
        "finalBlowsAvg",
        "healingDoneAvg",
        "objectiveKillsAvg",
        "objectiveTimeAvg",
        "soloKillsAvg",
        "games",
        "awards"
})
public class CompetitiveStats {

    @JsonProperty("eliminationsAvg")
    private Double eliminationsAvg;
    @JsonProperty("damageDoneAvg")
    private Integer damageDoneAvg;
    @JsonProperty("deathsAvg")
    private Double deathsAvg;
    @JsonProperty("finalBlowsAvg")
    private Double finalBlowsAvg;
    @JsonProperty("healingDoneAvg")
    private Integer healingDoneAvg;
    @JsonProperty("objectiveKillsAvg")
    private Double objectiveKillsAvg;
    @JsonProperty("objectiveTimeAvg")
    private String objectiveTimeAvg;
    @JsonProperty("soloKillsAvg")
    private Double soloKillsAvg;
    @JsonProperty("games")
    private Games games;
    @JsonProperty("awards")
    private Awards awards;

    @JsonProperty("eliminationsAvg")
    public Double getEliminationsAvg() {
        return eliminationsAvg;
    }

    @JsonProperty("eliminationsAvg")
    public void setEliminationsAvg(Double eliminationsAvg) {
        this.eliminationsAvg = eliminationsAvg;
    }

    @JsonProperty("damageDoneAvg")
    public Integer getDamageDoneAvg() {
        return damageDoneAvg;
    }

    @JsonProperty("damageDoneAvg")
    public void setDamageDoneAvg(Integer damageDoneAvg) {
        this.damageDoneAvg = damageDoneAvg;
    }

    @JsonProperty("deathsAvg")
    public Double getDeathsAvg() {
        return deathsAvg;
    }

    @JsonProperty("deathsAvg")
    public void setDeathsAvg(Double deathsAvg) {
        this.deathsAvg = deathsAvg;
    }

    @JsonProperty("finalBlowsAvg")
    public Double getFinalBlowsAvg() {
        return finalBlowsAvg;
    }

    @JsonProperty("finalBlowsAvg")
    public void setFinalBlowsAvg(Double finalBlowsAvg) {
        this.finalBlowsAvg = finalBlowsAvg;
    }

    @JsonProperty("healingDoneAvg")
    public Integer getHealingDoneAvg() {
        return healingDoneAvg;
    }

    @JsonProperty("healingDoneAvg")
    public void setHealingDoneAvg(Integer healingDoneAvg) {
        this.healingDoneAvg = healingDoneAvg;
    }

    @JsonProperty("objectiveKillsAvg")
    public Double getObjectiveKillsAvg() {
        return objectiveKillsAvg;
    }

    @JsonProperty("objectiveKillsAvg")
    public void setObjectiveKillsAvg(Double objectiveKillsAvg) {
        this.objectiveKillsAvg = objectiveKillsAvg;
    }

    @JsonProperty("objectiveTimeAvg")
    public String getObjectiveTimeAvg() {
        return objectiveTimeAvg;
    }

    @JsonProperty("objectiveTimeAvg")
    public void setObjectiveTimeAvg(String objectiveTimeAvg) {
        this.objectiveTimeAvg = objectiveTimeAvg;
    }

    @JsonProperty("soloKillsAvg")
    public Double getSoloKillsAvg() {
        return soloKillsAvg;
    }

    @JsonProperty("soloKillsAvg")
    public void setSoloKillsAvg(Double soloKillsAvg) {
        this.soloKillsAvg = soloKillsAvg;
    }

    @JsonProperty("games")
    public Games getGames() {
        return games;
    }

    @JsonProperty("games")
    public void setGames(Games games) {
        this.games = games;
    }

    @JsonProperty("awards")
    public Awards getAwards() {
        return awards;
    }

    @JsonProperty("awards")
    public void setAwards(Awards awards) {
        this.awards = awards;
    }

}