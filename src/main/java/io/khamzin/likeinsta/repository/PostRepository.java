package io.khamzin.likeinsta.repository;

import io.khamzin.likeinsta.entity.PostEntity;
import io.khamzin.likeinsta.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllByUserOrderByCreatedDateDesc(UserEntity user);

    List<PostEntity> findAllByOrderByCreatedDateDesc();

    Optional<PostEntity> findPostByIdAndUser(Long id, UserEntity user);

}
