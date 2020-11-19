package Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "icon",
        "name",
        "level",
        "levelIcon",
        "prestige",
        "prestigeIcon",
        "rating",
        "ratingIcon",
        "gamesWon",
        "quickPlayStats",
        "competitiveStats"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

    @JsonProperty("icon")
    private String icon;
    @JsonProperty("name")
    private String name;
    @JsonProperty("level")
    private int level;
    @JsonProperty("levelIcon")
    private String levelIcon;
    @JsonProperty("prestige")
    private int prestige;
    @JsonProperty("prestigeIcon")
    private String prestigeIcon;
    @JsonProperty("rating")
    private String rating;
    @JsonProperty("ratingIcon")
    private String ratingIcon;
    @JsonProperty("gamesWon")
    private int gamesWon;
    @JsonProperty("quickPlayStats")
    private QuickPlayStats quickPlayStats;
    @JsonProperty("competitiveStats")
    private CompetitiveStats competitiveStats;

    @JsonProperty("icon")
    public String getIcon() {
        return icon;
    }

    @JsonProperty("icon")
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("level")
    public int getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(int level) {
        this.level = level;
    }

    @JsonProperty("levelIcon")
    public String getLevelIcon() {
        return levelIcon;
    }

    @JsonProperty("levelIcon")
    public void setLevelIcon(String levelIcon) {
        this.levelIcon = levelIcon;
    }

    @JsonProperty("prestige")
    public int getPrestige() {
        return prestige;
    }

    @JsonProperty("prestige")
    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    @JsonProperty("prestigeIcon")
    public String getPrestigeIcon() {
        return prestigeIcon;
    }

    @JsonProperty("prestigeIcon")
    public void setPrestigeIcon(String prestigeIcon) {
        this.prestigeIcon = prestigeIcon;
    }

    @JsonProperty("rating")
    public String getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(String rating) {
        this.rating = rating;
    }

    @JsonProperty("ratingIcon")
    public String getRatingIcon() {
        return ratingIcon;
    }

    @JsonProperty("ratingIcon")
    public void setRatingIcon(String ratingIcon) {
        this.ratingIcon = ratingIcon;
    }

    @JsonProperty("gamesWon")
    public int getGamesWon() {
        return gamesWon;
    }

    @JsonProperty("gamesWon")
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    @JsonProperty("quickPlayStats")
    public QuickPlayStats getQuickPlayStats() {
        return quickPlayStats;
    }

    @JsonProperty("quickPlayStats")
    public void setQuickPlayStats(QuickPlayStats quickPlayStats) {
        this.quickPlayStats = quickPlayStats;
    }

    @JsonProperty("competitiveStats")
    public CompetitiveStats getCompetitiveStats() {
        return competitiveStats;
    }

    @JsonProperty("competitiveStats")
    public void setCompetitiveStats(CompetitiveStats competitiveStats) {
        this.competitiveStats = competitiveStats;
    }

}