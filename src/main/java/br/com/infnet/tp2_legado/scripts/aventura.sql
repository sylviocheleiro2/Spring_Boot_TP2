CREATE SCHEMA IF NOT EXISTS aventura;

CREATE TABLE aventura.aventureiros (
                                       id BIGSERIAL PRIMARY KEY,
                                       organizacao_id BIGINT NOT NULL,
                                       usuario_id BIGINT NOT NULL, -- Usuário responsável pelo cadastro
                                       nome VARCHAR(120) NOT NULL,
                                       classe VARCHAR(50) NOT NULL,
                                       nivel INTEGER NOT NULL DEFAULT 1,
                                       ativo BOOLEAN NOT NULL DEFAULT TRUE,
                                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_aventureiro_organizacao
                                           FOREIGN KEY (organizacao_id)
                                               REFERENCES audit.organizacoes(id),

                                       CONSTRAINT fk_aventureiro_usuario_resp
                                           FOREIGN KEY (usuario_id)
                                               REFERENCES audit.usuarios(id),

                                        -- Regras de Negócio no Banco
                                       CONSTRAINT chk_nivel_minimo CHECK (nivel >= 1)
);

CREATE OR REPLACE FUNCTION aventura.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trg_aventureiros_updated_at
    BEFORE UPDATE ON aventura.aventureiros
    FOR EACH ROW
    EXECUTE FUNCTION aventura.update_updated_at_column();



CREATE TABLE aventura.companheiros (
                                       aventureiro_id BIGINT PRIMARY KEY, -- A PK é a FK, garantindo o relacionamento 1:1
                                       nome VARCHAR(120) NOT NULL,
                                       especie VARCHAR(50) NOT NULL,
                                       lealdade INTEGER NOT NULL DEFAULT 100,

    -- Um companheiro não existe sem um aventureiro
    -- ON DELETE CASCADE garante que se o aventureiro sumir, o companheiro some junto
                                       CONSTRAINT fk_companheiro_aventureiro
                                           FOREIGN KEY (aventureiro_id)
                                               REFERENCES aventura.aventureiros(id)
                                               ON DELETE CASCADE,

    -- Regra de Negócio: lealdade entre 0 e 100
                                       CONSTRAINT chk_lealdade_range CHECK (lealdade BETWEEN 0 AND 100)
);

DROP TABLE IF EXISTS aventura.missoes CASCADE;

CREATE TABLE aventura.missoes (
                                  id BIGSERIAL PRIMARY KEY,
                                  organizacao_id BIGINT NOT NULL,
                                  titulo VARCHAR(150) NOT NULL,
                                  nivel_perigo VARCHAR(50) NOT NULL,
                                  status VARCHAR(50) NOT NULL DEFAULT 'PLANEJADA', -- Padronizado
                                  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                  data_inicio TIMESTAMP WITH TIME ZONE,
                                  data_termino TIMESTAMP WITH TIME ZONE,

    -- Missão pertence a uma organização
                                  CONSTRAINT fk_missao_organizacao
                                      FOREIGN KEY (organizacao_id)
                                          REFERENCES audit.organizacoes(id),

    -- Valores em UPPERCASE para bater com Enums Java
                                  CONSTRAINT chk_status_missao
                                      CHECK (status IN ('PLANEJADA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),

                                  CONSTRAINT chk_nivel_perigo
                                      CHECK (nivel_perigo IN ('BAIXO', 'MEDIO', 'ALTO', 'CRITICO'))
);

CREATE TABLE aventura.participacoes_missao (
                                               missao_id BIGINT NOT NULL,
                                               aventureiro_id BIGINT NOT NULL,
                                               papel VARCHAR(50) NOT NULL, -- Conjunto fixo (ex: LÍDER, TANQUE, SUPORTE)
                                               recompensa_ouro NUMERIC(15, 2) DEFAULT 0,
                                               destaque_mvp BOOLEAN NOT NULL DEFAULT FALSE,
                                               data_registro TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

                                                -- Chave Primária Composta
                                               PRIMARY KEY (missao_id, aventureiro_id),

                                                -- Se a missão ou aventureiro sumir, a participação some
                                               CONSTRAINT fk_participacao_missao
                                                   FOREIGN KEY (missao_id)
                                                       REFERENCES aventura.missoes(id)
                                                       ON DELETE CASCADE,

                                               CONSTRAINT fk_participacao_aventureiro
                                                   FOREIGN KEY (aventureiro_id)
                                                       REFERENCES aventura.aventureiros(id)
                                                       ON DELETE CASCADE,

                                                -- Recompensa não pode ser negativa
                                               CONSTRAINT chk_recompensa_positiva CHECK (recompensa_ouro >= 0)
);