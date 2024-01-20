package com.projects.productscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.projects.productscatalog.dto.UserInsertDTO;
import com.projects.productscatalog.entities.User;
import com.projects.productscatalog.repositories.UserRepository;
import com.projects.productscatalog.resources.exception.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Autowired
	private UserRepository repository;

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		Optional<User> user = repository.findByEmail(dto.getEmail());
		if (user.isPresent()) {
			// Usuário encontrado
			list.add(new FieldMessage("email", "Email já existe"));
			
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}