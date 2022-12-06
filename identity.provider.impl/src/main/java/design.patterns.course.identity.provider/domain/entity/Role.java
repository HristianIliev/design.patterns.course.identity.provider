package design.patterns.course.identity.provider.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Role {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Setter(AccessLevel.NONE)
  @JsonIgnore
  private Long id;
  @ManyToOne
  @JsonIgnore
  private User user;
  private String name;

}
