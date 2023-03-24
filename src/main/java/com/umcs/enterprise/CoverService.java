package com.umcs.enterprise;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CoverService {

	private final CoverRepository coverRepository;

	public Cover uploadCover(MultipartFile file) throws IOException {
		Path uploadDir = Paths.get("static/covers");
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}
		String filename =
			UUID.randomUUID() +
			Objects
				.requireNonNull(file.getOriginalFilename())
				.substring(file.getOriginalFilename().lastIndexOf("."));
		Path newFile = uploadDir.resolve(filename);

		try (OutputStream outputStream = Files.newOutputStream(newFile)) {
			outputStream.write(file.getBytes());
		}
		BufferedImage image = ImageIO.read(newFile.toFile());

		Cover cover = new Cover();
		cover.setFilename(filename);
		cover.setHeight(image.getHeight());
		cover.setWidth(image.getWidth());

		return coverRepository.save(cover);
	}
}
