package com.educative.jwt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
@ToString
@JsonPropertyOrder({"id", "issuer", "content", "description", "expires_at", "created"})
public class KeyResponse implements Serializable {

        @JsonProperty("id")
        @Getter @Setter
        Long id;

        @JsonProperty("issuer")
        @Getter @Setter
        String issuer;

        @JsonProperty("content")
        @Getter @Setter
        String content;

        @JsonProperty("description")
        @Getter @Setter
        String description;

        @JsonProperty("expires_at")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        @Getter @Setter
        Date expiresAt;

        @JsonProperty("created")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        @Getter @Setter
        Date created;

        public KeyResponse() {
        }

}
