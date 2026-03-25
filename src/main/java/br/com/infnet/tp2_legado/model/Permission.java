package br.com.infnet.tp2_legado.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions", schema = "audit")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mapeando para a coluna 'code' que o Hibernate não encontrou antes
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "descricao")
    private String descricao;
}