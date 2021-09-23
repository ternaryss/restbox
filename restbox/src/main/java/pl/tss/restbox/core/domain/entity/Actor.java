package pl.tss.restbox.core.domain.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Movie and person relationship as actor.
 *
 * @author TSS
 */
@Entity
@Table(name = "actor")
public class Actor implements Serializable {

  private static final long serialVersionUID = -3631587772494308528L;

  private int actId;
  private boolean act;
  private OffsetDateTime modifyDate;

  public Actor() {
    this.act = true;
    this.modifyDate = OffsetDateTime.now();
  }

  @Id
  @Column(name = "act_id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int getActId() {
    return actId;
  }

  public void setActId(int actId) {
    this.actId = actId;
  }

  @Basic
  @Column(name = "act", nullable = false)
  public boolean isAct() {
    return act;
  }

  public void setAct(boolean act) {
    this.act = act;
  }

  @Basic
  @Column(name = "modify_date", nullable = false)
  public OffsetDateTime getModifyDate() {
    return modifyDate;
  }

  public void setModifyDate(OffsetDateTime modifyDate) {
    this.modifyDate = modifyDate;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Actor actor = (Actor) obj;

    return actId == actor.actId && act == actor.act && Objects.equals(modifyDate, actor.modifyDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actId, act, modifyDate);
  }

}
