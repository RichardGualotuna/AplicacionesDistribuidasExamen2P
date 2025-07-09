package publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import publicaciones.entity.MedicalAlert;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalAlertRepository extends JpaRepository<MedicalAlert, UUID> {

    List<MedicalAlert> findByDeviceId(String deviceId);

    List<MedicalAlert> findByType(String type);

    List<MedicalAlert> findByDeviceIdAndType(String deviceId, String type);

    @Query("SELECT ma FROM MedicalAlert ma WHERE ma.timestamp >= :fromDate ORDER BY ma.timestamp DESC")
    List<MedicalAlert> findAlertsFromDate(@Param("fromDate") ZonedDateTime fromDate);

    @Query("SELECT ma FROM MedicalAlert ma WHERE ma.deviceId = :deviceId AND ma.timestamp >= :fromDate ORDER BY ma.timestamp DESC")
    List<MedicalAlert> findByDeviceIdAndTimestampAfter(@Param("deviceId") String deviceId, @Param("fromDate") ZonedDateTime fromDate);
}