package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder			// 생성자 클래
@NoArgsConstructor	//	파라미터가 없는 생성자 생
@AllArgsConstructor	// 모든 필드 값을 파라미터로 받는 생성자를 만든
@Data				// @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor 모두를 자동으로 적용
@Entity
@Table(name = "Todo")
public class TodoEntity {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	private String id; // 이 오브젝트의 아이디
	private String userId; // 이 오브젝트를 생성한 유저의 아이디
	private String title; // Todo 타이틀 예) 운동하기
	private	boolean done; // true - todo를 완료한 경우(checked)
	

}

