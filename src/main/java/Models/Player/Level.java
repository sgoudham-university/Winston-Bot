package Models.Player;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Level{" +
                "value=" + value +
                ", border='" + border + '\'' +
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
        Level level = (Level) o;
        return Objects.equals(value, level.value) &&
                Objects.equals(border, level.border) &&
                Objects.equals(rank, level.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, border, rank);
    }
}
