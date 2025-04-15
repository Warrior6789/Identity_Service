package com.philong.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.philong.identity_service.entity.InvalidatedToken;
import com.philong.identity_service.entity.User;
import com.philong.identity_service.exception.AppException;
import com.philong.identity_service.exception.Error;
import com.philong.identity_service.repository.InvalidatedTokenRepository;
import com.philong.identity_service.repository.UserRepository;
import com.philong.identity_service.request.AuthenticationRequest;
import com.philong.identity_service.request.IntrospectRequest;
import com.philong.identity_service.request.LogoutRequest;
import com.philong.identity_service.response.AuthenticationResponse;
import com.philong.identity_service.response.IntrospectResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor // tạo constructor với các field đánh dấu là final
public class AuthenticationService implements IAuthenticationService {
    @NonFinal
    @Value("${security.jwt.signkey}")
    private String signKey;

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(Error.USER_NOT_EXIST)
        );
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) {
            throw new AppException(Error.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();

    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        JWSVerifier verify = new MACVerifier(signKey.getBytes());
        boolean isValid = true;
        try{
            verifyToken(request.getToken()); // throw error if invalid
        }catch(AppException e){
            isValid = false;
        }
        // .verify kiểm tra chữ ký
        // method verify sẽ lấy header và payload
        // sd thuật toán HMAC và khóa bí mật mà t cấp cho MACverifier để tính lại chữ ký
        // sau đó so sánh hai chữ ký

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public void Logout(LogoutRequest request){
        try {
            var signToken = verifyToken(request.getToken());

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryDate = signToken.getJWTClaimsSet().getExpirationTime();

            invalidatedTokenRepository.save(InvalidatedToken.builder().id(jit).expiryTime(expiryDate).build());

        } catch (ParseException | JOSEException e){
            log.error(e.getMessage());
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verify = new MACVerifier(signKey.getBytes()); // chịu trách nhiệm xác thực chữ ký

        SignedJWT signedJwt = SignedJWT.parse(token);

        Date expiryTime = signedJwt.getJWTClaimsSet().getExpirationTime();

        var verified = signedJwt.verify(verify); // method verify sd jwsVerifier để thực hiện:
        // lấy header và payload của signedjwt

        if(!(verified && expiryTime.after(new Date()))) {
            throw new AppException(Error.UNAUTHENTICATED);
        }

        // kiểm tra trong db
        if(invalidatedTokenRepository.existsById(signedJwt.getJWTClaimsSet().getJWTID())){
            throw new AppException(Error.UNAUTHENTICATED);
        }


        return signedJwt;
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); // thường chứa thông tin về header của jwt

        JWTClaimsSet jwtClaimSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // dùng để định danh thực thể mà token này đại diện
                .issuer("identity_service") // phát hành
                .issueTime(new Date()) // chỉ định thời điểm mà token được phát hành
                .expirationTime(new Date(Instant.now().plus(1,ChronoUnit.HOURS).toEpochMilli()))
                //
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimSet.toJSONObject()); // chuyển đổi jwtClaims thành json

        JWSObject jwsObject = new JWSObject(header, payload); // chuẩn bị data đầu vào
        // jwsobject đã chứa thông tin về thuật toán và dữ liệu
        try {
            jwsObject.sign(new MACSigner(signKey.getBytes())); // tạo chữ ký, chuyển đổi signer_key về byte để có thể làm việc với thuật toán HMAC
            return jwsObject.serialize();
        }catch(JOSEException e){
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        StringJoiner builder = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role ->
                    {
                       builder.add("ROLE_"+role.getName());
                       if(!CollectionUtils.isEmpty(role.getPermissions())){
                           role.getPermissions().forEach(permission -> builder.add(permission.getName()));
                       }
                    }
            );

        }
        log.warn(builder.toString());
        return builder.toString();
    }
}

//Stringjoiner là class của java 8, nằm trong java.util, nó được sd để xây dựng 1 chuỗi ký tự
// được phân tách bằng dấu phân cách cho trước
// CollectionUtils chứa các phương thức tĩnh:
// vd các phương thức thông dụng: isEmpty(Collection<?> coll) và isNotEmpty(Collection<?> coll)
// .foreach() đây là phương thức cung cấp bởi collection trong java 8 nó cho phép
// lặp qua từng phần tử trong collection và thực hiện hành động nào đó lên từng phần tử
// builder::add là method reference tham chiếu tới pt add() của đối tượng builder(StringJoinner)
// mỗi pt trong user.getRole(), pth add() của StringJoinner