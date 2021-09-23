package pl.tss.restbox.core.domain.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Person treated as movie director or actor.
 *
 * @author TSS
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

  private static final long serialVersionUID = 3274904869981425343L;

  private int perId;
  private String firstName;
  private String secondName;
  private String lastName;
  private OffsetDateTime birthday;
  private int rate;
  private boolean director;
  private boolean act;
  private OffsetDateTime modifyDate;

  private List<Actor> actors;

  private Person() {
  }

  public Person(String firstName, String lastName, OffsetDateTime birthday, int rate, boolean director) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.rate = rate;
    this.director = director;
    this.act = true;
    this.modifyDate = OffsetDateTime.now();

    this.actors = new ArrayList<>();
  }

  @Id
  @Column(name = "per_id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int getPerId() {
    return perId;
  }

  public void setPerId(int perId) {
    this.perId = perId;
  }

  @Basic
  @Column(name = "first_name", nullable = false)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Basic
  @Column(name = "second_name")
  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  @Basic
  @Column(name = "last_name", nullable = false)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Basic
  @Column(name = "birthday", nullable = false)
  public OffsetDateTime getBirthday() {
    return birthday;
  }

  public void setBirthday(OffsetDateTime birthday) {
    this.birthday = birthday;
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
  @Column(name = "director", nullable = false)
  public boolean isDirector() {
    return director;
  }

  public void setDirector(boolean director) {
    this.director = director;
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

    Person person = (Person) obj;

    return perId == person.perId && Objects.equals(firstName, person.firstName)
        && Objects.equals(secondName, person.secondName) && Objects.equals(lastName, person.lastName)
        && Objects.equals(birthday, person.birthday) && rate == person.rate && director == person.director
        && act == person.act && Objects.equals(modifyDate, person.modifyDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(perId, firstName, secondName, lastName, birthday, rate, director, act, modifyDate);
  }

  @Transient
  public int getAge() {
    return Math.abs((int) ChronoUnit.YEARS.between(birthday, OffsetDateTime.now()));
  }

  @OneToMany(mappedBy = "person")
  public List<Actor> getActors() {
    return actors;
  }

  public void setActors(List<Actor> actors) {
    this.actors = actors;
  }

}
