package com.valdirsantos714.biblia.entities.biblia;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "versiculo_do_dia")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class VersiculoDoDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_verso")
    private Versos verso;

}
