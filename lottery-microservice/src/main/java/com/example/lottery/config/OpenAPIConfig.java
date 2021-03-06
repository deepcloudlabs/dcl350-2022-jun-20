package com.example.lottery.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@SecurityScheme(
  name = "Bearer Authentication",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
@OpenAPIDefinition(
		  info =@Info(
		    title = "Lottery API",
		    version = "${api.version}",
		    contact = @Contact(
		      name = "DEEPCLOUDLABS", email = "info@deepcloudlabs.com", url = "https://www.deepcloudlabs.com"
		    ),
		    license = @License(
		      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
		    ),
		    termsOfService = "${tos.uri}",
		    description = "${api.description}"
		  ),
		  servers = @Server(
		    url = "${api.server.url}",
		    description = "Production"
		  )
		)
public class OpenAPIConfig {

}
