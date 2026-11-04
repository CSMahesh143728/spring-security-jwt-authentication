package com.mahesh.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mahesh.security.model.Student;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentController {

	private List<Student> students = new ArrayList<>(
			List.of(new Student(1, "Mahesh", 40), new Student(2, "Navin", 80)));

	@PostMapping("/students")
	public Student addStudent(@RequestBody Student stu) {
		students.add(stu);
		return stu;
	}

	@GetMapping("/students")
	public List<Student> getStudents() {
		return students;
	}

	@GetMapping("/csrf-token")
	public CsrfToken getCSRFToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}

}
