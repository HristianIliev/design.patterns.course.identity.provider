package design.patterns.course.identity.provider.domain.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class UserInformation {

  private String email;
  private String username;
  private String password;
  private String description;
  private List<Interest> interests;

}
