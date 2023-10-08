import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let mockHttpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, HttpClientTestingModule],
      providers: [UserService],
    });
    service = TestBed.inject(UserService);
    mockHttpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verify that there are no pending HTTP requests
    mockHttpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve a user by ID from the API by GET', () => {
    const userId = '1';
    const mockUser: User = {
      id: 1,
      email: 'jean@jean.com',
      lastName: 'Jean',
      firstName: 'Jean',
      admin: false,
      password: 'password123',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    // Request a user by ID
    service.getById(userId).subscribe((user) => {
      expect(user).toEqual(mockUser);
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/user/${userId}`);
    expect(req.request.method).toBe('GET');

    // Respond to that request
    req.flush(mockUser);
  });

  it('should delete a user by ID from the API by DELETE', () => {
    const userId = '1';

    // Request to delete a user by ID
    service.delete(userId).subscribe((response) => {
      expect(response).toBeTruthy(); // Assuming your delete endpoint returns a truthy response
    });

    // Verify that the HTTP request has been sent
    const req = mockHttpController.expectOne(`api/user/${userId}`);
    expect(req.request.method).toBe('DELETE');

    // Respond to that request
    req.flush({});
  });
});
