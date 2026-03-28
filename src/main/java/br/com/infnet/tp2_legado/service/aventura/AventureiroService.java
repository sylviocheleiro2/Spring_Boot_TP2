package br.com.infnet.tp2_legado.service.aventura;

import br.com.infnet.tp2_legado.model.audit.Organizacao;
import br.com.infnet.tp2_legado.model.audit.Usuario;
import br.com.infnet.tp2_legado.model.aventura.Aventureiro;

import br.com.infnet.tp2_legado.repository.audit.OrganizacaoRepository;
import br.com.infnet.tp2_legado.repository.audit.UsuarioRepository;
import br.com.infnet.tp2_legado.repository.aventura.AventureiroRepository;

import br.com.infnet.tp2_legado.dto.audit.OrganizacaoResponse;
import br.com.infnet.tp2_legado.dto.aventura.AventureiroRequest;
import br.com.infnet.tp2_legado.dto.aventura.AventureiroResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AventureiroService {

    private final AventureiroRepository aventureiroRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public AventureiroResponse cadastrar(AventureiroRequest request) {
        // 1. Validar existência da Organização
        Organizacao org = organizacaoRepository.findById(request.organizacaoId())
                .orElseThrow(() -> new RuntimeException("ERRO: Organização não encontrada."));

        // 2. Validar existência do Usuário Responsável
        Usuario responsavel = usuarioRepository.findById(request.usuarioResponsavelId())
                .orElseThrow(() -> new RuntimeException("ERRO: Usuário responsável não encontrado."));

        // 3. REGRA: Restrição de Relacionamento Cruzado
        // O usuário que cadastra DEVE pertencer à mesma organização do aventureiro.
        if (!responsavel.getOrganizacao().getId().equals(org.getId())) {
            throw new RuntimeException("VIOLAÇÃO: O usuário responsável não pertence à organização do aventureiro.");
        }

        // 4. Mapeamento Entity com Regras de Integridade
        Aventureiro aventureiro = Aventureiro.builder()
                .nome(request.nome())
                .classe(request.classe())
                .nivel(request.nivel() != null && request.nivel() >= 1 ? request.nivel() : 1) // Min 1
                .ativo(true) // Novo aventureiro sempre ativo
                .organizacao(org)
                .usuarioResponsavel(responsavel)
                .build();

        Aventureiro salvo = aventureiroRepository.save(aventureiro);

        // 5. Conversão para DTO
        return new AventureiroResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getClasse(),
                salvo.getNivel(),
                salvo.getAtivo(),
                new OrganizacaoResponse(
                        salvo.getOrganizacao().getId(),
                        salvo.getOrganizacao().getNome()
                )
        );
    }
}