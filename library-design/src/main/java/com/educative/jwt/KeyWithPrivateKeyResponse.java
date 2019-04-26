package com.educative.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@JsonPropertyOrder({"id", "issuer", "content", "private", "description", "expires_at", "created"})
public class KeyWithPrivateKeyResponse extends KeyResponse {
    @JsonProperty("private")
    @Getter @Setter @JsonIgnore
    private String privateKey;

//    public KeyWithPrivateKeyResponse() {
//        super();
//    }


}