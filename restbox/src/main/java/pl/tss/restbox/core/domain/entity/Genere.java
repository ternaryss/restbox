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
 * Movie genere.
 *
 * @author TSS
 */
@Entity
@Table(name = "genere")
public class Genere implements Serializable {

  private static final long serialVersionUID = 8171917190197239024L;

  private int genId;
  private String name;
  private boolean act;
  private OffsetDateTime modifyDate;

  private List<Movie> movies;

  private Genere() {
  }

  public Genere(String name) {
    this.name = name;
    this.act = true;
    this.modifyDate = OffsetDateTime.now();

    this.movies = new ArrayList<>();
  }

  @Id
  @Column(name = "gen_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int getGenId() {
    return genId;
  }

  public void setGenId(int genId) {
    this.genId = genId;
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

    Genere genere = (Genere) obj;

    return genId == genere.genId && Objects.equals(name, genere.name) && act == genere.act
        && Objects.equals(modifyDate, genere.modifyDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(genId, name, act, modifyDate);
  }

  @OneToMany(mappedBy = "genere", cascade = CascadeType.ALL, orphanRemoval = true)
  public List<Movie> getMovies() {
    return movies;
  }

  public void setMovies(List<Movie> movies) {
    this.movies = movies;
  }

}
