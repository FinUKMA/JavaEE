package kma.topic6.springdatasample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@DatabaseSetup("/UserService/init.xml")
@DatabaseTearDown("/clean-up.xml")
class UserServiceTest extends AbstractTest {

    @Autowired
    private UserService service;

//    @Test
    @Sql(value = "/UserService/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql("/UserService/init.sql")
    void shouldSelectUserById() {
        assertThat(service.getUserById(1))
            .returns(1, UserEntity::getId)
            .returns("email1@example.com", UserEntity::getEmail);
    }

//    @Test
    @Sql("/UserService/init.sql")
    @Sql(value = "/UserService/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldSelectAllUsers() {
        assertThat(service.findAllUsers())
            .hasSize(3);
    }

//    @Test
    @Sql("/UserService/init.sql")
    @Sql(value = "/UserService/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldCreateUser() {
        service.createUser("new-user", "new-user", "email4@example.com");

        assertThat(service.countUsers()).isEqualTo(4L);
    }

    @Test
    void shouldSelectUserById_dbunit() {
        assertThat(service.getUserById(1))
            .returns(1, UserEntity::getId)
            .returns("email123@dummy.com", UserEntity::getEmail);
    }

    @Test
    void shouldSelectAllUsers_dbunit() {
        assertThat(service.findAllUsers())
            .hasSize(3);
    }

    @Test
    @ExpectedDatabase(value = "/UserService/expectedUsersAfterCreateNew.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void shouldCreateUser_dbunit() {
        service.createUser("new-user", "new-user", "email4@example.com");
    }

    @Test
    @ExpectedDatabase(value = "/UserService/expectedUsersAfterCreateNew.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void shouldCreateUser_dbunit1() {
        service.createUser("new-user", "new-user", "email4@example.com");
    }


}
