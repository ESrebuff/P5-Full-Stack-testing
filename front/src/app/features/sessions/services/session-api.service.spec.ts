import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let mockHttpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, HttpClientTestingModule],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    mockHttpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verify that there are no pending HTTP requests
    mockHttpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions from the API by GET', () => {
    const mockSessions: Session[] = [
      {
        id: 1,
        name: 'Session 1',
        description: 'Description 1',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      {
        id: 2,
        name: 'Session 2',
        description: 'Description 2',
        date: new Date(),
        teacher_id: 2,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];

    // Request all sessions
    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne('api/session');
    expect(req.request.method).toBe('GET');

    // Respond to that request
    req.flush(mockSessions);
  });

  it('should retrieve a session using the ID from the API by GET', () => {
    const sessionId = '1';
    const mockSession: Session = {
      id: 1,
      name: 'Session',
      description: 'Description',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    // Request a session by ID
    service.detail(sessionId).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('GET');

    // Respond to that request
    req.flush(mockSession);
  });

  it('should delete a session by ID from the API by DELETE', () => {
    const sessionId = '1';

    // Request to delete a session by ID
    service.delete(sessionId).subscribe((response) => {
      expect(response).toBeTruthy(); // Assuming your delete endpoint returns a truthy response
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');

    // Respond to that request
    req.flush({});
  });

  it('should create a new session using POST', () => {
    const mockSession: Session = {
      name: 'Session',
      description: 'Description',
      date: new Date(),
      teacher_id: 1,
      users: [],
    };

    // Request to create a new session
    service.create(mockSession).subscribe((createdSession) => {
      expect(createdSession).toEqual(mockSession);
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne('api/session');
    expect(req.request.method).toBe('POST');

    // Respond to that request
    req.flush(mockSession);
  });

  it('should update a session using PUT', () => {
    const sessionId = '1';
    const updatedSession: Session = {
      id: 1,
      name: 'Session',
      description: 'Description',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    // Request to update a session by ID
    service.update(sessionId, updatedSession).subscribe((updated) => {
      expect(updated).toEqual(updatedSession);
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('PUT');

    // Respond to that request
    req.flush(updatedSession);
  });

  it('should allow a user to participate in a session using POST', () => {
    const sessionId = '1';
    const userId = '1';

    // Request for a user to participate in a session
    service.participate(sessionId, userId).subscribe((response) => {
      expect(response).toBeTruthy(); // Assuming your participation endpoint returns a truthy response
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('POST');

    // Respond to that request
    req.flush({});
  });

  it('should allow a user to unparticipate from a session using DELETE', () => {
    const sessionId = '1';
    const userId = '1';

    // Request for a user to unparticipate from a session
    service.unParticipate(sessionId, userId).subscribe((response) => {
      expect(response).toBeTruthy(); // Assuming your unparticipation endpoint returns a truthy response
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('DELETE');

    // Respond to that request
    req.flush({});
  });
});
