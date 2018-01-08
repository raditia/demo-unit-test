package springboot.service;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class TodoServiceTest {

    private TodoService todoService;
    @Mock
    private TodoRepository todoRepository;

    private static final String NAME = "Ngumbah sepatu";
    private static final TodoPriority PRIORITY = TodoPriority.HIGH;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        todoService = new TodoService(todoRepository);

    }

    @After
    public void tearDown() throws Exception {

        //Memastikan kalau yang dipanggil hanya method getAll() dan tidak dengan fungsi lain
        //Kalau manggil fungsi lain maka error
        Mockito.verifyNoMoreInteractions(todoRepository);

    }

    @Test
    public void saveTodoTest() {

        //given
        BDDMockito.given(todoRepository.store(new Todo(NAME, PRIORITY))).willReturn(true);

        //when
        Boolean insertSuccess = todoService.saveTodo(NAME, PRIORITY);

        //then
        Assert.assertThat(NAME, Matchers.notNullValue());
        Assert.assertThat(PRIORITY, Matchers.notNullValue());
        Assert.assertThat(insertSuccess, Matchers.equalTo(true));

        //verify
        BDDMockito.then(todoRepository).should().store(new Todo(NAME, PRIORITY));
    }

    @Test
    public void getAllTest() {

        Todo todo = new Todo(NAME, PRIORITY);
        List<Todo> todoList = new ArrayList<Todo>();
        todoList.add(todo);

        //given
        BDDMockito.given(todoRepository.getAll()).willReturn(todoList); //Asumsi kalau hasilnya bakal seperti willReturn

        //when
        todoList = todoService.getAll();

        //then
        Assert.assertThat(todoList.isEmpty(), Matchers.equalTo(false)); //Hasilnya tidak boleh kosong
        Assert.assertThat(todoList, Matchers.notNullValue()); //Hasilnya tidak boleh null

        //verify
         BDDMockito.then(todoRepository).should().getAll(); //Memastikan bahwa method getAll() diambil dari todoRepo
    }


}
