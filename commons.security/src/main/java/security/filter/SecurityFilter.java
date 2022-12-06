package security.filter;

import com.google.gson.Gson;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import security.annotation.RequiresProtection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

@Component
@Order(1)
public class SecurityFilter implements HandlerInterceptor {

  @Override
  public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
    System.out.println("In security filter.");

    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();

    RequiresProtection annotation = method.getAnnotation(RequiresProtection.class);
    if (annotation == null) {
      return true;
    }

    String accessToken = extractBearer(request);
    if (accessToken == null) {
      sendResponseWithMessage(response, HttpStatus.UNAUTHORIZED.value(), "There is no provided bearer token.");

      return false;
    }

    try {
      AccessTokenInformation token = fetchTokenInformation(accessToken);

      String[] requiredRoles = annotation.roles();
      if (requiredRoles.length != 0 && requiredRoles[0].isEmpty() == false && tokenRolesContainRoles(requiredRoles, token) == false) {
        sendResponseWithMessage(response, HttpStatus.FORBIDDEN.value(), "Token does not have required roles.");

        return false;
      }

      String userIdentifierFrom = annotation.userIdentifierFrom();
      if (userIdentifierFrom.equals("path")) {
        String userIdentifier = extractPathParamFromRequest(annotation.userIdentifierParamName(), request);

        if (userIdentifier.equals(token.getUserId()) == false) {
          sendResponseWithMessage(response, HttpStatus.FORBIDDEN.value(), "Token is for different user.");

          return false;
        }
      } else if (userIdentifierFrom.equals("system")) {
        if (token.getUserId().equals("system") == false) {
          sendResponseWithMessage(response, HttpStatus.FORBIDDEN.value(), "Token is for wrong user.");

          return false;
        }
      } else {
        sendResponseWithMessage(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unrecognised user identifier extraction method.");

        return false;
      }
    } catch (Exception exception) {
      System.out.println("Exception message: " + exception.toString());
      exception.printStackTrace();

      sendResponseWithMessage(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not validate access token.");

      return false;
    }

    return true;
  }

  private void sendResponseWithMessage(HttpServletResponse httpResponse, int statusCode, String message) throws IOException {
    httpResponse.resetBuffer();

    httpResponse.setStatus(statusCode);
    httpResponse.setHeader("Content-Type", "application/json");

    httpResponse.getOutputStream().print("{\"description\":\"" + message + "\"}");

    httpResponse.flushBuffer();
  }

  private String extractPathParamFromRequest(String param, HttpServletRequest request) {
    String pathInfo = request.getRequestURI();

    StringTokenizer tokenizer = new StringTokenizer(pathInfo, "/");
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      if (token.equals(param)) {
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }

        return null;
      }
    }

    return null;
  }

  private boolean tokenRolesContainRoles(String[] requiredRoles, AccessTokenInformation token) {
    List<String> tokenRoles = token.getRoles();

    for (int i = 0; i < requiredRoles.length; i++) {
      if (tokenRoles.contains(requiredRoles[i]) == false) {
        return false;
      }
    }

    return true;
  }

  private AccessTokenInformation fetchTokenInformation(String accessToken) throws URISyntaxException, IOException, InterruptedException {
    //REAL IMPLEMENTATION

    //    HttpRequest request = HttpRequest.newBuilder()
//        .uri(new URI("https://<hostofauthorizationserver>/api/v1/tokens"))
//        .header("Authorization", "Bearer " + accessToken)
//        .GET()
//        .build();
//
//    HttpClient client = HttpClient.newHttpClient();
//
//    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//    String responseBody = response.body();
//
//    return toToken(responseBody);

    // DUMMY IMPLEMENTATION
    String json = "{\n" +
        "    \"userId\": \"testUsername\",\n" +
        "    \"roles\": [\"Administrator\"]   \n" +
        "}";

    return toToken(json);
  }

  private AccessTokenInformation toToken(String json) {
    Gson gson = new Gson();

    return gson.fromJson(json, AccessTokenInformation.class);
  }

  private String extractBearer(HttpServletRequest request) {
    Enumeration<String> headerNames = request.getHeaderNames();
    List<String> headers = Collections.list(headerNames);
    if (headers.contains("Authorization") == false && headers.contains("authorization") == false) {
      return null;
    }

    String authorizationHeaderValue = request.getHeader("Authorization");
    if (authorizationHeaderValue == null || authorizationHeaderValue.trim().isEmpty()) {
      return null;
    }

    if (authorizationHeaderValue.startsWith("Bearer ") == false) {
      return null;
    }

    return stripBearer(authorizationHeaderValue);
  }

  private String stripBearer(String accessToken) {
    return accessToken.replace("Bearer ", "");
  }

}
