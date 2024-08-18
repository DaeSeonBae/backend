package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.TimetableRequestDTO;
import com.daeseonbae.DSBBackend.entity.TimetableEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.TimetableRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

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

        // 입력된 시간의 겹침 여부 확인
        if (isTimeConflict(user, timetableRequestDTO.getDayOfWeek(), timetableRequestDTO.getTime())) {
            throw new IllegalArgumentException("The class already exists at that time.");
        }

        // 겹치지 않으면 저장
        TimetableEntity timetable = new TimetableEntity();
        timetable.setUser(user);
        timetable.setSubjectName(timetableRequestDTO.getSubjectName());
        timetable.setTime(timetableRequestDTO.getTime());
        timetable.setDayOfWeek(timetableRequestDTO.getDayOfWeek());
        timetable.setLocation(timetableRequestDTO.getLocation());

        timetableRepository.save(timetable);
    }

    private boolean isTimeConflict(UserEntity user, String dayOfWeek, String newTime) {
        // 해당 유저의 동일 요일 수업을 모두 가져옴
        List<TimetableEntity> existingTimetables = timetableRepository.findByUserAndDayOfWeek(user, dayOfWeek);

        // 새로 입력된 시간대를 파싱
        LocalTime[] newTimeRange = parseTimeRange(newTime);

        // 기존 시간대와 겹치는지 확인
        for (TimetableEntity timetable : existingTimetables) {
            LocalTime[] existingTimeRange = parseTimeRange(timetable.getTime());

            if (isOverlapping(newTimeRange, existingTimeRange)) {
                return true;
            }
        }
        return false;
    }

    private LocalTime[] parseTimeRange(String timeRange) {
        // 공백 제거
        String[] times = timeRange.replace(" ", "").split("-");

        return new LocalTime[]{
                LocalTime.parse(times[0]),
                LocalTime.parse(times[1])
        };
    }

    private boolean isOverlapping(LocalTime[] range1, LocalTime[] range2) {
        // 두 시간대가 겹치는지 확인
        return !range1[1].isBefore(range2[0]) && !range1[0].isAfter(range2[1]);
    }
}
