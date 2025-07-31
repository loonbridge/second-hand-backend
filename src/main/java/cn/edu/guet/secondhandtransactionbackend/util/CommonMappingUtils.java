package cn.edu.guet.secondhandtransactionbackend.util;

import org.mapstruct.Named;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class CommonMappingUtils {

    @Named("toUri")
    public static URI toUri(String url) {
        return url == null ? null : URI.create(url);
    }


    @Named("fromUri")
    public static String fromUri(URI uri) {
        return uri == null ? null : uri.toString();
    }

    @Named("toOffsetDateTime")
    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.ofHours(8));
    }

    @Named("toLocalDateTime")
    public static LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
    }
}