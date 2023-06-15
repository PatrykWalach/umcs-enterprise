package com.umcs.enterprise.cover;

import com.cloudinary.Cloudinary;
import java.io.*;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoverService {

	@NonNull
	private final Cloudinary cloudinary;

	private Cover uploadAndSave(Object multipart) throws IOException {
		UUID uuid = UUID.randomUUID();

		cloudinary.uploader().uploadLarge(multipart, Map.of("public_id", uuid.toString()));

		return (Cover.newBuilder().uuid(uuid.toString()).build());
	}

	public Cover upload(InputStream multipart) throws IOException {
		return uploadAndSave((multipart));
	}

	public Cover upload(String url) throws IOException {
		return uploadAndSave((url));
	}
}
