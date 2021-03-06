package pl.tss.restbox.core.domain.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Country.
 *
 * @author TSS
 */
@Entity
@Table(name = "country")
public class Country implements Serializable {

  private static final long serialVersionUID = -4216714032073933789L;

  private int couId;
  private String name;
  private boolean act;
  private OffsetDateTime modifyDate;

  private List<Movie> movies;

  private Country() {
  }

  public Country(String name) {
    this.name = name;
    this.act = true;
    this.modifyDate = OffsetDateTime.now();

    this.movies = new ArrayList<>();
  }

  @Id
  @Column(name = "cou_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int getCouId() {
    return couId;
  }

  public void setCouId(int couId) {
    this.couId = couId;
  }

  @Basic
  @Column(name = "name", nullable = false, unique = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

    Country country = (Country) obj;

    return couId == country.couId && Objects.equals(name, country.name) && act == country.act
        && Objects.equals(modifyDate, country.modifyDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(couId, name, act, modifyDate);
  }

  @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
  public List<Movie> getMovies() {
    return movies;
  }

  public void setMovies(List<Movie> movies) {
    this.movies = movies;
  }

}
