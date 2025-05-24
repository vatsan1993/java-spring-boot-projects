package com.dailycodeworks.dream_shop.service.refreshToken;



import java.util.Optional;

import com.dailycodeworks.dream_shop.entity.RefreshToken;

public interface IRefreshTokenService {
	 public RefreshToken createRefreshToken(Long userId);
	 public RefreshToken verifyExpiration(RefreshToken token) ;
	 public int deleteByUserId(Long userId);
	 public Optional<RefreshToken> findByToken(String token);

}
