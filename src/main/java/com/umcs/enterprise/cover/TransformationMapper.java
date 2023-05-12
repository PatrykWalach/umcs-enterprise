package com.umcs.enterprise.cover;

import com.umcs.enterprise.types.Format;
import org.mapstruct.*;

@Mapper
public interface TransformationMapper {
	@Mapping(target = "zoom", ignore = true)
	@Mapping(target = "y", ignore = true)
	@Mapping(target = "x", ignore = true)
	@Mapping(target = "videoSamplingSeconds", ignore = true)
	@Mapping(target = "videoSamplingFrames", ignore = true)
	@Mapping(target = "videoSampling", ignore = true)
	@Mapping(target = "videoCodec", ignore = true)
	@Mapping(target = "variables", ignore = true)
	@Mapping(target = "underlay", ignore = true)
	@Mapping(target = "streamingProfile", ignore = true)
	@Mapping(target = "startOffsetPercent", ignore = true)
	@Mapping(target = "startOffset", ignore = true)
	@Mapping(target = "responsiveWidth", ignore = true)
	@Mapping(target = "rawTransformation", ignore = true)
	@Mapping(target = "radius", ignore = true)
	@Mapping(target = "prefix", ignore = true)
	@Mapping(target = "params", ignore = true)
	@Mapping(target = "page", ignore = true)
	@Mapping(target = "overlay", ignore = true)
	@Mapping(target = "opacity", ignore = true)
	@Mapping(target = "offset", ignore = true)
	@Mapping(target = "named", ignore = true)
	@Mapping(target = "keyframeInterval", ignore = true)
	@Mapping(target = "ifCondition", ignore = true)
	@Mapping(target = "fps", ignore = true)
	@Mapping(target = "flags", ignore = true)
	@Mapping(target = "endOffsetPercent", ignore = true)
	@Mapping(target = "endOffset", ignore = true)
	@Mapping(target = "effect", ignore = true)
	@Mapping(target = "durationPercent", ignore = true)
	@Mapping(target = "duration", ignore = true)
	@Mapping(target = "dpr", ignore = true)
	@Mapping(target = "density", ignore = true)
	@Mapping(target = "delay", ignore = true)
	@Mapping(target = "defaultImage", ignore = true)
	@Mapping(target = "customPreFunction", ignore = true)
	@Mapping(target = "customFunction", ignore = true)
	@Mapping(target = "colorSpace", ignore = true)
	@Mapping(target = "color", ignore = true)
	@Mapping(target = "chainWith", ignore = true)
	@Mapping(target = "border", ignore = true)
	@Mapping(target = "bitRate", ignore = true)
	@Mapping(target = "background", ignore = true)
	@Mapping(target = "audioFrequency", ignore = true)
	@Mapping(target = "audioCodec", ignore = true)
	@Mapping(target = "angle", ignore = true)
	@Mapping(target = "fetchFormat", source = "format")
	com.cloudinary.Transformation map(com.umcs.enterprise.types.Transformation value);

	com.umcs.enterprise.types.Transformation copy(
		com.umcs.enterprise.types.Transformation transformation
	);

	@ValueMappings(
		{
			@ValueMapping(target = "jp2", source = "JPEG_2000"),
			@ValueMapping(target = "jxr", source = "JPEG_XR")
		}
	)
	@EnumMapping(nameTransformationStrategy = "case", configuration = "lower")
	String formatToString(com.umcs.enterprise.types.Format value);

	@EnumMapping(nameTransformationStrategy = "case", configuration = "lower")
	String cropToString(com.umcs.enterprise.types.Crop value);

	@EnumMapping(nameTransformationStrategy = "case", configuration = "lower")
	String gravityToString(com.umcs.enterprise.types.Gravity value);

	@ValueMappings(
		{
			@ValueMapping(target = "auto", source = "DEFAULT"),
			@ValueMapping(target = "auto:best", source = "BEST"),
			@ValueMapping(target = "auto:eco", source = "ECO"),
			@ValueMapping(target = "auto:good", source = "GOOD"),
			@ValueMapping(target = "auto:low", source = "LOW")
		}
	)
	String qualityAutoToString(com.umcs.enterprise.types.QualityAuto value);

   default String qualityToString(com.umcs.enterprise.types.Quality quality){
	   if (quality == null) {
		   return null;
	   }

        if(quality.getPercentage() !=null){
            return  quality.getPercentage().toString();
        }
        return  qualityAutoToString(quality.getAuto());
    }

	default String aspectRationToString(com.umcs.enterprise.types.AspectRatio value) {
		if (value == null) {
			return null;
		}
		return "" + value.getWidth() + ":" + value.getHeight();
	}
}
