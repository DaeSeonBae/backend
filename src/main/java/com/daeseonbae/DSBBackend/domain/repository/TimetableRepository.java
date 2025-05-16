package com.daeseonbae.DSBBackend.domain.repository;

import com.daeseonbae.DSBBackend.domain.entity.TimetableEntity;
import com.daeseonbae.DSBBackend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Integer> {
    List<TimetableEntity> findByUserAndDayOfWeek(UserEntity user, String dayOfWeek);
}
