package com.conferencehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String institution;

    @Column
    private String phone;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Registration> registrations;

    @OneToMany(mappedBy = "submittedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaperSubmission> paperSubmissions;

    public enum Role {
        ADMIN, PARTICIPANT
    }

    // ── Constructors ──────────────────────────────────────────────
    public User() {}

    public User(Long id, String name, String email, String password, Role role,
                String institution, String phone, LocalDateTime createdAt,
                LocalDateTime updatedAt, List<Registration> registrations,
                List<PaperSubmission> paperSubmissions) {
        this.id = id; this.name = name; this.email = email;
        this.password = password; this.role = role;
        this.institution = institution; this.phone = phone;
        this.createdAt = createdAt; this.updatedAt = updatedAt;
        this.registrations = registrations; this.paperSubmissions = paperSubmissions;
    }

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String name; private String email;
        private String password; private Role role; private String institution;
        private String phone;

        public Builder id(Long id)                   { this.id = id; return this; }
        public Builder name(String name)             { this.name = name; return this; }
        public Builder email(String email)           { this.email = email; return this; }
        public Builder password(String password)     { this.password = password; return this; }
        public Builder role(Role role)               { this.role = role; return this; }
        public Builder institution(String i)         { this.institution = i; return this; }
        public Builder phone(String phone)           { this.phone = phone; return this; }

        public User build() {
            User u = new User();
            u.id = this.id; u.name = this.name; u.email = this.email;
            u.password = this.password; u.role = this.role;
            u.institution = this.institution; u.phone = this.phone;
            return u;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }
    public String getName()                          { return name; }
    public void setName(String name)                 { this.name = name; }
    public String getEmail()                         { return email; }
    public void setEmail(String email)               { this.email = email; }
    public String getPassword()                      { return password; }
    public void setPassword(String password)         { this.password = password; }
    public Role getRole()                            { return role; }
    public void setRole(Role role)                   { this.role = role; }
    public String getInstitution()                   { return institution; }
    public void setInstitution(String institution)   { this.institution = institution; }
    public String getPhone()                         { return phone; }
    public void setPhone(String phone)               { this.phone = phone; }
    public LocalDateTime getCreatedAt()              { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt()              { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }
    public List<Registration> getRegistrations()     { return registrations; }
    public void setRegistrations(List<Registration> r){ this.registrations = r; }
    public List<PaperSubmission> getPaperSubmissions(){ return paperSubmissions; }
    public void setPaperSubmissions(List<PaperSubmission> p){ this.paperSubmissions = p; }
}
