package Models.Player.Endorsement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Endorsements{" +
                "level=" + level +
                ", frame='" + frame + '\'' +
                ", distribution=" + distribution +
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
        Endorsements that = (Endorsements) o;
        return Objects.equals(level, that.level) &&
                Objects.equals(frame, that.frame) &&
                Objects.equals(distribution, that.distribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, frame, distribution);
    }
}
