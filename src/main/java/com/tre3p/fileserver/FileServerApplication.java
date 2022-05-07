package com.tre3p.fileserver;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
public class FileServerApplication extends SpringBootServletInitializer
		implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(FileServerApplication.class, args);
	}

}
