package espe.edu.ec.notificaciones.repository;

import espe.edu.ec.notificaciones.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notificacion, UUID> {
}