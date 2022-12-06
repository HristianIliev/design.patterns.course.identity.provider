package security.filter;

import lombok.Getter;

import java.util.List;

@Getter
public class AccessTokenInformation {

  private String userId;
  private List<String> roles;

}
