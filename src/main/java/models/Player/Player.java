package models.Player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import models.Player.Competitive.Competitive;
import models.Player.Endorsement.Endorsements;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", level=" + level +
                ", competitive=" + competitive +
                ", endorsements=" + endorsements +
                ", privacy='" + privacy + '\'' +
                ", overbuffLink='" + overbuffLink + '\'' +
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
        Player player = (Player) o;
        return Objects.equals(username, player.username) && Objects.equals(avatar, player.avatar) && Objects.equals(level, player.level) && Objects.equals(competitive, player.competitive) && Objects.equals(endorsements, player.endorsements) && Objects.equals(privacy, player.privacy) && Objects.equals(overbuffLink, player.overbuffLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, avatar, level, competitive, endorsements, privacy, overbuffLink);
    }
}