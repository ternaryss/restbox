package pl.tss.restbox.core.domain.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Movie.
 *
 * @author TSS
 */
@Entity
@Table(name = "movie")
public class Movie implements Serializable {

  private static final long serialVersionUID = 8062148046084318527L;

  private int movId;
  private String title;
  private String description;
  private OffsetDateTime premiere;
  private int rate;
  private int length;
  private boolean act;
  private OffsetDateTime modifyDate;

  private Person director;
  private Country country;
  private Genere genere;
  private List<Actor> actors;

  private Movie() {
  }

  public Movie(String title, OffsetDateTime premiere, int rate, int length, Person director, Country country,
      Genere genere) {
    this.title = title;
    this.premiere = premiere;
    this.rate = rate;
    this.length = length;
    this.act = true;
    this.modifyDate = OffsetDateTime.now();

    this.director = director;
    this.country = country;
    this.genere = genere;
    this.actors = new ArrayList<>();
  }

  @Id
  @Column(name = "mov_id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int getMovId() {
    return movId;
  }

  public void setMovId(int movId) {
    this.movId = movId;
  }

  @Basic
  @Column(name = "title", nullable = false)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "premiere", nullable = false)
  public OffsetDateTime getPremiere() {
    return premiere;
  }

  public void setPremiere(OffsetDateTime premiere) {
    this.premiere = premiere;
  }

  @Basic
  @Column(name = "rate", nullable = false)
  public int getRate() {
    return rate;
  }

  public void setRate(int rate) {
    this.rate = rate;
  }

  @Basic
  @Column(name = "length", nullable = false)
  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
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

    Movie movie = (Movie) obj;

    return movId == movie.movId && Objects.equals(title, movie.title) && Objects.equals(description, movie.description)
        && Objects.equals(premiere, movie.premiere) && rate == movie.rate && length == movie.length && act == movie.act
        && Objects.equals(modifyDate, movie.modifyDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(movId, title, description, premiere, rate, length, act, modifyDate);
  }

  @ManyToOne
  @JoinColumn(name = "per_per_id", referencedColumnName = "per_id", nullable = false)
  public Person getDirector() {
    return director;
  }

  public void setDirector(Person director) {
    this.director = director;
  }

  @ManyToOne
  @JoinColumn(name = "cou_cou_id", referencedColumnName = "cou_id", nullable = false)
  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  @ManyToOne
  @JoinColumn(name = "gen_gen_id", referencedColumnName = "gen_id", nullable = false)
  public Genere getGenere() {
    return genere;
  }

  public void setGenere(Genere genere) {
    this.genere = genere;
  }

  @OneToMany(mappedBy = "movie")
  public List<Actor> getActors() {
    return actors;
  }

  public void setActors(List<Actor> actors) {
    this.actors = actors;
  }

}
