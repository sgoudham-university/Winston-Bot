package Models.Player.Achievement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "general",
        "damage",
        "tank",
        "support",
        "maps",
        "special"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Achievements {

    @JsonProperty("general")
    private List<General> general = null;
    @JsonProperty("damage")
    private List<Damage> damage = null;
    @JsonProperty("tank")
    private List<Tank> tank = null;
    @JsonProperty("support")
    private List<Support> support = null;
    @JsonProperty("maps")
    private List<Map> maps = null;
    @JsonProperty("special")
    private List<Special> special = null;

    @JsonProperty("general")
    public List<General> getGeneral() {
        return general;
    }

    @JsonProperty("general")
    public void setGeneral(List<General> general) {
        this.general = general;
    }

    @JsonProperty("damage")
    public List<Damage> getDamage() {
        return damage;
    }

    @JsonProperty("damage")
    public void setDamage(List<Damage> damage) {
        this.damage = damage;
    }

    @JsonProperty("tank")
    public List<Tank> getTank() {
        return tank;
    }

    @JsonProperty("tank")
    public void setTank(List<Tank> tank) {
        this.tank = tank;
    }

    @JsonProperty("support")
    public List<Support> getSupport() {
        return support;
    }

    @JsonProperty("support")
    public void setSupport(List<Support> support) {
        this.support = support;
    }

    @JsonProperty("maps")
    public List<Map> getMaps() {
        return maps;
    }

    @JsonProperty("maps")
    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }

    @JsonProperty("special")
    public List<Special> getSpecial() {
        return special;
    }

    @JsonProperty("special")
    public void setSpecial(List<Special> special) {
        this.special = special;
    }

}