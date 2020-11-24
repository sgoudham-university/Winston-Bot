package Models.Player.Competitive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tank",
        "dps",
        "support"
})
public class Competitive {

    @JsonProperty("tank")
    private Tank tank;
    @JsonProperty("dps")
    private Damage damage;
    @JsonProperty("support")
    private Support support;

    @JsonProperty("tank")
    public Tank getTank() {
        return tank;
    }

    @JsonProperty("tank")
    public void setTank(Tank tank) {
        this.tank = tank;
    }

    @JsonProperty("dps")
    public Damage getDamage() {
        return damage;
    }

    @JsonProperty("dps")
    public void setDamage(Damage damage) {
        this.damage = damage;
    }

    @JsonProperty("support")
    public Support getSupport() {
        return support;
    }

    @JsonProperty("support")
    public void setSupport(Support support) {
        this.support = support;
    }

    @Override
    public String toString() {
        return "Competitive{" +
                "tank=" + tank +
                ", damage=" + damage +
                ", support=" + support +
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
        Competitive that = (Competitive) o;
        return Objects.equals(tank, that.tank) &&
                Objects.equals(damage, that.damage) &&
                Objects.equals(support, that.support);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tank, damage, support);
    }
}
