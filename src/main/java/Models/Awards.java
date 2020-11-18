package Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cards",
        "medals",
        "medalsBronze",
        "medalsSilver",
        "medalsGold"
})

public class Awards {

    @JsonProperty("cards")
    private int cards;
    @JsonProperty("medals")
    private int medals;
    @JsonProperty("medalsBronze")
    private int medalsBronze;
    @JsonProperty("medalsSilver")
    private int medalsSilver;
    @JsonProperty("medalsGold")
    private int medalsGold;

    @JsonProperty("cards")
    public int getCards() {
        return cards;
    }

    @JsonProperty("cards")
    public void setCards(int cards) {
        this.cards = cards;
    }

    @JsonProperty("medals")
    public int getMedals() {
        return medals;
    }

    @JsonProperty("medals")
    public void setMedals(int medals) {
        this.medals = medals;
    }

    @JsonProperty("medalsBronze")
    public int getMedalsBronze() {
        return medalsBronze;
    }

    @JsonProperty("medalsBronze")
    public void setMedalsBronze(int medalsBronze) {
        this.medalsBronze = medalsBronze;
    }

    @JsonProperty("medalsSilver")
    public int getMedalsSilver() {
        return medalsSilver;
    }

    @JsonProperty("medalsSilver")
    public void setMedalsSilver(int medalsSilver) {
        this.medalsSilver = medalsSilver;
    }

    @JsonProperty("medalsGold")
    public int getMedalsGold() {
        return medalsGold;
    }

    @JsonProperty("medalsGold")
    public void setMedalsGold(int medalsGold) {
        this.medalsGold = medalsGold;
    }

}