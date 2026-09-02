package com.devon.building.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OAuth2PictureFetcher {

    private static final Logger log = Logger.getLogger(OAuth2PictureFetcher.class.getName());

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Downloads profile picture bytes from Google's picture URL (claim "picture").
     * Only allows known Google image hosts to reduce SSRF risk.
     */
    public byte[] fetchGoogleProfilePicture(String pictureUrl) {
        if (!StringUtils.hasText(pictureUrl)) {
            return null;
        }
        try {
            URI uri = URI.create(pictureUrl.trim());
            if (!isAllowedGooglePictureHost(uri)) {
                log.warning("Rejected profile picture URL (untrusted host): " + uri.getHost());
                return null;
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();
            HttpResponse<byte[]> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                byte[] body = response.body();
                return body != null && body.length > 0 ? body : null;
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to download OAuth2 profile picture: " + e.getMessage(), e);
        }
        return null;
    }

    private boolean isAllowedGooglePictureHost(URI uri) {
        if (!"https".equalsIgnoreCase(uri.getScheme())) {
            return false;
        }
        String host = uri.getHost();
        if (host == null) {
            return false;
        }
        host = host.toLowerCase();
        return host.endsWith(".googleusercontent.com")
                || host.endsWith(".ggpht.com")
                || host.equals("googleusercontent.com")
                || host.equals("lh3.googleusercontent.com")
                || host.equals("avatars.githubusercontent.com");
    }
}

