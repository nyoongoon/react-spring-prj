package com.example.spring_react.service;

import com.example.spring_react.model.TodoEntity;
import com.example.spring_react.persistence.TodoRepository;
import com.sun.tools.javac.comp.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;

    public String testService(){
        //TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        //TodoEntity 저장
        repository.save(entity);
        //TodoEntity 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    // 검증 -> save() -> findByUserId()
    // 검증은 넘어온 엔티티가 유효한지 검사하는 로직. 코드가 더 커지면 Validator 클래스로 분리
    public List<TodoEntity> create(final TodoEntity entity){
        //Validations
        validate(entity);

        repository.save(entity);

        log.info("Entity Id : {} id saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    private void validate(final TodoEntity entity){
        if(entity == null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity){
        // 1) 저장할 엔티티가 유효한지 확인한다.
        validate(entity);

        // 2) 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다. 존재하지 않는 엔티티는 업데이트할 수 없기 때문.
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo ->{
            // 3) 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌윈다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // 4) 데이터베이스에 새 값을 저장한다.
            repository.save(todo);
        });

        return retrieve(entity.getUserId());
    }
}
