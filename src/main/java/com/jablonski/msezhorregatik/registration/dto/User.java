package com.jablonski.msezhorregatik.registration.dto;

import com.jablonski.msezhorregatik.infrastructure.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Table(
    name = "users",
    indexes = {@Index(name = "users_email_idx", columnList = "email")}
)
@Entity
@EntityListeners({AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;

    @Email
    @ValidEmail
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime creationDate;

    @Column(name = "modification_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modificationDate;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final User user)) {
            return false;
        }
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
