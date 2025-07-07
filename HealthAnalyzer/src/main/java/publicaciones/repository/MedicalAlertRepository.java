package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import publicaciones.entity.MedicalAlert;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalAlertRepository extends JpaRepository<MedicalAlert, UUID> {
    List<MedicalAlert> findByDeviceId(String deviceId);
}