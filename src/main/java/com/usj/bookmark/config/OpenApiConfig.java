package com.usj.bookmark.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "Library Management API",
		description = "REST endpoints for managing books, members, circulation, reservations, and fines.",
		version = "1.0.0",
		contact = @Contact(name = "Library Ops", email = "ops@example.com")
	),
	servers = {
		@Server(url = "/", description = "Default server")
	},
	tags = {
		@Tag(name = "Books", description = "Catalog management"),
		@Tag(name = "Members", description = "Member registry"),
		@Tag(name = "Loans", description = "Loan lifecycle"),
		@Tag(name = "Reservations", description = "Holds queue"),
		@Tag(name = "Fines", description = "Fine settlement"),
		@Tag(name = "Dashboard", description = "Operational summary")
	}
)
public class OpenApiConfig {
}
