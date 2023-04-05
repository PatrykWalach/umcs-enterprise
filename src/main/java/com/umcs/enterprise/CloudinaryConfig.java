package com.umcs.enterprise;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CloudinaryConfig {

	@Value("${cloudinary.cloud_name}")
	private String cloud_name;

	@Value("${cloudinary.api_key}")
	private String api_key;

	@Value("${cloudinary.api_secret}")
	private String api_secret;

	@Bean
	public Cloudinary get() {
		Map<String, String> config = new HashMap<String, String>();
		config.put("cloud_name", cloud_name);
		config.put("api_key", api_key);
		config.put("api_secret", api_secret);
		return new Cloudinary(config);
	}
}
