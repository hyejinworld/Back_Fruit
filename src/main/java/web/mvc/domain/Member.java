package web.mvc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "fruit_member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo ;

    @Column(unique = true)
    private String  id;
    private String  pwd;

    @Column(length = 20)
    private String  name;
    private String role;

    @CreationTimestamp
    private LocalDateTime regDate;

}
