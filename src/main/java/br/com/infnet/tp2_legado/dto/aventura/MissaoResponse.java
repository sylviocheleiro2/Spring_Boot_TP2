package br.com.infnet.tp2_legado.dto.aventura;

import br.com.infnet.tp2_legado.dto.audit.OrganizacaoResponse;
import br.com.infnet.tp2_legado.enums.NivelPerigo;
import br.com.infnet.tp2_legado.enums.StatusMissao;

public record MissaoResponse(
        Long id,
        String titulo,
        NivelPerigo nivelPerigo,
        StatusMissao status,
        OrganizacaoResponse organizacao
) {}