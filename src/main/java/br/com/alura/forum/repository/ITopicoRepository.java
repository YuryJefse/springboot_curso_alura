package br.com.alura.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.forum.modelo.Topico;

@Repository
public interface ITopicoRepository extends JpaRepository<Topico, Long> {

    Page<Topico> findByCurso_Nome(String cursoNome, Pageable paginacao);
    
}
