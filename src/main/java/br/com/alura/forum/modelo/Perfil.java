package br.com.alura.forum.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Perfil implements GrantedAuthority {

    @Id
    private Long id;
    private String nomePerfil;

    @Override
    public String getAuthority() {
        return this.nomePerfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePerfil() {
        return nomePerfil;
    }

    public void setNomePerfil(String nomePerfil) {
        this.nomePerfil = nomePerfil;
    }

}
