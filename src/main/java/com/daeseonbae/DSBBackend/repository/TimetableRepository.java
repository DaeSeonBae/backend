package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.TimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Integer> {
}
