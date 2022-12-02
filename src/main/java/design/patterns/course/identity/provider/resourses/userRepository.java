package design.patterns.course.identity.provider.resourses;

import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;

import com.example.accessingdatamysql.UserInfo;

import javax.print.attribute.IntegerSyntax;

public interface userRepository extends CrudRepository<UserInfo, Integer> {

}
