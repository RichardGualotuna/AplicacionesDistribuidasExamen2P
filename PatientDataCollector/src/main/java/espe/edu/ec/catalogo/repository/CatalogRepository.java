package espe.edu.ec.catalogo.repository;

import espe.edu.ec.catalogo.entity.VitalSign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<VitalSign, Long> {
}
