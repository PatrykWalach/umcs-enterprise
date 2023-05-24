package com.umcs.enterprise.data;

import com.umcs.enterprise.book.cover.Cover;
import com.umcs.enterprise.book.cover.Covers;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper
public interface CoversMapper {
	static record ResultDto(
		List<BreakpointsDto> responsive_breakpoints,
		String signature,
		String format,
		String resource_type,
		String secure_url,
		String created_at,
		String asset_id,
		String version_id,
		String type,
		String version,
		String url,
		String public_id,
		List<String> tags,
		Boolean existing,
		String folder,
		String api_key,
		Integer bytes,
		Integer width,
		String etag,
		String placeholder,
		Integer height,
		String original_filename
	) {}

	static record BreakpointsDto(List<BreakpointDto> breakpoints, String transformation) {}

	static record BreakpointDto(
		Integer width,
		Integer height,
		String secure_url,
		Integer bytes,
		String url
	) {}

	@Mapping(target = "transformation", source = "transformation")
	@Mapping(target = "covers", source = "breakpoints")
	Covers breakpointsToCovers(BreakpointsDto map);

	@Mapping(target = "width", source = "width")
	@Mapping(target = "height", source = "height")
	@Mapping(target = "url", source = "secure_url")
	Cover breakpointToCover(BreakpointDto map);
}
