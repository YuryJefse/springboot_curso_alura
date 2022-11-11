package br.com.alura.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.forum.modelo.Curso;

@Repository
public interface ICursoRepository extends JpaRepository<Curso, Long> {

    Curso findByNome(String nomeCurso);
    
}
