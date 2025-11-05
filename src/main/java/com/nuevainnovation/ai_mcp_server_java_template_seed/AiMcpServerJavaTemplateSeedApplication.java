package com.nuevainnovation.ai_mcp_server_java_template_seed;

import com.nuevainnovation.ai_mcp_server_java_template_seed.service.ContactContextService; // 1. Import new service
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiMcpServerJavaTemplateSeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiMcpServerJavaTemplateSeedApplication.class, args);
	}

	@Bean // 2. Register the ContactContextService bean as the ToolCallbackProvider
	public ToolCallbackProvider contactTools(ContactContextService contactService) {
		return MethodToolCallbackProvider.builder().toolObjects(contactService).build(); // Registers contact tools
	}
}