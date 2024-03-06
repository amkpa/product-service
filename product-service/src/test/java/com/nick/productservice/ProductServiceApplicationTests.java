package com.nick.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.productservice.dto.ProductRequest;
import com.nick.productservice.repo.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepo productRepo;
@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
	dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productrequest = getProductrequest();
		String valueAsString = objectMapper.writeValueAsString(productrequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product-service")
			.contentType(MediaType.APPLICATION_JSON)
			.content(valueAsString))
				.andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepo.findAll().size());
	}

	private ProductRequest getProductrequest() {
	return ProductRequest.builder()
			.name("Iphone 13")
			.description("Iphone 13")
			.price(BigDecimal.valueOf(1200))
			.build();
	}

}
