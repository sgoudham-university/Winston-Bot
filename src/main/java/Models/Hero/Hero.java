package Models.Hero;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "role",
        "difficulty",
        "description",
        "abilities",
        "story"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hero {

    @JsonProperty("name")
    private String name;
    @JsonProperty("role")
    private String role;
    @JsonProperty("difficulty")
    private Integer difficulty;
    @JsonProperty("description")
    private String description;
    @JsonProperty("abilities")
    private List<Ability> abilities = null;
    @JsonProperty("story")
    private Story story;
    private String portraitUrl;

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("difficulty")
    public Integer getDifficulty() {
        return difficulty;
    }

    @JsonProperty("difficulty")
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("abilities")
    public List<Ability> getAbilities() {
        return abilities;
    }

    @JsonProperty("abilities")
    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    @JsonProperty("story")
    public Story getStory() {
        return story;
    }

    @JsonProperty("story")
    public void setStory(Story story) {
        this.story = story;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", difficulty=" + difficulty +
                ", description='" + description + '\'' +
                ", abilities=" + abilities +
                ", story=" + story +
                ", portraitUrl='" + portraitUrl + '\'' +
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
        Hero hero = (Hero) o;
        return Objects.equals(name, hero.name) &&
                Objects.equals(role, hero.role) &&
                Objects.equals(difficulty, hero.difficulty) &&
                Objects.equals(description, hero.description) &&
                Objects.equals(abilities, hero.abilities) &&
                Objects.equals(story, hero.story) &&
                Objects.equals(portraitUrl, hero.portraitUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role, difficulty, description, abilities, story, portraitUrl);
    }
}