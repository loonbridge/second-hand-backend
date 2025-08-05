package cn.edu.guet.secondhandtransactionbackend.util;

import org.mapstruct.Named;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

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

    @Named("stringListToUriList")
    public static List<URI> stringListToUriList(List<String> strings) {
        if (strings == null) {
            return null;
        }
        return strings.stream()
                .map(CommonMappingUtils::toUri)
                .collect(Collectors.toList());
    }

    // localDateTime to LOcalDate

    @Named("toLocalDate")
    public static java.time.LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toLocalDate();

    }

    @Named("toLocalDateTimeFromLocalDate")
    public static LocalDateTime toLocalDateTimeFromLocalDate(LocalDate localDate
    ) {
        return localDate == null ? null : localDate.atStartOfDay();
    }
}