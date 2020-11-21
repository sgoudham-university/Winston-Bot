package Models.Hero;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

}
