package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Smith");
        teacher.setFirstName("Jane");
    }

    @Test
    void findAll_shouldReturnAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertEquals(1, result.size());
        assertEquals("Smith", result.get(0).getLastName());
        assertEquals("Jane", result.get(0).getFirstName());
        verify(teacherRepository).findAll();
    }

    @Test
    void findById_shouldReturnNull_whenTeacherNotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(1L);

        assertNull(result);
        verify(teacherRepository).findById(1L);
    }
}