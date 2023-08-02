package com.project.server.controller;

import com.project.server.model.Usuario;
import com.project.server.config.EncryptedProperties;
import com.project.server.model.dto.UserLoginRequest;
import com.project.server.model.mapper.UsuarioResponseMapper;
import com.project.server.repository.UsuarioRepository;
import com.project.server.services.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Autenticação")
@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private EncryptedProperties encryptedProperties;


    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @ApiOperation(value = "Buscar usuário autenticado")
    @GetMapping("/me")
    public ResponseEntity<Object> getMe(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(auth.getPrincipal());
    }

    private Map<String, Object> createTokenMap(Usuario usuario) {
        Map<String, Object> tokenMap = new HashMap<>();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date expiration = calendar.getTime();
        String token = Jwts.builder().setSubject(usuario.getUsername())
                .claim("roles", usuario.getPerfil())
                .setIssuedAt(now)
                .setIssuer("Server Realm")
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
        tokenMap.put("token", token);
        tokenMap.put("usuario", UsuarioResponseMapper.usuarioDTO(usuario));
        return tokenMap;
    }


    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginRequest user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            Usuario usuario = usuarioService.findByUserName(authentication.getName());
            Map<String, Object> tokenMap = createTokenMap(usuario);
            return new ResponseEntity<>(tokenMap, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        usuario = usuarioService.createUser(usuario);
        return new ResponseEntity<>(UsuarioResponseMapper.usuarioDTO(usuario), HttpStatus.CREATED);
    }

}
