package design.patterns.course.identity.provider.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Setter(AccessLevel.NONE)
  @JsonIgnore
  private Long id;
  private String email;
  private String username;
  @JsonIgnore
  private String password;
  @JsonIgnore
  private String salt;
  private String description;
  private LocalDateTime createdAt;
  @Lob
  private byte[] picture;
  @OneToMany(mappedBy = "user")
  private List<Role> roles;

  public boolean hasRole(String roleName) {
    for (Role role : roles) {
      if (role.getName().equals(roleName)) {
        return true;
      }
    }

    return false;
  }
}
