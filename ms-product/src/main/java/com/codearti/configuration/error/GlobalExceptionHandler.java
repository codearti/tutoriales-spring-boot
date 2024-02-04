package com.codearti.configuration.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.codearti.model.dto.ErrorResponseDto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private ObjectMapper mapper = new ObjectMapper();

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponseDto> handleConflict(Exception ex, WebRequest request) {
		log.error("Exception " + ex.getMessage(), ex);
		var error = new ErrorResponseDto(ex.getMessage(), "" + HttpStatus.INTERNAL_SERVER_ERROR.value(),
				LocalDateTime.now());
		return ResponseEntity.internalServerError().body(error);// ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	protected ResponseEntity<ErrorResponseDto> handleConflict(HttpClientErrorException ex, WebRequest request)
			throws JsonProcessingException {
		log.error("HttpClientErrorException " + ex.getMessage(), ex);
		var json = convertToJson(ex.getMessage());
		var error = new ErrorResponseDto(json.get("message").asText(), "" + ex.getRawStatusCode(), LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getRawStatusCode()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<ErrorResponseDto> handleConflict(ResourceNotFoundException ex, WebRequest request) {
		log.error("ResourceNotFoundException " + ex.getMessage(), ex);
		var error = new ErrorResponseDto(ex.getMessage(), "" + ex.getHttpStatus().value(), LocalDateTime.now());
		return new ResponseEntity<>(error, ex.getHttpStatus());
	}

	private JsonNode convertToJson(String message) throws JsonProcessingException {
		var messageTmp = message.split(": \"")[1];
		messageTmp = messageTmp.substring(0, messageTmp.length() - 1);
		return mapper.readTree(messageTmp);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("handleMethodArgumentNotValid " + ex.getMessage(), ex);
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach((error) -> {
			String field = error.getField();
			//field = field.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
			field = ((SnakeCaseStrategy)PropertyNamingStrategies.SNAKE_CASE).translate(field);
			String message = error.getDefaultMessage();
			errors.put(field, message);
		});
		var error = new ErrorResponseDto("Validation failed for argument", "" + HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), errors);
		return ResponseEntity.badRequest().body(error);//new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
	}

}