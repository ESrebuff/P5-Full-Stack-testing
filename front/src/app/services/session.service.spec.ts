import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  test('should initialize the service with parameters isLogged to false and sessionInformation to undefined', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should login an user', () => {
    const user = {
      token: 'test_token',
      type: 'test_user_type',
      id: 1,
      username: 'testuser',
      firstName: 'Toto',
      lastName: 'Test',
      admin: false,
    };

    service.logIn(user);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(user);
  });

  it('should log out an user by setting user to undefined and isLogged to false', () => {
    const user = {
      token: 'test_token',
      type: 'test_user_type',
      id: 1,
      username: 'testuser',
      firstName: 'Toto',
      lastName: 'Test',
      admin: false,
    };

    service.sessionInformation = user;
    service.isLogged = true;

    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should return an observable for isLogged', () => {
    const isLogged = service.$isLogged();
    expect(isLogged).toBeTruthy();
  });
});
