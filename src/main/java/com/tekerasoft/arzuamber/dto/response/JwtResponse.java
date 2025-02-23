package com.tekerasoft.arzuamber.dto.response;

public record JwtResponse(
        String accessToken,
        String refreshToken
) {
}
