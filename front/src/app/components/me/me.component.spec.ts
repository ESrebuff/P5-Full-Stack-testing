import { expect } from '@jest/globals';
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { User } from '../../interfaces/user.interface';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  // Utility function to create mock SessionService
  const createMockSessionService = (admin: boolean, id: number) => {
    return {
      sessionInformation: { admin, id },
      logOut: jest.fn(),
    };
  };

  // Utility function to create mock UserService
  const createMockUserService = (user: User) => {
    return {
      getById: jest.fn().mockReturnValue(of(user)),
      delete: jest.fn().mockReturnValue(of(null)),
    };
  };

  // Mocked objects and services
  const mockSessionService = createMockSessionService(true, 1);

  const mockUser = {
    id: 1,
    email: 'jean@dupont.com',
    lastName: 'Dupont',
    firstName: 'Jean',
    admin: false,
    password: 'password123',
    createdAt: new Date(),
    updatedAt: new Date(),
  } as User;

  const mockUserService = createMockUserService(mockUser);

  const mockRouter = {
    navigate: jest.fn(),
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create MeComponent instance', () => {
    expect(component).toBeTruthy();
  });

  it('should call userService.getById and set the user property', () => {
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith(
      mockSessionService.sessionInformation.id.toString()
    );

    // Specific assertions for user properties
    expect(component.user!.id).toBe(mockUser.id);
    expect(component.user!.email).toBe(mockUser.email);
    // Add more assertions for other properties as needed
  });

  it('should call window.history.back', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should call userService.delete, show a snackbar, and navigate to home', () => {
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith(
      mockSessionService.sessionInformation.id.toString()
    );

    // Verify that matSnackBar.open was called with the correct parameters
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );

    // Verify that sessionService.logOut was called
    expect(mockSessionService.logOut).toHaveBeenCalled();

    // Verify that router.navigate was called with the correct parameter
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
