package br.com.alura.forum.config.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.config.service.TokenService;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.IUsuarioRepository;

public class AutenticacaoFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private IUsuarioRepository usuarioRepository;

    public AutenticacaoFilter(TokenService tokenService, IUsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);

        boolean valido = tokenService.validarToken(token);

        if (valido) {
            Long idUsuarioToken = tokenService.getIdUsuarioToken(token);
            Optional<Usuario> usuarioPesquisa = usuarioRepository.findById(idUsuarioToken);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    usuarioPesquisa.get(), null, usuarioPesquisa.get().getAuthorities()));
        }

        filterChain.doFilter(request, response);

    }

    private String recuperarToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7);
    }

}
