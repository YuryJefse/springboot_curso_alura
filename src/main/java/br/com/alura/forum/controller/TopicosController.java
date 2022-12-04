package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDetailDTO;
import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.ICursoRepository;
import br.com.alura.forum.repository.ITopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private ITopicoRepository topicoRepository;

    @Autowired
    private ICursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listarTopicos")
    public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso,
            @PageableDefault(page = 0, size = 50) Pageable paginacao) {
        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);

            return TopicoDTO.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);

            return TopicoDTO.converter(topicos);
        }
    }

    @PostMapping
    @CacheEvict(value = "listarTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> create(@RequestBody @Valid TopicoForm topicoForm,
            UriComponentsBuilder uriBuilder) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    @CacheEvict(value = "listarTopicos", allEntries = true)
    public ResponseEntity<TopicoDetailDTO> detail(@PathVariable Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new TopicoDetailDTO(topico.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "listarTopicos", allEntries = true)
    @Transactional
    public ResponseEntity<TopicoDTO> update(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm form) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            Topico topicoAtualizado = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDTO(topicoAtualizado));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listarTopicos", allEntries = true)
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            topicoRepository.delete(topico.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
