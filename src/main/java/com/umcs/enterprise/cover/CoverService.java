package com.umcs.enterprise.cover;

import com.cloudinary.Cloudinary;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CoverService {

	@NonNull
	private final CoverRepository coverRepository;

	@NonNull
	private final Cloudinary cloudinary;

	private Cover uploadAndSave(Object multipart) throws IOException {
		UUID uuid = UUID.randomUUID();

		cloudinary.uploader().upload(multipart, Map.of("public_id", uuid.toString()));

		return coverRepository.save(Cover.newBuilder().uuid(uuid.toString()).build());
	}

	public Cover upload(MultipartFile multipart) throws IOException {
		return uploadAndSave(toFile(multipart));
	}

	public Cover upload(String url) throws IOException {
		return uploadAndSave((url));
	}

	private File toFile(MultipartFile multipart) throws IOException {
		File file = new File(Objects.requireNonNull(multipart.getOriginalFilename()));
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(multipart.getBytes());
		fos.close();
		return file;
	}
}
