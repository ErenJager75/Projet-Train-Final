package fr.ensiie.repository.db;

import fr.ensiie.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, String> {
}
