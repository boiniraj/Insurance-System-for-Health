package com.nt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class AppConfig
{
	@Bean(name="template")
	public RestTemplate createTemplate()
	{
		return new RestTemplate();
	}

}
