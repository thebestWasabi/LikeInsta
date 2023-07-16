package io.khamzin.likeinsta.repository;

import io.khamzin.likeinsta.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    Optional<ImageEntity> findByUserId(Long userId);

    Optional<ImageEntity> findByPostId(Long postId);

}
