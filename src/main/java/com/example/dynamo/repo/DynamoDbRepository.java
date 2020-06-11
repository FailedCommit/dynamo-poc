package com.example.dynamo.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.example.dynamo.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class DynamoDbRepository {

	@Autowired
	private DynamoDBMapper mapper;

	public void insertIntoDynamoDB(Student student) {
		mapper.save(student);
	}

	public Student getOneStudentDetails(String studentId, String lastName) {
		return mapper.load(Student.class, studentId, lastName);
	}

	public void updateStudentDetails(Student student) {
		try {
			mapper.save(student, buildDynamoDBSaveExpression(student));
		} catch (ConditionalCheckFailedException exception) {
			log.error("invalid data - " + exception.getMessage());
		}
	}

	public void deleteStudentDetails(Student student) {
		mapper.delete(student);
	}

	public DynamoDBSaveExpression buildDynamoDBSaveExpression(Student student) {
		DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
		Map<String, ExpectedAttributeValue> expected = new HashMap<>();
		expected.put("studentId", new ExpectedAttributeValue(new AttributeValue(student.getStudentId()))
				.withComparisonOperator(ComparisonOperator.EQ));
		saveExpression.setExpected(expected);
		return saveExpression;
	}
}