/// <reference types="cypress" />
describe('all e2e tests', () => {
  it('Not Found successfull', () => {
    cy.visit('/random');
    cy.url().should('include', '/404');
  });

  it('Register successfully', () => {
    cy.visit('/register');
    cy.url().should('include', '/register');

    cy.get('input[formControlName=firstName]').type('name');
    cy.get('input[formControlName=lastName]').type('mail');
    cy.get('input[formControlName=email]').type(
      `name${Date.now().toString()}@mail.com`
    );
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/login');
  });

  it('Login successfull', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/sessions');
  });

  it('Create session successfull', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [
        {
          id: 99,
          lastName: 'Dupont',
          firstName: 'Jean',
          createdAt: '2023-09-23T12:00:00.789Z',
          updatedAt: '2023-09-23T12:00:00.789Z',
        },
        {
          id: 100,
          lastName: 'Jean',
          firstName: 'Jean',
          createdAt: '2023-09-23T12:00:00.789Z',
          updatedAt: '2023-09-23T12:00:00.789Z',
        },
      ]
    ).as('teacher');

    cy.intercept('POST', '/api/session', {
      body: {
        id: 1,
        name: 'Session 5',
        date: '2023-04-01T09:00:00Z',
        teacher_id: 1234,
        description: 'This is a test session.',
        users: [5678, 9012],
        createdAt: '2023-03-20T12:34:56.789Z',
        updatedAt: '2023-03-20T12:34:56.789Z',
      },
    });

    cy.get('button[routerLink="create"]').click();

    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type('Session de test');
    cy.get('input[formControlName=date]').type('2024-10-10');
    cy.get('mat-select').click();
    cy.get('mat-option').contains('Jean Dupont').click();
    cy.get('textarea').type('Description de la session');
    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/sessions');
  });

  // it('Logout successfull', () => {
  //   cy.contains('Logout').click();
  //   cy.url().should('include', '/');
  // });
});
