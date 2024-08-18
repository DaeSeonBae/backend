package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.TimetableRequestDTO;
import com.daeseonbae.DSBBackend.entity.TimetableEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.TimetableRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveTimetable(TimetableRequestDTO timetableRequestDTO, Authentication authentication) {
        Integer userId = ((com.daeseonbae.DSBBackend.dto.CustomUserDetails) authentication.getPrincipal()).getId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저 ID입니다."));

        TimetableEntity timetable = new TimetableEntity();
        timetable.setUser(user);
        timetable.setSubjectName(timetableRequestDTO.getSubjectName());
        timetable.setTime(timetableRequestDTO.getTime());
        timetable.setDayOfWeek(timetableRequestDTO.getDayOfWeek());
        timetable.setLocation(timetableRequestDTO.getLocation());

        timetableRepository.save(timetable);
    }
}
