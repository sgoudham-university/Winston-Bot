package Models.Player.Endorsement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "level",
        "frame",
        "distribution"
})
public class Endorsements {

    @JsonProperty("level")
    private Integer level;
    @JsonProperty("frame")
    private String frame;
    @JsonProperty("distribution")
    private Distribution distribution;

    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    @JsonProperty("frame")
    public String getFrame() {
        return frame;
    }

    @JsonProperty("frame")
    public void setFrame(String frame) {
        this.frame = frame;
    }

    @JsonProperty("distribution")
    public Distribution getDistribution() {
        return distribution;
    }

    @JsonProperty("distribution")
    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

}
