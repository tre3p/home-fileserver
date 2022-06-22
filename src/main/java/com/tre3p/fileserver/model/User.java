package com.tre3p.fileserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String username;

    @NotNull
    @JsonIgnore
    private String hashedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @CreationTimestamp
    private Date createdAt;

    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }
}
