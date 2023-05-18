package com.ll.jwt_2023_05_16;

import com.ll.jwt_2023_05_16.base.jwt.JwtProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class JwtTests {

	@Autowired
	private JwtProvider jwtProvider;
	@Value("${custom.jwt.secretKey}")
	private String secretKeyPlain;

	@Test
	@DisplayName("secrectKey는 존재해야합니다.")
	void t1() {
		assertThat(secretKeyPlain).isNotNull();
	}

	@Test
	@DisplayName("SecrectKey 원문으로 hmac 암호화 알고리즘에 맞는 SecrectKey 객체를 만들 수 있다.")
	void t2(){
		String keyBase64Encoded =
				Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());

		SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());

		assertThat(secretKey).isNotNull();

	}

	@Test
	@DisplayName("JwtProvider 객체로 SecretKey 객체를 생성할 수 있다.")
	void t3() {
		SecretKey secretKey = jwtProvider.getSecretKey();

		assertThat(secretKey).isNotNull();
	}

	@Test
	@DisplayName("SecretKey 객체는 단 한번만 생성되어야 한다.")
	void t4() {
		SecretKey secretKey1 = jwtProvider.getSecretKey();
		SecretKey secretKey2 = jwtProvider.getSecretKey();

		assertThat(secretKey1 == secretKey2).isTrue();
	}

	@Test
	@DisplayName("accessToken을 얻는다.")
	void t5(){
		Map<String,Object> claims = new HashMap<>();
		claims.put("id",1L);
		claims.put("username","admin");

		String accessToken = jwtProvider.genToken(claims,60*60*5);
		System.out.println("accessToken : " + accessToken);

		assertThat(accessToken).isNotNull();
	}

}
