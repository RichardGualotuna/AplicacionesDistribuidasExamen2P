package espe.edu.ec.catalogo.repository;

import espe.edu.ec.catalogo.entity.VitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, UUID> {
    List<VitalSign> findByDeviceId(String deviceId);
}
