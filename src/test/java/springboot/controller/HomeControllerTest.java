package springboot.controller;

import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.Introduction;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.model.request.CreateTodoRequest;
import springboot.service.TodoService;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Introduction.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

    @MockBean
    private TodoService todoService;

    @LocalServerPort
    private int serverPort;

    private static final String NAME = "Ngumbahkathok";
    private static final TodoPriority PRIORITY = TodoPriority.HIGH;
    private static final String TODO = "{\"code\":200,\"message\":null," +
            "\"value\":[{\"name\":\"Ngumbahkathok\",\"priority\":\"HIGH\"}]}";

    @Test
    public void all() {

        when(todoService.getAll()).thenReturn(Arrays.asList(new Todo(NAME, PRIORITY)));

        given()
                .contentType("application/json")
                .when()
                .port(serverPort)
                .get("/todos")
                .then()
                .body(equalTo(TODO))
                .statusCode(200);

        verify(todoService).getAll();
    }

    @Test
    public void insert() {

        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setName(NAME);
        createTodoRequest.setPriority(PRIORITY);

        BDDMockito.when(todoService.saveTodo(createTodoRequest.getName(), createTodoRequest.getPriority()))
                .thenReturn(true);

        RestAssured
                .given()
                .contentType("application/json")
                .body(createTodoRequest)
                .when()
                .port(serverPort)
                .post("/todos");

        Mockito.verify(todoService).saveTodo(NAME, PRIORITY);
    }

    @After
    public void tearDown() throws Exception {

        verifyNoMoreInteractions(todoService);
    }
}
