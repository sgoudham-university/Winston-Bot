package models.Hero;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "biography",
        "catchPhrase",
        "backStory"
})

public class Story {

    @JsonProperty("biography")
    private Biography biography;
    @JsonProperty("catchPhrase")
    private String catchPhrase;
    @JsonProperty("backStory")
    private String backStory;

    @JsonProperty("biography")
    public Biography getBiography() {
        return biography;
    }

    @JsonProperty("biography")
    public void setBiography(Biography biography) {
        this.biography = biography;
    }

    @JsonProperty("catchPhrase")
    public String getCatchPhrase() {
        return catchPhrase;
    }

    @JsonProperty("catchPhrase")
    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    @JsonProperty("backStory")
    public String getBackStory() {
        return backStory;
    }

    @JsonProperty("backStory")
    public void setBackStory(String backStory) {
        this.backStory = backStory;
    }

    @Override
    public String toString() {
        return "Story{" +
                "biography=" + biography +
                ", catchPhrase='" + catchPhrase + '\'' +
                ", backStory='" + backStory + '\'' +
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
        Story story = (Story) o;
        return Objects.equals(biography, story.biography) &&
                Objects.equals(catchPhrase, story.catchPhrase) &&
                Objects.equals(backStory, story.backStory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(biography, catchPhrase, backStory);
    }
}
