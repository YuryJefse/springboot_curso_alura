package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.ITopicoRepository;

@RestController
public class TopicosController {

    @Autowired
    private ITopicoRepository topicoRepository;

    @GetMapping("/topicos")
    @ResponseBody
    public List<TopicoDTO> lista(String nomeCurso) {
        if (nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();

        return TopicoDTO.converter(topicos);   
        } else {
            List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);

            return TopicoDTO.converter(topicos);
        }        
    }
}
