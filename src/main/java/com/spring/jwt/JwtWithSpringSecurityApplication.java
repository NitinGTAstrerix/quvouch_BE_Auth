package com.spring.jwt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.security.Security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootApplication
@EnableScheduling
@EnableMethodSecurity(

		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
)
@OpenAPIDefinition(
		info = @Info(
				title = "Quvouch",
				description = "QR-Driven Review Management System",
=======
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
@OpenAPIDefinition(
		info = @Info(
				title = " Quvouch",
				description = " QR-Driven Review Management System" ,

				version = "v1",
				contact = @Contact(
						name = "A",
						email = "example@gmail.com"
				),
<<<<<<< HEAD
				license = @License(name = "Apache 2.0")
=======
				license = @License(
						name = "Apache 2.0"
				)
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
		)
)
public class JwtWithSpringSecurityApplication {

	public static void main(String[] args) {
<<<<<<< HEAD

		// Security provider for JWT encryption
		Security.addProvider(new BouncyCastleProvider());

		SpringApplication.run(JwtWithSpringSecurityApplication.class, args);

		System.out.println("======================================");
		System.out.println(" Application Started Successfully ");
		System.out.println(" Swagger: http://localhost:8080/swagger-ui/index.html");
		System.out.println("======================================");
=======
		Security.addProvider(new BouncyCastleProvider());

		SpringApplication.run(JwtWithSpringSecurityApplication.class, args);
		System.err.println("  *****    *******  *******       *****   *******    *****    ******   *******" );
		System.err.println(" *     *   *      *    *         *           *      *     *   *     *     *   " );
		System.err.println("*       *  *      *    *         *           *     *       *  *     *     *   " );
		System.err.println("*       *  *******     *          *****      *     *       *  ******      *   " );
		System.err.println("*********  *           *               *     *     *********  *   *       *   " );
		System.err.println("*       *  *           *               *     *     *       *  *    *      *   " );
		System.err.println("*       *  *        *******       *****      *     *       *  *     *     *   " );

		System.err.println("PORT : localhost8080");
		System.err.println("documentation : "+"http://localhost:8080/swagger-ui/index.html#/   ok");
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
	}
}
