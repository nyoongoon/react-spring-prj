package com.example.spring_react.service;

import com.example.spring_react.model.TodoEntity;
import com.example.spring_react.persistence.TodoRepository;
import com.sun.tools.javac.comp.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
