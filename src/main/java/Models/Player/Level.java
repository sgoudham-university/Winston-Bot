package Models.Player;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "value",
        "border",
        "rank"
})
public class Level {

    @JsonProperty("value")
    private Integer value;
    @JsonProperty("border")
    private String border;
    @JsonProperty("rank")
    private String rank;

    @JsonProperty("value")
    public Integer getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Integer value) {
        this.value = value;
    }

    @JsonProperty("border")
    public String getBorder() {
        return border;
    }

    @JsonProperty("border")
    public void setBorder(String border) {
        this.border = border;
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
