package com.jablonski.msezhorregatik.security.dto;

import com.jablonski.msezhorregatik.registration.domain.dto.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "REFRESH_TOKEN")
@Getter
@Setter
@ToString
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPIRY_DATE")
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final RefreshToken refreshToken)) {
            return false;
        }
        return Objects.equals(id, refreshToken.id)
                && Objects.equals(token, refreshToken.token)
                && Objects.equals(expiryDate, refreshToken.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, expiryDate);
    }
}
