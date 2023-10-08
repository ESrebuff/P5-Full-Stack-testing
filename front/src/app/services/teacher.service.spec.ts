import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let mockHttpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, HttpClientTestingModule],
      providers: [TeacherService],
    });
    service = TestBed.inject(TeacherService);
    mockHttpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verify that there are no pending HTTP requests
    mockHttpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all teachers from the API by GET', () => {
    const mockTeachers: Teacher[] = [
      {
        id: 1,
        lastName: 'Dupont',
        firstName: 'Jean',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        lastName: 'Dupont',
        firstName: 'George',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    // Request all teachers
    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');

    // Respond to that request
    req.flush(mockTeachers);
  });

  it('should retrieve a teacher using the ID from the API by GET', () => {
    const teacherId = '1';
    const mockTeacher: Teacher = {
      id: 1,
      lastName: 'Jean',
      firstName: 'Jean',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    // Request a teacher by ID
    service.detail(teacherId).subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/teacher/${teacherId}`);
    expect(req.request.method).toBe('GET');

    // Respond to that request
    req.flush(mockTeacher);
  });
});
