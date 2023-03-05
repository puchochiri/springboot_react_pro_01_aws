package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

import lombok.extern.slf4j.Slf4j;


//controller 단 component
@RestController
@RequestMapping("todo")
@Slf4j
public class TodoController {
	
	//쿨래스 : 틀 ex) 붕어빵 틀
	//객체    : 클래스의 인스턴스 ex) 붕어빵
	//인스턴스 : 실제로 구현된 구체적인 실체 ex) 각각의 붕어빵
	//자동으로 빈 찾아서 인스턴스 멤버변수에 연결
	@Autowired
	private TodoService service;
	
	
	/*
	 * @GetMapping public String testController() {
	 * 
	 * return "Hello World! testtodo"; }
	 */
	/*
	@GetMapping("/test")
	public ResponseEntity<?> testControllerResponseEntity() {
		String str = service.testService();
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		
		
		return ResponseEntity.ok().body(response);
		
		
	}
	*/
	
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
		log.info("controller createTodo 실행:" + dto);
		
		try {
			// String temporaryUserId = "temporary-user"; // temporary user id.
			
			// (1) TodoEntity로 변환한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			log.info("controller createTodo entity 실행:" + entity);
			
			// (2) id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문이다.
			entity.setId(null);
			
			// (3) 임시 유저 아이디를 설정해준다. 이부분은 4장 인증과 인가에서 수정할 예정
			entity.setUserId(userId);
			
			// (4) 서비스를 이용해 Todo 엔티티를 생성한다.
			List<TodoEntity> entities = service.create(entity);
			log.info("controller createTodo entities 실행:" + entities);
			
			// (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			log.info("controller createTodo dtos 실행:" + dtos);
			
			// (6) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			log.info("controller createTodo ok response 실행:" + response);

			// (7) ResponseDTO를 리턴한다.
			return ResponseEntity.ok().body(response);
			
			
		} catch (Exception e) {
			// (8) 혹시 예외가 나는 경우 dto 대신 error에 메시지를 넣어 리턴한다.
			
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response =ResponseDTO.<TodoDTO>builder().error(error).build();
			// TODO: handle exception
			log.info("controller createTodo bad response 실행:" + response);

			return ResponseEntity.badRequest().body(response);
			
		}
		
	
		
		
		
		
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
		//String  temporaryUserId = "temporary-user"; // temporary user id.
		
		// (1) 서비스 메서드의 retrieve 메서드를 사용해 Todo 리스트를 가져온다
		List<TodoEntity> entities = service.retrieve(userId);
		
		// (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (6) 변환된 TodoDTO 리스트를 이용해 responseDTO를 초기화 한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
	
		// (7) ResponseDTO를 리턴한다.
		return ResponseEntity.ok().body(response);
		
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
		//String temporaryUserId = "temporary-user"; // temporary user id
		
		// (1) dto를 entity로 변환한다.
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// (2) id를 temporaryUserId로 초기화한다. 여기는 4장 인증과 인가에서 수정할 예정이다.
		entity.setUserId(userId);
		
		// (3) 서비스를 이용해 entity를 업데이트한다.
		List<TodoEntity> entities = service.update(entity);
		
		// (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// (6) ResponseDTO를 리턴한다.
		return ResponseEntity.ok().body(response);
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			//String temporaryUserId = "temporary-user"; // temporary user id.
			
			// (1) TodoEntity로 변환한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// (2) 임시 유저 아이디를 설정해준다.
			entity.setUserId(userId);
			
			// (3) 서비스를 이용해 entity를 삭제한다.
			List<TodoEntity> entities = service.delete(entity);
			
			// (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.\
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
			ResponseDTO<TodoDTO> response =ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// (6) ResponseDTO를 리턴한다.
			return ResponseEntity.ok().body(response);
			
			
		} catch (Exception e) {
			// (8) 혹시 예외가 나는 경우 dto 대신 error에 메시지를 넣어 리턴한다.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			// TODO: handle exception
			return ResponseEntity.badRequest().body(response);
		}
		
		
	}

}
