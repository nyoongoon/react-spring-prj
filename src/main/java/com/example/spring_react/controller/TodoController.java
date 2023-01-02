package com.example.spring_react.controller;

import com.example.spring_react.dto.ResponseDTO;
import com.example.spring_react.dto.TodoDTO;
import com.example.spring_react.model.TodoEntity;
import com.example.spring_react.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    // testTodo 생략
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
        try{
            String temporaryUserId = "temporary-user";
            // 1) TodoEntity로 변환하기
            TodoEntity entity = TodoDTO.toEntity(dto);
            // 2) id를 null로 초기화.
            entity.setId(null);
            // 3) 임시 유저 아이디를 설정.
            entity.setUserId(temporaryUserId);
            // 4) 서비스를 이용해 Todo엔티티 생성
            List<TodoEntity> entities = service.create(entity);
            // 5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDto리스트로 변환.   //
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 6) 변환된 TodoDTO 리스트를 이용해 ResopnseDTO를 초기화 한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            // 7) ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            // 8) 예외가 생긴 경우 dto 대신 error에 메시지를 넣어 리턴.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(){
        String temporaryUserId = "temporary-user"; //temporary user id

        // 1) 서비스 메서드의 retrieve 메서드를 사용해 Todo리스트를 가져온다.
        List<TodoEntity> entities = service.retrieve(temporaryUserId);

        // 2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 3) 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // 4) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto){
        String temporaryUserId = "temporary-user";
        // 1) dto를 entity로 변환한다.
        TodoEntity entity = TodoDTO.toEntity(dto);
        // 2) id를 temporaryUserId로 초기화
        entity.setUserId(temporaryUserId);
        // 3) 서비스를 이용해 entity를 업데이트한다.
        List<TodoEntity> entities = service.update(entity);
        // 4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        // 5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        // 6) Response DTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto){
        try{
            String temporaryUserId = "temporary-user";
            // 1) TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);
            // 2) 임시 유저 아이디를 설정해준다.
            entity.setUserId(temporaryUserId);
            // 3) 서비스를 이용해 entity를 삭제한다.
            List<TodoEntity> entities = service.delete(entity);
            // 4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            // 6) ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            // 8) 혹시 예외가 나눈 경우 dto 대신 error에 메시지를 넣어 리턴.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
