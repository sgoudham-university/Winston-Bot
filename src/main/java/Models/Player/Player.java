package Models.Player;

import Models.Player.Achievement.Achievements;
import Models.Player.Competitive.Competitive;
import Models.Player.Endorsement.Endorsements;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "avatar",
        "level",
        "competitive",
        "endorsement",
        "privacy"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

    @JsonProperty("username")
    private String username;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("level")
    private Level level;
    @JsonProperty("competitive")
    private Competitive competitive;
    @JsonProperty("endorsement")
    private Endorsements endorsements;
    @JsonProperty("privacy")
    private String privacy;

    private String overbuffLink;
    private Achievements achievements;

    public Achievements getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
    }

    public String getOverbuffLink() {
        return overbuffLink;
    }

    public void setOverbuffLink(String overbuffLink) {
        this.overbuffLink = overbuffLink;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("level")
    public Level getLevel() {
        return level;
    }

    @JsonProperty("level")
    public void setLevel(Level level) {
        this.level = level;
    }

    @JsonProperty("competitive")
    public Competitive getCompetitive() {
        return competitive;
    }

    @JsonProperty("competitive")
    public void setCompetitive(Competitive competitive) {
        this.competitive = competitive;
    }

    @JsonProperty("endorsement")
    public Endorsements getEndorsement() {
        return endorsements;
    }

    @JsonProperty("endorsement")
    public void setEndorsement(Endorsements endorsements) {
        this.endorsements = endorsements;
    }

    @JsonProperty("privacy")
    public String getPrivacy() {
        return privacy;
    }

    @JsonProperty("privacy")
    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

}