package com.example.resource_service.util;

import com.example.resource_service.dtos.SongMetadataRequest;
import lombok.experimental.UtilityClass;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@UtilityClass
public final class Mp3MetadataExtractor {
    public static Metadata extract(byte[] data) throws IOException, TikaException, SAXException {
        var parser = new AutoDetectParser();
        var metadata = new Metadata();

        try (var in = new ByteArrayInputStream(data)) {
            parser.parse(in, new BodyContentHandler(), metadata);
        }
        return metadata;
    }

    public static SongMetadataRequest toSongRequest(Long resourceId, Metadata md) {
        var name = firstNonBlank(md.get("title"), md.get("dc:title"));
        var artist = md.get("xmpDM:artist");
        var album = md.get("xmpDM:album");

        String duration = null;
        var rawDur = md.get("xmpDM:duration");
        if (rawDur != null) {
            duration = toMmSs(rawDur);
        }

        var year = fourDigits(md.get("xmpDM:releaseDate"));
        if (year == null) year = fourDigits(md.get("xmp:CreateDate"));
        if (year == null) year = fourDigits(md.get("meta:creation-date"));

        return SongMetadataRequest.builder()
                .id(resourceId)
                .name(name)
                .artist(artist)
                .album(album)
                .duration(duration)
                .year(year)
                .build();
    }

    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String fourDigits(String value) {
        if (value == null) return null;
        var m = java.util.regex.Pattern.compile("\\b(19\\d{2}|20\\d{2})\\b").matcher(value);
        return m.find() ? m.group(1) : null;
    }

    private static String toMmSs(String raw) {
        try {
            var val = Double.parseDouble(raw);
            var seconds = (val >= 1000) ? Math.round(val / 1000.0) : Math.round(val);
            var mm = seconds / 60;
            var ss = seconds % 60;

            return String.format("%02d:%02d", mm, ss);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
