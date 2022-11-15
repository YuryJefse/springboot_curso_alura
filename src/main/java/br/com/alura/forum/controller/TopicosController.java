package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<TopicoDTO> lista(String nomeCurso) {
        if (nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();

            return TopicoDTO.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);

            return TopicoDTO.converter(topicos);
        }
    }

    @PostMapping
    public ResponseEntity<TopicoDTO> create(@RequestBody @Valid TopicoForm topicoForm,
            UriComponentsBuilder uriBuilder) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoDetailDTO> detail(@PathVariable Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new TopicoDetailDTO(topico.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
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
