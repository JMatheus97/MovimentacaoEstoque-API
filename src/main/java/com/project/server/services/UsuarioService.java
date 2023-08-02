package com.project.server.services;

import com.project.server.config.EncryptedProperties;
import com.project.server.exception.BadRequestException;
import com.project.server.exception.NotFoundException;
import com.project.server.model.Usuario;
import com.project.server.model.enums.EnumPerfil;
import com.project.server.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private EncryptedProperties encryptedProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByUsername(username);
        if (usuario != null){
            return usuario;
        }else{
            throw new UsernameNotFoundException("Usuario não encontrado!");
        }
    }

    public Usuario createUser(Usuario usuario) {
        if (!usuario.getPerfil().equals(EnumPerfil.GERENTE.name()) && !usuario.getPerfil().equals(EnumPerfil.OPERADOR.name())) {
            throw new BadRequestException("O perfil informado é inválido.", null);
        }

        if (findByUserName(usuario.getUsername()) == null) {
            String encodedPassword = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(encodedPassword);
            return usuarioRepository.save(usuario);
        } else {
            throw new BadRequestException("Usuário já existe.", null);
        }
    }


    public Usuario create(Usuario usuario){
        if (usuarioRepository.findByUsername(usuario.getUsername()) != null){
            throw new BadRequestException("Usuário já cadastrado.", null);
        }else if(usuario.getPassword() == null) {
            throw new BadRequestException("A senha não pode ser nula.", null);
        } else{
            usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            return usuarioRepository.save(usuario);
        }
    }

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }


    public void delete(Long id){
        usuarioRepository.delete(usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado.")));
    }

    public Usuario findByUserName(String userName) {
        return usuarioRepository.findByUsername(userName);
    }


    public Usuario edit(Usuario usuarioNew) throws IllegalAccessException {
        if (usuarioNew.getPassword() != null) {
            usuarioNew.setPassword(new BCryptPasswordEncoder().encode(usuarioNew.getPassword()));
        }
        Usuario usuarioOld = usuarioRepository.findById(usuarioNew.getId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        for (Field field: usuarioNew.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if (field.get(usuarioNew) != null && !field.get(usuarioNew).equals(field.get(usuarioOld))){
                field.set(usuarioOld,field.get(usuarioNew));
            }
        }
        return usuarioRepository.save(usuarioOld);
    }

}
